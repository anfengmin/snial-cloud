package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysUserPost;
import com.snail.sys.service.SysUserPostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;

/**
 * 用户与岗位关联表(SysUserPost)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:53:03
 */
@Api(tags = "用户与岗位关联表")
@RestController
@RequestMapping("/v1/sysUserPost")
public class SysUserPostController {

    @Resource
    private SysUserPostService sysUserPostService;

    @GetMapping
    @ApiOperation(value = "分页查询")
    public R<Page<SysUserPost>> queryByPage(SysUserPost sysUserPost, PageRequest pageRequest) {
        return R.ok(sysUserPostService.queryByPage(sysUserPost, pageRequest));
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

