package com.snail.common.core.excel.model;

import java.util.List;

/**
 * Excel分页数据拉取器
 *
 * @author Levi.
 * @since 1.0
 */
@FunctionalInterface
public interface ExcelPageFetcher<T> {

    /**
     * 按页拉取导出数据
     *
     * @param current 当前页
     * @param size    每页条数
     * @return 数据列表
     */
    List<T> fetch(long current, long size);
}
