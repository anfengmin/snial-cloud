package com.snail.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 行政区划基础信息
 *
 * @author Levi.
 * @since 2026-05-03
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "行政区划基础信息")
public class RegionInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "区域ID")
    private Long id;

    @Schema(description = "父级行政区划编码")
    private String parentId;

    @Schema(description = "区域名称")
    private String name;

    @Schema(description = "级联显示名称")
    private String label;

    @Schema(description = "级联值")
    private String value;

    @Schema(description = "区域简称")
    private String shortName;

    @Schema(description = "完整名称")
    private String fullName;

    @Schema(description = "层级合并名称")
    private String mergerName;

    @Schema(description = "层级（1省/直辖市/自治区/特别行政区 2地市/州 3区县）")
    private Integer level;

    @Schema(description = "层级名称")
    private String levelName;

    @Schema(description = "电话区号")
    private String cityCode;

    @Schema(description = "邮政编码")
    private String zipCode;

    @Schema(description = "经度")
    private BigDecimal lng;

    @Schema(description = "纬度")
    private BigDecimal lat;

    @Schema(description = "拼音")
    private String pinyin;

    @Schema(description = "首字母")
    private String initial;

    @Schema(description = "排序")
    private Long sort;

    @Schema(description = "是否叶子节点")
    private Boolean isLeaf;
}
