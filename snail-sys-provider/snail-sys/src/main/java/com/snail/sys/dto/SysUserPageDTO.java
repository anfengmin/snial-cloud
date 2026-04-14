package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.snail.common.core.domain.PageBaseEntity;
import com.snail.sys.domain.SysDept;
import com.snail.sys.domain.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "用户")
public class SysUserPageDTO extends PageBaseEntity {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户账号")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型（sys_user系统用户）")
    private String userType;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNo;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "密码")
    private String passWord;

    @Schema(description = "帐号状态（0:正常 1:停用）")
    private Integer status;

    @Schema(description = "删除标志（0:存在 1:删除）")
    private String deleted;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "创建者")
    private String createBy;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新者")
    private String updateBy;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;


    @TableField(exist = false)
    @Schema(description = "部门对象")
    private SysDept dept;

    @TableField(exist = false)
    @Schema(description = "角色对象")
    private List<SysRole> roles;

    @TableField(exist = false)
    @Schema(description = "角色组")
    private Long[] roleIds;

    @TableField(exist = false)
    @Schema(description = "岗位组")
    private Long[] postIds;

    @TableField(exist = false)
    @Schema(description = "数据权限 当前角色ID")
    private Long roleId;


}
