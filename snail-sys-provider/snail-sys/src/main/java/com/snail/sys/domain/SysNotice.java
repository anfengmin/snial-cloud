package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "通知公告")
public class SysNotice extends Model<SysNotice> {

    private static final long serialVersionUID = 507177616524286982L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "公告ID")
    private Long id;

    @ApiModelProperty(value = "公告标题")
    private String noticeTitle;

    @ApiModelProperty(value = "公告类型（1通知 2公告）")
    private String noticeType;

    @ApiModelProperty(value = "公告内容")
    private String noticeContent;

    @ApiModelProperty(value = "公告状态（0:正常 1:关闭）")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
