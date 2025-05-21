package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDept;
import com.snail.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表(SysDept)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:30:46
 */
@Api(tags = "部门表")
@RestController
@RequestMapping("/v1/sysDept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;


    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysDept> queryById(@PathVariable("id") Long id) {
        return R.ok(sysDeptService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysDept sysDept) {
        return R.ok(sysDeptService.save(sysDept));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysDept sysDept) {
        return R.ok(sysDeptService.updateById(sysDept));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysDeptService.removeByIds(ids));
    }

}

