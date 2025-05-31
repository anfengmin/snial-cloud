package com.snail.sys.controller;

import cn.hutool.core.lang.tree.Tree;
import com.snail.common.satoken.utils.LoginUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.dto.SysMenuPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:44:59
 */
@Api(tags = "菜单权限")
@RestController
@RequestMapping("/v1/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

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

    @GetMapping("/queryMenuTree")
    @ApiOperation(value = "获取菜单下拉树列表")
    public R<List<Tree<Long>>> queryMenuTree(SysMenu menu) {
        Long userId = LoginUtils.getUserId();
        List<SysMenu> menus = sysMenuService.queryMenuList(menu, userId);
        return R.ok(sysMenuService.queryMenuTree(menus));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysMenu sysMenu) {
        return R.ok(sysMenuService.save(sysMenu));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysMenu sysMenu) {
        return R.ok(sysMenuService.updateById(sysMenu));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysMenuService.removeByIds(ids));
    }

}

