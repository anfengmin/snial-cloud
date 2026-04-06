package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 对象存储配置表(SysOssConfig)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:52:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss_config")
@ApiModel(value = "对象存储配置表")
public class SysOssConfig extends Model<SysOssConfig> {

    private static final long serialVersionUID = 527786467057027481L;

    @TableId(type = IdType.INPUT)
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
