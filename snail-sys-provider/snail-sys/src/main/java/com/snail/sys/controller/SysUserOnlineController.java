package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.comparator.CompareUtil;
import cn.hutool.core.util.StrUtil;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 在线用户监控
 *
 * @author Levi.
 * Created time 2026/4/6
 * @since 1.0
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/online")
public class SysUserOnlineController {

    /**
     * 在线用户列表
     *
     * @param ipaddr 登录地址
     * @param userName 用户名
     */
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    public R<List<SysUserOnline>> list(String ipaddr, String userName) {
        List<SysUserOnline> userOnlineList = getAllOnlineUsers();

        // 3. 动态组合过滤条件（解决 if-else 冗余）
        if (StrUtil.isNotBlank(ipaddr) || StrUtil.isNotBlank(userName)) {
            userOnlineList = userOnlineList.stream()
                    .filter(user -> StrUtil.isBlank(ipaddr) ||
                            StrUtil.containsIgnoreCase(user.getIpaddr(), ipaddr))
                    .filter(user -> StrUtil.isBlank(userName) ||
                            Stream.of(user.getUserName(), user.getUserCode())
                                    .anyMatch(field -> StrUtil.containsIgnoreCase(field, userName)))
                    .collect(Collectors.toList());
        }

        // 4. 按登录时间倒序返回
        userOnlineList = userOnlineList.stream()
                .sorted((a, b) -> CompareUtil.compare(b.getLoginTime(), a.getLoginTime()))
                .collect(Collectors.toList());

        return R.ok(userOnlineList);
    }

    @SaCheckLogin
    @GetMapping("/current-user/devices")
    public R<List<SysUserOnline>> listCurrentUserDevices() {
        Long userId = LoginUtils.getUserId();
        String currentToken = LoginUtils.getTokenValue();

        List<SysUserOnline> devices = getAllOnlineUsers().stream()
                .filter(user -> Objects.equals(user.getUserId(), userId))
                .peek(user -> user.setCurrentDevice(StrUtil.equals(user.getTokenId(), currentToken)))
                .sorted((a, b) -> CompareUtil.compare(b.getLoginTime(), a.getLoginTime()))
                .collect(Collectors.toList());

        return R.ok(devices);
    }

    @SaCheckLogin
    @DeleteMapping("/current-user/devices/{tokenId}")
    public R<Void> forceLogoutCurrentUserDevice(@PathVariable String tokenId) {
        Long userId = LoginUtils.getUserId();
        SysUserOnline targetDevice = findOnlineUserByToken(tokenId);
        if (targetDevice == null) {
            return R.fail("设备不存在或已下线");
        }
        if (!Objects.equals(targetDevice.getUserId(), userId)) {
            return R.fail("无权操作该设备");
        }

        StpUtil.kickoutByTokenValue(tokenId);
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenId);
        return R.ok();
    }

    /**
     * 强退用户
     */
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    public R<Void> forceLogout(@PathVariable String tokenId) {
        // 1. 清除 Sa-Token 的登录状态（踢人下线）
        StpUtil.kickoutByTokenValue(tokenId);

        // 2. 同步删除自定义的在线用户信息缓存
        // 这里的 CacheConstants.ONLINE_TOKEN_KEY 必须和你 list 方法里 get 的 key 完全一致
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenId);
        return R.ok();
    }

    private List<SysUserOnline> getAllOnlineUsers() {
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        if (CollUtil.isEmpty(keys)) {
            return new ArrayList<>();
        }
        return keys.stream()
                .map(k -> k.replace(CacheConstants.LOGIN_TOKEN_KEY, ""))
                .filter(token -> StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) >= -2)
                .map(token -> (SysUserOnline) RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + token))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SysUserOnline findOnlineUserByToken(String tokenId) {
        return (SysUserOnline) RedisUtils.getCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenId);
    }
}
