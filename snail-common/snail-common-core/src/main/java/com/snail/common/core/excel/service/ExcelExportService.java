package com.snail.common.core.excel.service;

import com.snail.common.core.excel.model.ExcelExportRequest;

import javax.servlet.http.HttpServletResponse;

/**
 * Excel导出服务
 *
 * @author Levi.
 * @since 1.0
 */
public interface ExcelExportService {

    /**
     * 导出Excel
     *
     * @param request  导出请求
     * @param response 响应
     */
    <T> void export(ExcelExportRequest<T> request, HttpServletResponse response);
}
