package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDictType;
import com.snail.sys.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型(SysDictType)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
@Api(tags = "字典类型")
@RestController
@RequestMapping("/v1/sysDictType")
public class SysDictTypeController {

    @Resource
    private SysDictTypeService sysDictTypeService;


    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysDictType> queryById(@PathVariable("id") Long id) {
        return R.ok(sysDictTypeService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysDictType sysDictType) {
        return R.ok(sysDictTypeService.save(sysDictType));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysDictType sysDictType) {
        return R.ok(sysDictTypeService.updateById(sysDictType));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysDictTypeService.removeByIds(ids));
    }

}

