package com.snail.sys.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 角色信息
 *
 * @author Anfm
 * @since 1.0
 */
@Data
@NoArgsConstructor
@Schema(description = "角色信息")
public class RoleDTO implements Serializable {

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "角色名称")
    private String roleName;

    @Schema(description = "角色权限")
    private String roleKey;

    @Schema(description = "数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）")
    private String dataScope;
}
