package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位信息(SysPost)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:52:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@Schema(description = "岗位信息")
public class SysPost extends Model<SysPost> {

    private static final long serialVersionUID = -99849742995495600L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "岗位ID")
    private Long id;

    @Schema(description = "岗位编码")
    private String postCode;

    @Schema(description = "岗位名称")
    private String postName;

    @Schema(description = "显示顺序")
    private Integer postSort;

    @Schema(description = "状态（0正常 1停用）")
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

    @Schema(description = "备注")
    private String remark;

}
