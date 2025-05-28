package com.snail.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.service.SysDictDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典数据表(SysDictData)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
@Api(tags = "字典数据")
@RestController
@RequestMapping("/v1/dict/data")
public class SysDictDataController {

    @Resource
    private SysDictDataService sysDictDataService;



    @ApiOperation(value = "获取字典数据列表")
    @GetMapping("/list")
    public R<Page<SysDictData>> list(SysDictData dictData, Page<SysDictData> pageQuery) {
        Page<SysDictData> page = sysDictDataService.lambdaQuery().page(pageQuery);
        return R.ok(page);
    }

    @ApiOperation(value = "查询字典数据详细")
    @GetMapping(value = "/{dictCode}")
    public R<SysDictData> getInfo(@PathVariable Long dictCode) {
        return R.ok(sysDictDataService.getById(dictCode));
    }

    @ApiOperation(value = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        return R.ok(sysDictDataService.queryDictDataByType(dictType));
    }

    @PostMapping(value = "/add")
    @ApiOperation(value = "新增字典类型")
    public R<Boolean> add(@Validated @RequestBody SysDictData dict) {
        return R.ok(sysDictDataService.save(dict));
    }


    @PutMapping
    @ApiOperation(value = "编辑字典类型")
    public R<Boolean> edit(SysDictData sysDictData) {
        return R.ok(sysDictDataService.updateById(sysDictData));
    }

    @DeleteMapping
    @ApiOperation(value = "删除字典类型")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysDictDataService.removeByIds(ids));
    }

}

