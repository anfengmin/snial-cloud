package com.snail.common.core.excel.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Excel导入结果
 *
 * @author Levi.
 * @since 1.0
 */
@Data
public class ExcelImportResult {

    /**
     * 总行数
     */
    private long totalCount;

    /**
     * 成功数
     */
    private long successCount;

    /**
     * 失败数
     */
    private long failureCount;

    /**
     * 错误明细
     */
    private List<ExcelImportError> errors = new ArrayList<>();

    public void increaseTotalCount() {
        this.totalCount++;
    }

    public void increaseSuccessCount() {
        this.successCount++;
    }

    public void addError(Integer rowNum, String message) {
        this.failureCount++;
        this.errors.add(new ExcelImportError(rowNum, message));
    }
}
