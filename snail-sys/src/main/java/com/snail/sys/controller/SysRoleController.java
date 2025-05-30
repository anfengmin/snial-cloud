package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysRole;
import com.snail.sys.service.SysRoleService;
import com.snail.sys.dto.SysRolePageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:10
 */
@Api(tags = "角色信息")
@RestController
@RequestMapping("/v1/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysRole>> queryByPage(@RequestBody SysRolePageDTO dto) {
        return sysRoleService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysRole> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysRole sysRole) {
        return R.ok(sysRoleService.save(sysRole));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysRoleService.removeByIds(ids));
    }

}

