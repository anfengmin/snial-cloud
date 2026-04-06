package com.snail.sys.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/11/3
 * @since 1.0
 */
@Builder
@Data
public class SysUserVo {

    @ApiModelProperty(value = "用户信息")
    private UserVO user;

    @ApiModelProperty(value = "菜单权限")
    private Set<String> menuPermission;

    @ApiModelProperty(value = "角色权限")
    private Set<String> rolePermission;
}
