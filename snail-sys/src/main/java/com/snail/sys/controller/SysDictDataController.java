package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.service.SysDictDataService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典数据表(SysDictData)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
@Api(tags = "字典数据表")
@RestController
@RequestMapping("/v1/sysDictData")
public class SysDictDataController {

    @Resource
    private SysDictDataService sysDictDataService;



    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysDictData> queryById(@PathVariable("id") Long id) {
        return R.ok(sysDictDataService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysDictData sysDictData) {
        return R.ok(sysDictDataService.save(sysDictData));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysDictData sysDictData) {
        return R.ok(sysDictDataService.updateById(sysDictData));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysDictDataService.removeByIds(ids));
    }

}

