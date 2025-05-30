package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysUserPost;
import com.snail.sys.service.SysUserPostService;
import com.snail.sys.dto.SysUserPostPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户与岗位关联表
 *
 * @author makejava
 * @since 2025-05-30 23:13:44
 */
@Api(tags = "用户与岗位关联表")
@RestController
@RequestMapping("/v1/sysUserPost")
public class SysUserPostController {

    @Resource
    private SysUserPostService sysUserPostService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUserPost>> queryByPage(@RequestBody SysUserPostPageDTO dto) {
        return sysUserPostService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysUserPost> queryById(@PathVariable("id") Long id) {
        return R.ok(sysUserPostService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysUserPost sysUserPost) {
        return R.ok(sysUserPostService.save(sysUserPost));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysUserPost sysUserPost) {
        return R.ok(sysUserPostService.updateById(sysUserPost));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysUserPostService.removeByIds(ids));
    }

}

