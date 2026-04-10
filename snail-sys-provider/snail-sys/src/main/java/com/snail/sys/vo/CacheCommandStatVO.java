package com.snail.sys.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 缓存命令统计
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CacheCommandStatVO {

    private String name;

    private Long value;
}
