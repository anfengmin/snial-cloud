package com.snail.sys.controller;

import cn.hutool.core.lang.tree.Tree;
import com.snail.common.core.utils.R;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "菜单权限")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/menu")
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @GetMapping("/list")
    @ApiOperation(value = "获取菜单列表")
    public R<List<SysMenu>> list(SysMenu menu) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(menu, userId);
        return R.ok(menus);
    }

    @GetMapping("/{menuId}")
    @ApiOperation(value = "根据菜单编号获取详细信息")
    public R<SysMenu> queryById(@PathVariable Long menuId) {
        return R.ok(sysMenuService.getById(menuId));
    }

    @GetMapping("/menuTree")
    @ApiOperation(value = "获取菜单下拉树列表")
    public R<List<Tree<Long>>> queryMenuTree(SysMenu menu) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(menu, userId);
        return R.ok(sysMenuService.queryMenuTree(menus));
    }

    @GetMapping(value = "/roleMenuTrees/{roleId}")
    @ApiOperation(value = "获取角色菜单下拉树列表")
    public R<List<Tree<Long>>> roleMenuTrees(@PathVariable("roleId") Long roleId) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(new SysMenu(), userId);
        List<Tree<Long>> trees = sysMenuService.queryMenuTree(menus);
        return R.ok(trees);
    }

    @PostMapping
    @ApiOperation(value = "新增菜单")
    public R<Boolean> add(@Validated @RequestBody SysMenu sysMenu) {
        return sysMenuService.addMenu(sysMenu);
    }

    @PutMapping
    @ApiOperation(value = "编辑菜单")
    public R<Boolean> edit(@Validated @RequestBody SysMenu sysMenu) {
        return sysMenuService.editMenu(sysMenu);
    }

    @DeleteMapping
    @ApiOperation(value = "删除菜单")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return sysMenuService.deleteMenuByIds(ids);
    }



}

