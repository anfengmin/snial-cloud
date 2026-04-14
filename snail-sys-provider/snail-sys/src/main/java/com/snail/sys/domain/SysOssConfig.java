package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "对象存储配置表")
public class SysOssConfig extends Model<SysOssConfig> {

    private static final long serialVersionUID = 527786467057027481L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "主建")
    private Long ossConfigId;

    @Schema(description = "配置key")
    private String configKey;

    @Schema(description = "accessKey")
    private String accessKey;

    @Schema(description = "秘钥")
    private String secretKey;

    @Schema(description = "桶名称")
    private String bucketName;

    @Schema(description = "前缀")
    private String prefix;

    @Schema(description = "访问站点")
    private String endpoint;

    @Schema(description = "自定义域名")
    private String domain;

    @Schema(description = "是否https（Y=是,N=否）")
    private String isHttps;

    @Schema(description = "域")
    private String region;

    @Schema(description = "桶权限类型(0=private 1=public 2=custom)")
    private String accessPolicy;

    @Schema(description = "是否默认（0=是,1=否）")
    private Integer status;

    @Schema(description = "扩展字段")
    private String ext1;

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
