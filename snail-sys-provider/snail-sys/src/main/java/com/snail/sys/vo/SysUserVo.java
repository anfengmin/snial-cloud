package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(description = "用户信息")
    private UserVO user;

    @Schema(description = "菜单权限")
    private Set<String> menuPermission;

    @Schema(description = "角色权限")
    private Set<String> rolePermission;
}
