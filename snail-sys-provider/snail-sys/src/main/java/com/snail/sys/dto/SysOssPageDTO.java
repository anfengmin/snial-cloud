package com.snail.sys.dto;

import java.util.Date;

import com.snail.sys.domain.SysOss;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * OSS对象存储(SysOss)
 *
 * @author makejava
 * @since 2025-05-30 23:04:34
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "OSS对象存储")
public class SysOssPageDTO extends PageDTO<SysOss> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "对象存储主键")
    private Long id;

    @Schema(description = "文件名")
    private String fileName;

    @Schema(description = "原名")
    private String originalName;

    @Schema(description = "文件后缀名")
    private String fileSuffix;

    @Schema(description = "URL地址")
    private String url;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "上传人")
    private String createBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "更新人")
    private String updateBy;

    @Schema(description = "服务商")
    private String service;

}
