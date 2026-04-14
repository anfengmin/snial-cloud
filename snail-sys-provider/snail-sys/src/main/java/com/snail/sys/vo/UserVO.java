package com.snail.sys.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.snail.sys.domain.SysDept;
import com.snail.sys.domain.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "用户")
public class UserVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户账号")
    private String userCode;

    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String userName;

    @NotBlank(message = "用户昵称不能为空")
    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型（sys_user系统用户）")
    private String userType;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNo;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "帐号状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "部门对象")
    @TableField(exist = false)
    private SysDept dept;

    @Schema(description = "角色对象")
    @TableField(exist = false)
    private List<SysRole> roles;

    @Schema(description = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    @Schema(description = "岗位组")
    @TableField(exist = false)
    private Long[] postIds;

    @TableField(exist = false)
    @Schema(description = "数据权限 当前角色ID")
    private Long roleId;

}
