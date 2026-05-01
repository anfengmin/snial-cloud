package com.snail.common.core.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel行数据
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelRow<T> {

    /**
     * Excel中的行号（从1开始）
     */
    private Integer rowNum;

    /**
     * 行数据
     */
    private T data;
}
