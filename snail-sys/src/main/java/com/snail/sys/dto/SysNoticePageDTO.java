package com.snail.sys.dto;

import java.util.Date;

import com.snail.sys.domain.SysNotice;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 通知公告(SysNotice)
 *
 * @author makejava
 * @since 2025-05-29 21:50:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "通知公告")
public class SysNoticePageDTO extends PageDTO<SysNotice> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注")
    private String remark;

}
