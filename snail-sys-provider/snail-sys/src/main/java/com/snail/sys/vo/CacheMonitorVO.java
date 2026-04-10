package com.snail.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 缓存监控
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheMonitorVO {

    private CacheBasicInfoVO basicInfo;

    private List<CacheCommandStatVO> commandStats;

    private CacheMemoryInfoVO memoryInfo;
}
