package com.snail.sys.api.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.snail.sys.api.domain.SysUser;
import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * No explanation is needed - Levi
 *
 * @author Anfm
 * Created time 2025/5/12
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AutoMapper(target = SysUser.class)
@ApiModel(value = "用户")
public class LoginUser implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用户ID")
    private Long id;

    @ApiModelProperty(value = "用户账号")
    private String userCode;

    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @JsonIgnore
    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "部门ID")
    private Long deptId;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "用户唯一标识")
    private String token;

    @ApiModelProperty(value = "用户类型（sys_user系统用户）")
    private String userType;

    @ApiModelProperty(value = "最后登录时间")
    private Date loginDate;

    @ApiModelProperty(value = "过期时间")
    private Long expireTime;

    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "登录地点")
    private String loginLocation;

    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "菜单权限")
    private Set<String> menuPermission;

    @ApiModelProperty(value = "角色权限")
    private Set<String> rolePermission;


}
