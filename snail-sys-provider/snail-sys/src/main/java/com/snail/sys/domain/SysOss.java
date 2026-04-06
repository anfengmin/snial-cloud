package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OSS对象存储(SysOss)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:52:20
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_oss")
@ApiModel(value = "OSS对象存储")
public class SysOss extends Model<SysOss> {

    private static final long serialVersionUID = 218914817594058349L;


    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "服务商")
    private String service;

}
