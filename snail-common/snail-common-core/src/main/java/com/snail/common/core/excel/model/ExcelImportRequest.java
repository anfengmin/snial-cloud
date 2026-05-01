package com.snail.common.core.excel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Excel导入请求
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelImportRequest<T> {

    /**
     * 表头类型
     */
    private Class<T> head;

    /**
     * 是否开启JSR303校验
     */
    @Builder.Default
    private Boolean validate = Boolean.TRUE;

    /**
     * 批量大小
     */
    @Builder.Default
    private Integer batchSize = 200;

    /**
     * 批量消费器
     */
    private ExcelBatchConsumer<T> batchConsumer;
}
