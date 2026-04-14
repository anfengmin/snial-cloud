package com.snail.sys.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snail.sys.api.dto.RoleDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名称")
    private String userName;

    /**
     * 用户账号
     */
    @Schema(description = "用户账号")
    private String userCode;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称")
    private String nickName;

    /**
     * 用户头像
     */
    @Schema(description = "用户头像")
    private String avatar;

    /**
     * 用户邮箱
     */
    @Schema(description = "用户邮箱")
    private String email;

    /**
     * 用户手机号
     */
    @Schema(description = "手机号码")
    private String phonenumber;

    /**
     * 用户性别
     */
    @Schema(description = "用户性别")
    private String sex;

    /**
     * 角色对象
     */
    @Schema(description = "角色对象")
    private List<RoleDTO> roles;

    /**
     * 数据权限 当前角色ID
     */
    @Schema(description = "数据权限当前角色ID")
    private Long roleId;

    /**
     * 部门ID
     */
    @Schema(description = "部门ID")
    private Long deptId;

    /**
     * 部门名称
     */
    @Schema(description = "部门名称")
    private String deptName;

    /**
     * 用户类型
     */
    @Schema(description = "用户类型")
    private String userType;

    /**
     * 角色权限
     */
    private Set<String> rolePermission;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    /**
     * 密码
     */
    @JsonIgnore
    private String password;

    public Long getId() {
        return userId;
    }

    public void setId(Long id) {
        this.userId = id;
    }

    public String getPassWord() {
        return password;
    }

    public void setPassWord(String passWord) {
        this.password = passWord;
    }
}
