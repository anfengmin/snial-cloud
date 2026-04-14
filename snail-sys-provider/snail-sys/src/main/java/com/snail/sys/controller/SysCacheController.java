package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.snail.common.core.utils.R;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.sys.vo.CacheBasicInfoVO;
import com.snail.sys.vo.CacheCommandStatVO;
import com.snail.sys.vo.CacheMemoryInfoVO;
import com.snail.sys.vo.CacheMonitorVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.redisson.api.RedissonClient;
import org.redisson.api.redisnode.RedisCluster;
import org.redisson.api.redisnode.RedisMasterSlave;
import org.redisson.api.redisnode.RedisNode;
import org.redisson.api.redisnode.RedisNodes;
import org.redisson.api.redisnode.RedisSentinelMasterSlave;
import org.redisson.api.redisnode.RedisSingle;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 缓存监控
 */
@Tag(name = "缓存监控")
@RestController
@RequestMapping("/monitor")
public class SysCacheController {

    @SaCheckPermission("monitor:cache:list")
    @GetMapping("/cache")
    @Operation(summary = "获取缓存监控信息")
    public R<CacheMonitorVO> cache() {
        RedisNode redisNode = resolveRedisNode();

        Map<String, String> serverInfo = redisNode.info(RedisNode.InfoSection.SERVER);
        Map<String, String> clientsInfo = redisNode.info(RedisNode.InfoSection.CLIENTS);
        Map<String, String> memoryInfo = redisNode.info(RedisNode.InfoSection.MEMORY);
        Map<String, String> persistenceInfo = redisNode.info(RedisNode.InfoSection.PERSISTENCE);
        Map<String, String> statsInfo = redisNode.info(RedisNode.InfoSection.STATS);
        Map<String, String> cpuInfo = redisNode.info(RedisNode.InfoSection.CPU);
        Map<String, String> commandStats = redisNode.info(RedisNode.InfoSection.COMMANDSTATS);

        CacheMonitorVO data = CacheMonitorVO.builder()
                .basicInfo(buildBasicInfo(redisNode, serverInfo, clientsInfo, memoryInfo, persistenceInfo, statsInfo, cpuInfo))
                .commandStats(buildCommandStats(commandStats))
                .memoryInfo(buildMemoryInfo(memoryInfo))
                .build();

        return R.ok(data);
    }

    private CacheBasicInfoVO buildBasicInfo(RedisNode redisNode,
                                            Map<String, String> serverInfo,
                                            Map<String, String> clientsInfo,
                                            Map<String, String> memoryInfo,
                                            Map<String, String> persistenceInfo,
                                            Map<String, String> statsInfo,
                                            Map<String, String> cpuInfo) {
        double cpuUsage = NumberUtil.add(
                parseDouble(cpuInfo.get("used_cpu_sys")),
                parseDouble(cpuInfo.get("used_cpu_user"))
        );

        return CacheBasicInfoVO.builder()
                .redisVersion(defaultValue(serverInfo.get("redis_version")))
                .redisMode(defaultValue(serverInfo.get("redis_mode")))
                .tcpPort(defaultValue(serverInfo.get("tcp_port")))
                .connectedClients(defaultValue(clientsInfo.get("connected_clients")))
                .uptimeInDays(defaultValue(serverInfo.get("uptime_in_days")))
                .usedMemory(defaultValue(memoryInfo.get("used_memory_human")))
                .usedCpu(NumberUtil.round(cpuUsage, 2).toString())
                .maxmemory(defaultValue(memoryInfo.get("maxmemory_human")))
                .aofEnabled(defaultValue(persistenceInfo.get("aof_enabled")))
                .rdbLastBgsaveStatus(defaultValue(persistenceInfo.get("rdb_last_bgsave_status")))
                .keyCount(String.valueOf(redisNode.size()))
                .instantaneousInputKbps(defaultValue(statsInfo.get("instantaneous_input_kbps")))
                .instantaneousOutputKbps(defaultValue(statsInfo.get("instantaneous_output_kbps")))
                .build();
    }

