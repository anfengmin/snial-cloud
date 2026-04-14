package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "OSS对象存储")
public class SysOss extends Model<SysOss> {

    private static final long serialVersionUID = 218914817594058349L;


    @TableId(type = IdType.AUTO)
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

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "服务商")
    private String service;

}
