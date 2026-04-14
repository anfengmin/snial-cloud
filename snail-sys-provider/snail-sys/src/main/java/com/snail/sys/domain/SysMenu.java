package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 菜单权限(SysMenu)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:50:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@Schema(description = "菜单权限")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = -47458081641583414L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "菜单ID")
    private Long id;

    @NotBlank(message = "菜单名称不能为空")
    @Schema(description = "菜单名称")
    private String menuName;

    @Schema(description = "父菜单ID")
    private Long parentId;

    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序")
    private Integer orderNum;

    @Size(min = 0, max = 200, message = "路由地址不能超过200个字符")
    @Schema(description = "路由地址")
    private String path;

    @Size(min = 0, max = 200, message = "组件路径不能超过255个字符")
    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "路由参数")
    private String queryParam;

    @Schema(description = "是否为外链（0是 1否）")
    private Integer isFrame;

    @Schema(description = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @NotBlank(message = "菜单类型不能为空")
    @Schema(description = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @Schema(description = "显示状态（0显示 1隐藏）")
    private Integer visible;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0:存在 1:删除）")
    private Integer deleted;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Size(min = 0, max = 100, message = "权限标识长度不能超过100个字符")
    @Schema(description = "权限标识")
    private String perms;

    @Schema(description = "菜单图标")
    private String icon;

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
