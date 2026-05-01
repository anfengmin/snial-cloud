package com.snail.common.core.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel导入错误
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportError {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 错误消息
     */
    private String message;
}
