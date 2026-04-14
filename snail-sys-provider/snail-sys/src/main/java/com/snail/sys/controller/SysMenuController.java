package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.lang.tree.Tree;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.service.SysRoleMenuService;
import com.snail.sys.vo.RouterDataVO;
import com.snail.sys.vo.RouterVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:44:59
 */
@Tag(name = "菜单权限")
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;
    private final SysRoleMenuService sysRoleMenuService;


    @SaCheckPermission("system:menu:list")
    @GetMapping("/list")
    @Operation(summary = "获取菜单列表")
    public R<List<SysMenu>> list(SysMenu menu) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(menu, userId);
        return R.ok(menus);
    }

    @SaCheckPermission("system:menu:query")
    @GetMapping("/{menuId}")
    @Operation(summary = "根据菜单编号获取详细信息")
    public R<SysMenu> queryById(@PathVariable Long menuId) {
        return R.ok(sysMenuService.getById(menuId));
    }

    @GetMapping("/menuTree")
    @Operation(summary = "获取菜单下拉树列表")
    public R<List<Tree<Long>>> queryMenuTree(SysMenu menu) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(menu, userId);
        return R.ok(sysMenuService.queryMenuTree(menus));
    }

    @GetMapping("/roleMenuTrees/{roleId}")
    @Operation(summary = "获取角色菜单下拉树列表")
    public R<List<Tree<Long>>> roleMenuTrees(@PathVariable("roleId") Long roleId) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(new SysMenu(), userId);
        List<Tree<Long>> trees = sysMenuService.queryMenuTree(menus);
        return R.ok(trees);
    }

    @SaCheckPermission("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @Operation(summary = "新增菜单")
    public R<Boolean> add(@Validated @RequestBody SysMenu sysMenu) {
        if (sysMenuService.checkMenuNameExists(sysMenu)) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && Validator.isUrl(sysMenu.getPath()) ) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }

        return R.ok(sysMenuService.save(sysMenu));
    }

    @SaCheckPermission("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PostMapping("edit")
    @Operation(summary = "编辑菜单")
    public R<Boolean> edit(@Validated @RequestBody SysMenu sysMenu) {
        if (sysMenuService.checkMenuNameExists(sysMenu)) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && Validator.isUrl(sysMenu.getPath()) ) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (sysMenu.getParentId().equals(sysMenu.getId())) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        return R.ok(sysMenuService.updateById(sysMenu));
    }

    @SaCheckPermission("system:menu:remove")
    @DeleteMapping("delete")
    @Operation(summary = "删除菜单")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        if (sysMenuService.hasChildByMenuIds(ids)) {
            return R.warn("存在子菜单，不允许删除");
        } else if (sysRoleMenuService.checkMenuExistRole(ids)) {
            return R.warn("菜单已分配,不允许删除");
        }
        return R.ok(sysMenuService.removeBatchByIds(ids));
    }

    @GetMapping("getRouters")
    @Operation(summary = "获取路由信息")
    public R<RouterDataVO> getRouters() {
        Long userId = LoginUtils.getUserId();
        List<RouterVO> menus = sysMenuService.selectMenuTreeByUserId(userId);
        // 默认首页路由名称，可根据需要从配置或数据库中获取
        RouterDataVO data = RouterDataVO.builder().home("home").routes(menus).build();
        return R.ok(data);
    }

}

