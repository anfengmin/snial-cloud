package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysPost;
import com.snail.sys.service.SysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位信息(SysPost)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:52:59
 */
@Api(tags = "岗位信息")
@RestController
@RequestMapping("/v1/sysPost")
public class SysPostController {

    @Resource
    private SysPostService sysPostService;


    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysPost> queryById(@PathVariable("id") Long id) {
        return R.ok(sysPostService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysPost sysPost) {
        return R.ok(sysPostService.save(sysPost));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysPost sysPost) {
        return R.ok(sysPostService.updateById(sysPost));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysPostService.removeByIds(ids));
    }

}

