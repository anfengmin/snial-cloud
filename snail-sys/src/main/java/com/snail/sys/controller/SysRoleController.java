package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.sys.domain.SysRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:10
 */
@Api(tags = "角色信息")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sysRole")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @SaCheckPermission("system:role:list")
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询")
    public R<Page<SysRole>> queryByPage(@RequestBody SysRolePageDTO dto) {
        return R.ok(sysRoleService.queryByPage(dto));
    }


    @SaCheckPermission("system:role:query")
    @GetMapping(value = "/{roleId}")
    @ApiOperation(value = "根据角色编号获取详细信息")
    public R<SysRole> getInfo(@PathVariable Long roleId) {
        return R.ok(sysRoleService.selectRoleById(roleId));
    }

    @SaCheckPermission("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<Boolean> add(@Validated @RequestBody SysRole role) {
        sysRoleService.checkRoleAllowed(role);
        if (sysRoleService.checkRoleNameExists(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (sysRoleService.checkRoleKeyExists(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return R.ok(sysRoleService.save(role));

    }
}

