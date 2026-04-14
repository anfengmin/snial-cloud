package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 部门(SysDept)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:46:51
 */
@Data
@TableName("sys_dept")
@Schema(description = "部门")
public class SysDept implements Serializable {

    private static final long serialVersionUID = -96652055306476181L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "部门id")
    private Long id;

    @Schema(description = "父部门id")
    private Long parentId;

    @Schema(description = "祖级列表")
    private String ancestors;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "显示顺序")
    private Integer orderNo;

    @Schema(description = "负责人")
    private String leader;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "部门状态（0:正常 1:停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0:存在 1:删除）")
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

}
