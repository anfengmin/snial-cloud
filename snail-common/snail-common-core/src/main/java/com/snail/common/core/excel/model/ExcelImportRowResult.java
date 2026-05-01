package com.snail.common.core.excel.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel导入单行处理结果
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportRowResult {

    /**
     * 行号
     */
    private Integer rowNum;

    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 结果消息
     */
    private String message;

    public static ExcelImportRowResult success(Integer rowNum) {
        return new ExcelImportRowResult(rowNum, Boolean.TRUE, null);
    }

    public static ExcelImportRowResult fail(Integer rowNum, String message) {
        return new ExcelImportRowResult(rowNum, Boolean.FALSE, message);
    }
}
