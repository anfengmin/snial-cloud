package com.snail.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存基础信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheBasicInfoVO {

    private String redisVersion;

    private String redisMode;

    private String tcpPort;

    private String connectedClients;

    private String uptimeInDays;

    private String usedMemory;

    private String usedCpu;

    private String maxmemory;

    private String aofEnabled;

    private String rdbLastBgsaveStatus;

    private String keyCount;

    private String instantaneousInputKbps;

    private String instantaneousOutputKbps;
}
