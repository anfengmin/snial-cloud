package com.snail.sys.dto;

import java.util.Date;

import com.snail.sys.domain.SysOss;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "OSS对象存储")
public class SysOssPageDTO extends PageDTO<SysOss> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "对象存储主键")
    private Long id;

    @ApiModelProperty(value = "文件名")
    private String fileName;

    @ApiModelProperty(value = "原名")
    private String originalName;

    @ApiModelProperty(value = "文件后缀名")
    private String fileSuffix;

    @ApiModelProperty(value = "URL地址")
    private String url;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "上传人")
    private String createBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "更新人")
    private String updateBy;

    @ApiModelProperty(value = "服务商")
    private String service;

}
