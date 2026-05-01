package com.snail.common.core.excel.model;

import com.snail.common.core.excel.core.DropDownOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Excel导出请求
 *
 * @author Levi.
 * @since 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcelExportRequest<T> {

    /**
     * 导出文件名（不含扩展名）
     */
    @Builder.Default
    private String fileName = "export";

    /**
     * Sheet名称
     */
    @Builder.Default
    private String sheetName = "Sheet1";

    /**
     * 表头类型
     */
    private Class<T> head;

    /**
     * 数据总量
     */
    @Builder.Default
    private Long totalCount = 0L;

    /**
     * 分页拉取器
     */
    private ExcelPageFetcher<T> pageFetcher;

    /**
     * 分页大小
     */
    @Builder.Default
    private Integer pageSize = 1000;

    /**
     * 每个Sheet最大数据行数
     */
    @Builder.Default
    private Integer maxRowsPerSheet = 50000;

    /**
     * 每个工作簿最大Sheet数量
     */
    @Builder.Default
    private Integer maxSheetsPerWorkbook = 10;

    /**
     * 是否强制ZIP导出
     */
    @Builder.Default
    private Boolean forceZip = Boolean.FALSE;

    /**
     * 是否启用下拉校验
     */
    @Builder.Default
    private Boolean dropDownEnabled = Boolean.FALSE;

    /**
     * 下拉选项
     */
    @Builder.Default
    private List<DropDownOptions> dropDownOptions = Collections.emptyList();
}
