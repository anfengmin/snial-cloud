package com.snail.sys.dto;

import java.util.Date;

import com.snail.sys.domain.SysOssConfig;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 对象存储配置表(SysOssConfig)
 *
 * @author makejava
 * @since 2025-05-30 23:05:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "对象存储配置表")
public class SysOssConfigPageDTO extends PageDTO<SysOssConfig> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主建")
    private Long ossConfigId;

    @ApiModelProperty(value = "配置key")
    private String configKey;

    @ApiModelProperty(value = "accessKey")
    private String accessKey;

    @ApiModelProperty(value = "秘钥")
    private String secretKey;

    @ApiModelProperty(value = "桶名称")
    private String bucketName;

    @ApiModelProperty(value = "前缀")
    private String prefix;

    @ApiModelProperty(value = "访问站点")
    private String endpoint;

    @ApiModelProperty(value = "自定义域名")
    private String domain;

    @ApiModelProperty(value = "是否https（Y=是,N=否）")
    private String isHttps;

    @ApiModelProperty(value = "域")
    private String region;

    @ApiModelProperty(value = "桶权限类型(0=private 1=public 2=custom)")
    private String accessPolicy;

    @ApiModelProperty(value = "是否默认（0=是,1=否）")
    private Integer status;

    @ApiModelProperty(value = "扩展字段")
    private String ext1;

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
