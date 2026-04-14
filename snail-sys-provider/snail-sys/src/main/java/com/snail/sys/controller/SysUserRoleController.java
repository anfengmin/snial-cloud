package com.snail.sys.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.service.SysUserRoleService;
import com.snail.sys.dto.SysUserRolePageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户和角色关联表
 *
 * @author makejava
 * @since 2025-05-30 23:14:01
 */
@Tag(name = "用户和角色关联表")
@RestController
@RequestMapping("/sysUserRole")
public class SysUserRoleController {

    @Resource
    private SysUserRoleService sysUserRoleService;

    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysUserRole>> queryByPage(@RequestBody SysUserRolePageDTO dto) {
        return sysUserRoleService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysUserRole> queryById(@PathVariable("id") Long id) {
        return R.ok(sysUserRoleService.getById(id));
    }

    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(SysUserRole sysUserRole) {
        return R.ok(sysUserRoleService.save(sysUserRole));
    }

    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(SysUserRole sysUserRole) {
        return R.ok(sysUserRoleService.updateById(sysUserRole));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysUserRoleService.removeByIds(ids));
    }

}

