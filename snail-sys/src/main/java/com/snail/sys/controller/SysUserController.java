package com.snail.sys.controller;

import com.snail.sys.api.domain.SysUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.service.SysUserService;
import com.snail.sys.dto.SysUserPageDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-28 23:03:42
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/v1/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUser>> queryByPage(@RequestBody SysUserPageDTO dto) {
        return sysUserService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysUser> queryById(@PathVariable("id") Long id) {
        return R.ok(sysUserService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysUser sysUser) {
        return R.ok(sysUserService.save(sysUser));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysUser sysUser) {
        return R.ok(sysUserService.updateById(sysUser));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysUserService.removeByIds(ids));
    }

}

