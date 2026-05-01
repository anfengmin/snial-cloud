package com.snail.common.core.excel.service;

import com.snail.common.core.excel.model.ExcelImportRequest;
import com.snail.common.core.excel.model.ExcelImportResult;

import java.io.InputStream;

/**
 * Excel导入服务
 *
 * @author Levi.
 * @since 1.0
 */
public interface ExcelImportService {

    /**
     * 导入Excel
     *
     * @param inputStream 输入流
     * @param request     导入请求
     * @return 导入结果
     */
    <T> ExcelImportResult importExcel(InputStream inputStream, ExcelImportRequest<T> request);
}
