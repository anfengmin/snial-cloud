package com.snail.sys.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.snail.sys.api.domain.SysDept;
import com.snail.sys.api.domain.SysRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * class
 *
 * @author Levi.
 * Created time 2026/3/4
 * @since 1.0
 */
@Data
@ApiModel(value = "用户")
public class UserVO {

    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "用户账号")
    private String userCode;

    @NotBlank(message = "用户名称不能为空")
    @ApiModelProperty(value = "用户名称")
    private String userName;

    @NotBlank(message = "用户昵称不能为空")
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户类型（sys_user系统用户）")
    private String userType;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @ApiModelProperty(value = "用户邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNo;

    @ApiModelProperty(value = "用户性别（0男 1女 2未知）")
    private String sex;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "帐号状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty(value = "最后登录IP")
    private String loginIp;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginDate;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "部门对象")
    @TableField(exist = false)
    private SysDept dept;

    @ApiModelProperty(value = "角色对象")
    @TableField(exist = false)
    private List<SysRole> roles;

    @ApiModelProperty(value = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    @ApiModelProperty(value = "岗位组")
    @TableField(exist = false)
    private Long[] postIds;

    @TableField(exist = false)
    @ApiModelProperty(value = "数据权限 当前角色ID")
    private Long roleId;

}
