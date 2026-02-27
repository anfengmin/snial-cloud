package com.snail.sys.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/27
 * @since 1.0
 */

@Data
@NoArgsConstructor
@ApiModel("角色信息")
public class RoleDTO implements Serializable {

    @ApiModelProperty("角色ID")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("角色权限")
    private String roleKey;

    @ApiModelProperty("数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）")
    private String dataScope;

}
