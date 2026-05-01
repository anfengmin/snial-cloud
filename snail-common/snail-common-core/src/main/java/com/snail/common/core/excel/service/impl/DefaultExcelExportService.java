package com.snail.common.core.excel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.snail.common.core.excel.convert.ExcelBigNumberConvert;
import com.snail.common.core.excel.core.DropDownOptions;
import com.snail.common.core.excel.core.ExcelDownHandler;
import com.snail.common.core.excel.model.ExcelPageFetcher;
import com.snail.common.core.excel.model.ExcelExportRequest;
import com.snail.common.core.excel.service.ExcelExportService;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.utils.file.FileUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 默认Excel导出服务
 *
 * @author Levi.
 * @since 1.0
 */
@Service
public class DefaultExcelExportService implements ExcelExportService {

    private static final String XLSX_CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8";
    private static final String ZIP_CONTENT_TYPE = "application/zip;charset=UTF-8";

    @Override
    public <T> void export(ExcelExportRequest<T> request, HttpServletResponse response) {
        validateRequest(request);
        long totalCount = Math.max(ObjectUtil.defaultIfNull(request.getTotalCount(), 0L), 0L);
        long workbookCapacity = (long) request.getMaxRowsPerSheet() * request.getMaxSheetsPerWorkbook();
        long workbookCount = Math.max(1L, (long) Math.ceil(totalCount * 1D / workbookCapacity));
        boolean zipOutput = Boolean.TRUE.equals(request.getForceZip()) || workbookCount > 1;

        try {
            if (zipOutput) {
                resetResponse(response, request.getFileName(), "zip", ZIP_CONTENT_TYPE);
                exportZip(request, response.getOutputStream());
            } else {
                resetResponse(response, request.getFileName(), "xlsx", XLSX_CONTENT_TYPE);
                exportWorkbookStream(request, response.getOutputStream());
            }
        } catch (IOException e) {
            throw new ServiceException("导出Excel失败");
        }
    }

    private <T> void validateRequest(ExcelExportRequest<T> request) {
        if (ObjectUtil.isNull(request) || ObjectUtil.isNull(request.getHead())) {
            throw new ServiceException("导出参数不完整");
        }
        if (ObjectUtil.defaultIfNull(request.getPageSize(), 0) <= 0
                || ObjectUtil.defaultIfNull(request.getMaxRowsPerSheet(), 0) <= 0
                || ObjectUtil.defaultIfNull(request.getMaxSheetsPerWorkbook(), 0) <= 0) {
            throw new ServiceException("导出分页参数不正确");
        }
        if (ObjectUtil.defaultIfNull(request.getTotalCount(), 0L) > 0 && ObjectUtil.isNull(request.getPageFetcher())) {
            throw new ServiceException("导出分页拉取器不能为空");
        }
    }

    private <T> void exportWorkbookStream(ExcelExportRequest<T> request, OutputStream outputStream) {
        WorkbookWriteContext<T> context =
                new WorkbookWriteContext<>(request, outputStream, 1, needDropDownHandler(request));
        try {
            streamWrite(request, new WorkbookContextRotator<T>() {
                @Override
                public WorkbookWriteContext<T> getCurrentContext() {
                    return context;
                }

                @Override
                public WorkbookWriteContext<T> rotateContext() {
                    throw new ServiceException("单个工作簿容量不足，请启用ZIP导出");
                }
            });
        } finally {
            context.finish();
        }
    }

    private <T> void exportZip(ExcelExportRequest<T> request, ServletOutputStream outputStream) throws IOException {
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
        boolean needDropDownHandler = needDropDownHandler(request);
        final int[] workbookIndex = {1};
        WorkbookWriteContext<T> initialContext = openZipWorkbookContext(request, zipOutputStream, workbookIndex[0], needDropDownHandler);
        final WorkbookWriteContext<T>[] currentContext = new WorkbookWriteContext[]{initialContext};

        try {
            streamWrite(request, new WorkbookContextRotator<T>() {
                @Override
                public WorkbookWriteContext<T> getCurrentContext() {
                    return currentContext[0];
                }

                @Override
                public WorkbookWriteContext<T> rotateContext() {
                    currentContext[0].finish();
                    try {
                        zipOutputStream.closeEntry();
                    } catch (IOException e) {
                        throw new ServiceException("导出ZIP失败");
                    }
                    workbookIndex[0]++;
                    currentContext[0] = openZipWorkbookContext(request, zipOutputStream, workbookIndex[0], needDropDownHandler);
                    return currentContext[0];
                }
            });
            currentContext[0].finish();
            zipOutputStream.closeEntry();
            zipOutputStream.finish();
        } finally {
            zipOutputStream.flush();
        }
    }

