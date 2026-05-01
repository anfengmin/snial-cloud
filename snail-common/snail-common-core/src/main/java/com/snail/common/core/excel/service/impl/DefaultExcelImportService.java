package com.snail.common.core.excel.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.snail.common.core.excel.model.ExcelBatchConsumer;
import com.snail.common.core.excel.model.ExcelImportRequest;
import com.snail.common.core.excel.model.ExcelImportResult;
import com.snail.common.core.excel.model.ExcelImportRowResult;
import com.snail.common.core.excel.model.ExcelRow;
import com.snail.common.core.excel.service.ExcelImportService;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.utils.ValidatorUtils;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 默认Excel导入服务
 *
 * @author Levi.
 * @since 1.0
 */
@Service
public class DefaultExcelImportService implements ExcelImportService {

    @Override
    public <T> ExcelImportResult importExcel(InputStream inputStream, ExcelImportRequest<T> request) {
        if (inputStream == null || request == null || request.getHead() == null) {
            throw new ServiceException("导入参数不完整");
        }
        BatchExcelImportListener<T> listener = new BatchExcelImportListener<>(request);
        EasyExcel.read(inputStream, request.getHead(), listener).autoCloseStream(false).sheet().doRead();
        return listener.getResult();
    }

    private static class BatchExcelImportListener<T> extends AnalysisEventListener<T> {

        private final ExcelImportRequest<T> request;
        private final ExcelImportResult result = new ExcelImportResult();
        private final List<ExcelRow<T>> batchRows = new ArrayList<>();
        private Map<Integer, String> headMap = new HashMap<>();

        private BatchExcelImportListener(ExcelImportRequest<T> request) {
            this.request = request;
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            this.headMap = headMap;
        }

        @Override
        public void invoke(T data, AnalysisContext context) {
            int rowNum = context.readRowHolder().getRowIndex() + 1;
            result.increaseTotalCount();
            if (Boolean.TRUE.equals(request.getValidate())) {
                try {
                    ValidatorUtils.validate(data);
                } catch (ConstraintViolationException e) {
                    result.addError(rowNum, buildConstraintViolationMessage(e.getConstraintViolations()));
                    return;
                }
            }
            batchRows.add(new ExcelRow<>(rowNum, data));
            if (batchRows.size() >= request.getBatchSize()) {
                flushBatch();
            }
        }

        @Override
        public void onException(Exception exception, AnalysisContext context) {
            if (exception instanceof ExcelDataConvertException) {
                ExcelDataConvertException convertException = (ExcelDataConvertException) exception;
                int rowNum = convertException.getRowIndex() + 1;
                String headerName = headMap.getOrDefault(convertException.getColumnIndex(), "未知列");
                result.increaseTotalCount();
                result.addError(rowNum, StrUtil.format("第{}列({})解析失败", convertException.getColumnIndex() + 1, headerName));
                return;
            }
            throw new ServiceException("Excel导入失败: " + exception.getMessage());
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            flushBatch();
        }

        private void flushBatch() {
            if (CollUtil.isEmpty(batchRows)) {
                return;
            }
            List<ExcelRow<T>> currentBatch = new ArrayList<>(batchRows);
            batchRows.clear();

            ExcelBatchConsumer<T> consumer = request.getBatchConsumer();
            if (consumer == null) {
                currentBatch.forEach(row -> result.increaseSuccessCount());
                return;
            }

            try {
                List<ExcelImportRowResult> rowResults = consumer.accept(currentBatch);
                Map<Integer, ExcelImportRowResult> rowResultMap = new HashMap<>();
                if (CollUtil.isNotEmpty(rowResults)) {
                    rowResults.forEach(item -> rowResultMap.put(item.getRowNum(), item));
                }
                for (ExcelRow<T> row : currentBatch) {
                    ExcelImportRowResult rowResult = rowResultMap.get(row.getRowNum());
                    if (rowResult == null || Boolean.TRUE.equals(rowResult.getSuccess())) {
                        result.increaseSuccessCount();
                    } else {
                        result.addError(row.getRowNum(), StrUtil.blankToDefault(rowResult.getMessage(), "导入失败"));
                    }
                }
            } catch (Exception e) {
                String errorMsg = StrUtil.blankToDefault(e.getMessage(), "导入失败");
                currentBatch.forEach(row -> result.addError(row.getRowNum(), errorMsg));
            }
        }

        private String buildConstraintViolationMessage(Set<ConstraintViolation<?>> violations) {
            return violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("；"));
        }

        private ExcelImportResult getResult() {
            return result;
        }
    }
}
