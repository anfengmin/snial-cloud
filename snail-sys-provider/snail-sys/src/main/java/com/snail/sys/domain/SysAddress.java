package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 区域字典(SysAddress)实体类
 *
 * @author makejava
 * @since 2026-05-03
 */
@Data
@TableName("sys_address")
@Schema(description = "区域字典")
public class SysAddress implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "区域ID")
    private Long id;

    @Schema(description = "父级行政区划编码")
    private String parentId;

    @Schema(description = "区域名称")
    private String name;

    @Schema(description = "区域简称")
    private String shortName;

    @Schema(description = "完整名称")
    private String fullName;

    @Schema(description = "层级合并名称")
    private String mergerName;

    @Schema(description = "层级（0国家 1省/直辖市/自治区/特别行政区 2地市/州 3区县）")
    private Integer levelType;

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

    @Schema(description = "是否叶子节点（0否 1是）")
    private Integer isLeaf;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0存在 1删除）")
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;
}
