package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色和部门关联(SysRoleDept)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role_dept")
@Schema(description = "角色和部门关联")
public class SysRoleDept extends Model<SysRoleDept> {

    private static final long serialVersionUID = -73263080247909543L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;

}
