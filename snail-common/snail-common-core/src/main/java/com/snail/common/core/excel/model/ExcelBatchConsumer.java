package com.snail.common.core.excel.model;

import java.util.List;

/**
 * Excel导入批量消费器
 *
 * @author Levi.
 * @since 1.0
 */
@FunctionalInterface
public interface ExcelBatchConsumer<T> {

    /**
     * 批量消费导入数据
     *
     * @param rows 行数据
     * @return 每行处理结果
     */
    List<ExcelImportRowResult> accept(List<ExcelRow<T>> rows);
}
