package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.snail.common.core.domain.PageBaseEntity;
import com.snail.sys.domain.SysDept;
import com.snail.sys.domain.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

/**
 * 用户(SysUser)
 *
 * @author makejava
 * @since 2025-05-30 23:07:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "用户")
public class SysUserPageDTO extends PageBaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "用户账号")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型（sys_user系统用户）")
    private String userType;

    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNo;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "帐号状态（0:正常 1:停用）")
    private Integer status;

    @ApiModelProperty(value = "删除标志（0:存在 1:删除）")
    private String deleted;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginDate;

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


    @TableField(exist = false)
    @ApiModelProperty(value = "部门对象")
    private SysDept dept;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色对象")
    private List<SysRole> roles;

    @TableField(exist = false)
    @ApiModelProperty(value = "角色组")
    private Long[] roleIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "岗位组")
    private Long[] postIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "数据权限 当前角色ID")
    private Long roleId;


}