    private CacheMemoryInfoVO buildMemoryInfo(Map<String, String> memoryInfo) {
        return CacheMemoryInfoVO.builder()
                .usedMemory(defaultValue(memoryInfo.get("used_memory_human")))
                .usedMemoryBytes(parseLong(memoryInfo.get("used_memory")))
                .maxmemory(defaultValue(memoryInfo.get("maxmemory_human")))
                .maxmemoryBytes(parseLong(memoryInfo.get("maxmemory")))
                .usedMemoryPeak(defaultValue(memoryInfo.get("used_memory_peak_human")))
                .usedMemoryPeakBytes(parseLong(memoryInfo.get("used_memory_peak")))
                .build();
    }

    private List<CacheCommandStatVO> buildCommandStats(Map<String, String> commandStats) {
        if (commandStats == null || commandStats.isEmpty()) {
            return Collections.emptyList();
        }

        return commandStats.entrySet().stream()
                .filter(entry -> StrUtil.startWith(entry.getKey(), "cmdstat_"))
                .map(entry -> CacheCommandStatVO.builder()
                        .name(StrUtil.removePrefix(entry.getKey(), "cmdstat_"))
                        .value(parseCommandCalls(entry.getValue()))
                        .build())
                .filter(item -> item.getValue() != null && item.getValue() > 0)
                .sorted(Comparator.comparingLong(CacheCommandStatVO::getValue).reversed())
                .collect(Collectors.toList());
    }

    private Long parseCommandCalls(String commandStat) {
        if (StrUtil.isBlank(commandStat)) {
            return 0L;
        }

        String callsPart = StrUtil.subBetween(commandStat, "calls=", ",");
        if (StrUtil.isBlank(callsPart) && StrUtil.startWith(commandStat, "calls=")) {
            callsPart = StrUtil.removePrefix(commandStat, "calls=");
        }

        return parseLong(callsPart);
    }

    private RedisNode resolveRedisNode() {
        RedissonClient redissonClient = RedisUtils.getClient();

        RedisNode node = tryResolveSingle(redissonClient);
        if (node != null) {
            return node;
        }

        node = tryResolveMasterSlave(redissonClient);
        if (node != null) {
            return node;
        }

        node = tryResolveSentinel(redissonClient);
        if (node != null) {
            return node;
        }

        node = tryResolveCluster(redissonClient);
        if (node != null) {
            return node;
        }

        throw new IllegalStateException("未找到可用的 Redis 节点");
    }

    private RedisNode tryResolveSingle(RedissonClient redissonClient) {
        try {
            RedisSingle redisSingle = redissonClient.getRedisNodes(RedisNodes.SINGLE);
            return redisSingle.getInstance();
        } catch (Exception ignored) {
            return null;
        }
    }

    private RedisNode tryResolveMasterSlave(RedissonClient redissonClient) {
        try {
            RedisMasterSlave masterSlave = redissonClient.getRedisNodes(RedisNodes.MASTER_SLAVE);
            return masterSlave.getMaster();
        } catch (Exception ignored) {
            return null;
        }
    }

    private RedisNode tryResolveSentinel(RedissonClient redissonClient) {
        try {
            RedisSentinelMasterSlave sentinel = redissonClient.getRedisNodes(RedisNodes.SENTINEL_MASTER_SLAVE);
            return sentinel.getMaster();
        } catch (Exception ignored) {
            return null;
        }
    }

    private RedisNode tryResolveCluster(RedissonClient redissonClient) {
        try {
            RedisCluster cluster = redissonClient.getRedisNodes(RedisNodes.CLUSTER);
            return cluster.getMasters().stream().filter(Objects::nonNull).findFirst().orElse(null);
        } catch (Exception ignored) {
            return null;
        }
    }

    private String defaultValue(String value) {
        return StrUtil.blankToDefault(value, "--");
    }

    private Long parseLong(String value) {
        if (!NumberUtil.isLong(value)) {
            return 0L;
        }
        return Long.parseLong(value);
    }

    private double parseDouble(String value) {
        if (!NumberUtil.isDouble(value)) {
            return 0D;
        }
        return Double.parseDouble(value);
    }
}