    private <T> void streamWrite(ExcelExportRequest<T> request, WorkbookContextRotator<T> rotator) {
        long totalCount = Math.max(ObjectUtil.defaultIfNull(request.getTotalCount(), 0L), 0L);
        if (totalCount == 0L) {
            rotator.getCurrentContext().write(Collections.<T>emptyList());
            return;
        }

        ExcelPageFetcher<T> pageFetcher = request.getPageFetcher();
        long pageSize = request.getPageSize();
        long current = 1L;
        long exportedCount = 0L;
        while (exportedCount < totalCount) {
            List<T> pageData = pageFetcher.fetch(current, pageSize);
            if (CollUtil.isEmpty(pageData)) {
                break;
            }
            int fromIndex = 0;
            while (fromIndex < pageData.size() && exportedCount < totalCount) {
                WorkbookWriteContext<T> context = rotator.getCurrentContext();
                int nextIndex = context.writeChunk(pageData, fromIndex);
                if (nextIndex == fromIndex) {
                    context = rotator.rotateContext();
                    nextIndex = context.writeChunk(pageData, fromIndex);
                    if (nextIndex == fromIndex) {
                        throw new ServiceException("导出工作簿切换失败");
                    }
                }
                exportedCount += nextIndex - fromIndex;
                fromIndex = nextIndex;
            }
            current++;
        }
    }

    private void resetResponse(HttpServletResponse response, String fileName, String extension, String contentType)
            throws UnsupportedEncodingException {
        String safeFileName = StrUtil.blankToDefault(StrUtil.trim(fileName), "export");
        FileUtils.setAttachmentResponseHeader(response, safeFileName + "." + extension);
        response.setContentType(contentType);
    }

    private String buildWorkbookFileName(String fileName, int workbookIndex) {
        String safeFileName = StrUtil.blankToDefault(StrUtil.trim(fileName), "export");
        return safeFileName + "_" + workbookIndex + ".xlsx";
    }

    private <T> WorkbookWriteContext<T> openZipWorkbookContext(ExcelExportRequest<T> request,
                                                               ZipOutputStream zipOutputStream,
                                                               int workbookIndex,
                                                               boolean needDropDownHandler) {
        try {
            zipOutputStream.putNextEntry(new ZipEntry(buildWorkbookFileName(request.getFileName(), workbookIndex)));
        } catch (IOException e) {
            throw new ServiceException("导出ZIP失败");
        }
        return new WorkbookWriteContext<>(request, zipOutputStream, workbookIndex, needDropDownHandler);
    }

    private boolean needDropDownHandler(ExcelExportRequest<?> request) {
        if (Boolean.TRUE.equals(request.getDropDownEnabled())) {
            return true;
        }
        return CollUtil.isNotEmpty(request.getDropDownOptions());
    }

    private interface WorkbookContextRotator<T> {
        WorkbookWriteContext<T> getCurrentContext();

        WorkbookWriteContext<T> rotateContext();
    }

    private static class WorkbookWriteContext<T> {

        private final ExcelExportRequest<T> request;
        private final ExcelWriter writer;
        private final int workbookIndex;
        private final boolean needDropDownHandler;
        private int sheetIndex = 1;
        private int sheetCount = 1;
        private int sheetRowCount = 0;
        private WriteSheet currentSheet;

        private WorkbookWriteContext(ExcelExportRequest<T> request,
                                     OutputStream outputStream,
                                     int workbookIndex,
                                     boolean needDropDownHandler) {
            this.request = request;
            this.needDropDownHandler = needDropDownHandler;
            this.workbookIndex = workbookIndex;
            com.alibaba.excel.write.builder.ExcelWriterBuilder builder = EasyExcel.write(outputStream, request.getHead())
                    .autoCloseStream(false)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .registerConverter(new ExcelBigNumberConvert());
            if (needDropDownHandler) {
                builder.registerWriteHandler(new ExcelDownHandler(request.getDropDownOptions()));
            }
            this.writer = builder.build();
            this.currentSheet = buildSheet();
        }

        private void write(List<T> rows) {
            if (CollUtil.isEmpty(rows)) {
                writer.write(rows, currentSheet);
                return;
            }
            int nextIndex = writeChunk(rows, 0);
            if (nextIndex < rows.size()) {
                throw new ServiceException("当前工作簿容量不足");
            }
        }

        private int writeChunk(List<T> rows, int fromIndex) {
            if (fromIndex >= rows.size()) {
                return fromIndex;
            }
            if (sheetRowCount >= request.getMaxRowsPerSheet()) {
                if (sheetCount >= request.getMaxSheetsPerWorkbook()) {
                    return fromIndex;
                }
                sheetIndex++;
                sheetCount++;
                sheetRowCount = 0;
                currentSheet = buildSheet();
            }
            int remaining = request.getMaxRowsPerSheet() - sheetRowCount;
            int toIndex = Math.min(rows.size(), fromIndex + remaining);
            List<T> chunk = rows.subList(fromIndex, toIndex);
            writer.write(chunk, currentSheet);
            sheetRowCount += chunk.size();
            return toIndex;
        }

        private WriteSheet buildSheet() {
            String baseSheetName = StrUtil.blankToDefault(StrUtil.trim(request.getSheetName()), "Sheet1");
            String sheetName = sheetIndex == 1 ? baseSheetName : baseSheetName + "_" + sheetIndex;
            return EasyExcel.writerSheet(sheetIndex, sheetName).build();
        }

        private int getWorkbookIndex() {
            return workbookIndex;
        }

        private void finish() {
            writer.finish();
        }
    }
}
