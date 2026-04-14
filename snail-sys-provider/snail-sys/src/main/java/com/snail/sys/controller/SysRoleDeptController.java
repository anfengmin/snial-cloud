package com.snail.sys.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.service.SysRoleDeptService;
import com.snail.sys.dto.SysRoleDeptPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和部门关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:25
 */
@Tag(name = "角色和部门关联")
@RestController
@RequestMapping("/sysRoleDept")
public class SysRoleDeptController {

    @Resource
    private SysRoleDeptService sysRoleDeptService;

    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysRoleDept>> queryByPage(@RequestBody SysRoleDeptPageDTO dto) {
        return sysRoleDeptService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysRoleDept> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleDeptService.getById(id));
    }

    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.save(sysRoleDept));
    }

    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.updateById(sysRoleDept));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysRoleDeptService.removeByIds(ids));
    }

}

