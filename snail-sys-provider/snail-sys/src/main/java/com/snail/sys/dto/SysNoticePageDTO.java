package com.snail.sys.dto;

import java.util.Date;

import com.snail.sys.domain.SysNotice;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "通知公告")
public class SysNoticePageDTO extends PageDTO<SysNotice> implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;

}
