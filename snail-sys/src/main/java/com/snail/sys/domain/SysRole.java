package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色信息(SysRole)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@ApiModel(value = "角色信息")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 361781560607356976L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "角色ID")
    private Long id;

    @ApiModelProperty(value = "角色名称")
    private String roleName;

    @ApiModelProperty(value = "角色权限字符串")
    private String roleKey;

    @ApiModelProperty(value = "显示顺序")
    private Integer roleSort;

    @ApiModelProperty(value = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private String dataScope;

    @ApiModelProperty(value = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    @ApiModelProperty(value = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @ApiModelProperty(value = "删除标志（0:存在 1:删除）")
    private Integer deleted;

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
