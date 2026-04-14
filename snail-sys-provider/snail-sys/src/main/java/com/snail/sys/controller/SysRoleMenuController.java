package com.snail.sys.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.service.SysRoleMenuService;
import com.snail.sys.dto.SysRoleMenuPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和菜单关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:37
 */
@Tag(name = "角色和菜单关联")
@RestController
@RequestMapping("/sysRoleMenu")
public class SysRoleMenuController {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysRoleMenu>> queryByPage(@RequestBody SysRoleMenuPageDTO dto) {
        return sysRoleMenuService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysRoleMenu> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleMenuService.getById(id));
    }

    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(SysRoleMenu sysRoleMenu) {
        return R.ok(sysRoleMenuService.save(sysRoleMenu));
    }

    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(SysRoleMenu sysRoleMenu) {
        return R.ok(sysRoleMenuService.updateById(sysRoleMenu));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysRoleMenuService.removeByIds(ids));
    }

}

