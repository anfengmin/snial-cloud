package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据(SysDictData)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:47:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@Schema(description = "字典数据")
public class SysDictData extends Model<SysDictData> {

    private static final long serialVersionUID = 636238931487518643L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "字典编码")
    private Long id;

    @Schema(description = "字典排序")
    private Integer dictSort;

    @Schema(description = "字典标签")
    private String dictLabel;

    @Schema(description = "字典键值")
    private String dictValue;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "样式属性（其他样式扩展）")
    private String cssClass;

    @Schema(description = "表格回显样式")
    private String listClass;

    @Schema(description = "是否默认（Y是 N否）")
    private String isDefault;

    @Schema(description = "状态（0:正常 1:停用）")
    private Integer status;

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
