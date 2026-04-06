package com.snail.sys.dto;

import com.snail.sys.domain.SysRoleMenu;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色和菜单关联(SysRoleMenu)
 *
 * @author makejava
 * @since 2025-05-30 23:06:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "角色和菜单关联")
public class SysRoleMenuPageDTO extends PageDTO<SysRoleMenu> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

    @ApiModelProperty(value = "菜单ID")
    private Long menuId;

}
