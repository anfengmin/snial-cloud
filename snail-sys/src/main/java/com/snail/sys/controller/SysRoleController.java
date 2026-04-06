package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.log.annotation.Log;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.api.vo.OptionVO;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.SysRoleService;
import com.snail.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:10
 */
@Api(tags = "角色信息")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysRole")
public class SysRoleController {

    private final SysRoleService sysRoleService;
    private final SysUserService sysUserService;

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
    @PostMapping("add")
    @ApiOperation(value = "新增角色")
    public R<Boolean> add(@Validated @RequestBody SysRole role) {
        sysRoleService.checkRoleAllowed(role);
        if (sysRoleService.checkRoleNameExists(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (sysRoleService.checkRoleKeyExists(role)) {
            return R.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        return R.ok(sysRoleService.insertRole(role));

    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("edit")
    @ApiOperation(value = "编辑角色")
    public R<Void> edit(@Validated @RequestBody SysRole role) {
        sysRoleService.checkRoleAllowed(role);
        sysRoleService.checkRoleDataScope(role.getId());
        if (sysRoleService.checkRoleNameExists(role)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
        } else if (sysRoleService.checkRoleKeyExists(role)) {
            return R.fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
        }
        if (sysRoleService.updateRole(role)) {
            sysRoleService.cleanOnlineUserByRole(role.getId());
            return R.ok();
        }
        return R.fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    @ApiOperation(value = "修改角色数据权限")
    public R<Boolean> dataScope(@RequestBody SysRole role) {
        sysRoleService.checkRoleAllowed(role);
        sysRoleService.checkRoleDataScope(role.getId());
        return R.ok(sysRoleService.authDataScope(role));
    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    @ApiOperation(value = "修改角色状态")
    public R<Boolean> changeStatus(@RequestBody SysRole role) {
        sysRoleService.checkRoleAllowed(role);
        sysRoleService.checkRoleDataScope(role.getId());
        return R.ok(sysRoleService.updateRoleStatus(role));
    }

    @SaCheckPermission("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    @ApiOperation(value = "删除角色")
    public R<Boolean> remove(@PathVariable("roleIds") List<Long> roleIds) {
        return R.ok(sysRoleService.removeBatchByIds(roleIds));
    }

    @SaCheckPermission("system:role:query")
    @GetMapping("/optionSelect")
    @ApiOperation(value = "角色下拉列表")
    public R<List<OptionVO>> optionSelect() {
        return R.ok(sysRoleService.selectRoleAll());
    }


    @SaCheckPermission("system:role:list")
    @PostMapping("/authUser/allocatedList")
    @ApiOperation(value = "已分配用户列表")
    public R<Page<SysUser>> allocatedList(@RequestBody SysUserPageDTO dto) {
        return R.ok(sysUserService.selectAllocatedList(dto));
    }

    @SaCheckPermission("system:role:list")
    @PostMapping("/authUser/unallocatedList")
    @ApiOperation(value = "未分配用户列表")
    public R<Page<SysUser>> unallocatedList(@RequestBody SysUserPageDTO dto) {
        return R.ok(sysUserService.selectUnallocatedList(dto));
    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    @ApiOperation(value = "取消授权用户")
    public R<Boolean> cancelAuthUser(@RequestBody SysUserRole userRole) {
        return R.ok(sysRoleService.deleteAuthUser(userRole));
    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    @ApiOperation(value = "批量取消授权用户")
    public R<Boolean> cancelAuthUserAll(Long roleId, Long[] userIds) {
        return R.ok(sysRoleService.deleteAuthUsers(roleId, userIds));
    }

    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    @ApiOperation(value = "批量选择用户授权")
    public R<Void> selectAuthUserAll(Long roleId, Long[] userIds) {
        sysRoleService.checkRoleDataScope(roleId);
        sysRoleService.insertAuthUsers(roleId, userIds);
        return R.ok();
    }
}
