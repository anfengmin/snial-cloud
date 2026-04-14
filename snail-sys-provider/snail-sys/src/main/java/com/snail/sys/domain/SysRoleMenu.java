package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和菜单关联(SysRoleMenu)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_menu")
@Schema(description = "角色和菜单关联")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = -23476875467648078L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;

}
