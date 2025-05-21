package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单权限(SysMenu)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:50:33
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@ApiModel(value = "菜单权限")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = -47458081641583414L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "菜单ID")
    private Long id;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "父菜单ID")
    private Long parentId;

    @ApiModelProperty(value = "显示顺序")
    private Integer orderNum;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "组件路径")
    private String component;

    @ApiModelProperty(value = "路由参数")
    private String queryParam;

    @ApiModelProperty(value = "是否为外链（0是 1否）")
    private Integer isFrame;

    @ApiModelProperty(value = "是否缓存（0缓存 1不缓存）")
    private Integer isCache;

    @ApiModelProperty(value = "菜单类型（M目录 C菜单 F按钮）")
    private String menuType;

    @ApiModelProperty(value = "显示状态（0显示 1隐藏）")
    private Integer visible;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0:存在 1:删除）")
    private Integer deleted;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

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
