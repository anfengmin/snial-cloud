package com.snail.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存内存信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheMemoryInfoVO {

    private String usedMemory;

    private Long usedMemoryBytes;

    private String maxmemory;

    private Long maxmemoryBytes;

    private String usedMemoryPeak;

    private Long usedMemoryPeakBytes;
}
