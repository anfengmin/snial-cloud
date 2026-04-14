package com.snail.sys.dto;

import com.snail.sys.domain.SysRoleDept;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 角色和部门关联(SysRoleDept)
 *
 * @author makejava
 * @since 2025-05-30 23:06:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色和部门关联")
public class SysRoleDeptPageDTO extends PageDTO<SysRoleDept> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "部门ID")
    private Long deptId;

}
