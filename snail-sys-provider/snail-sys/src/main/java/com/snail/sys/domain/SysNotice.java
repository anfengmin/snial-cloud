package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通知公告(SysNotice)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:51:14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_notice")
@Schema(description = "通知公告")
public class SysNotice extends Model<SysNotice> {

    private static final long serialVersionUID = 507177616524286982L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "公告ID")
    private Long id;

    @Schema(description = "公告标题")
    private String noticeTitle;

    @Schema(description = "公告类型（1通知 2公告）")
    private String noticeType;

    @Schema(description = "公告内容")
    private String noticeContent;

    @Schema(description = "公告状态（0:正常 1:关闭）")
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
