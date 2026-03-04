package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "角色和部门关联")
@RestController
@RequestMapping("/sysRoleDept")
public class SysRoleDeptController {

    @Resource
    private SysRoleDeptService sysRoleDeptService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysRoleDept>> queryByPage(@RequestBody SysRoleDeptPageDTO dto) {
        return sysRoleDeptService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysRoleDept> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleDeptService.getById(id));
    }

    @PostMapping("add")
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.save(sysRoleDept));
    }

    @PutMapping("edit")
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.updateById(sysRoleDept));
    }

    @DeleteMapping("delete")
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysRoleDeptService.removeByIds(ids));
    }

}

