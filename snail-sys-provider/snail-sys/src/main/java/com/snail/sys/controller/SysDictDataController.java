package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.service.SysDictDataService;
import com.snail.sys.service.SysDictTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
@Tag(name = "字典数据")
@RestController
@RequiredArgsConstructor
@RequestMapping("/dict/data")
public class SysDictDataController {

    private final SysDictDataService sysDictDataService;
    private final SysDictTypeService sysDictTypeService;


    @SaCheckPermission("system:dict:list")
    @Operation(summary = "获取字典数据列表")
    @GetMapping("/list")
    public R<Page<SysDictData>> list(SysDictData dictData, Page<SysDictData> pageQuery) {
        Page<SysDictData> page = sysDictDataService.lambdaQuery()
                .eq(StrUtil.isNotBlank(dictData.getDictType()), SysDictData::getDictType, dictData.getDictType())
                .like(StrUtil.isNotBlank(dictData.getDictLabel()), SysDictData::getDictLabel, dictData.getDictLabel())
                .eq(ObjectUtil.isNotNull(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus())
                .orderByAsc(SysDictData::getDictSort)
                .page(pageQuery);
        return R.ok(page);
    }

    @SaCheckPermission("system:dict:query")
    @Operation(summary = "查询字典数据详细")
    @GetMapping("/{dictCode}")
    public R<SysDictData> getInfo(@PathVariable Long dictCode) {
        return R.ok(sysDictDataService.getById(dictCode));
    }

    @SaCheckPermission("system:dict:query")
    @Operation(summary = "根据字典类型查询字典数据信息")
    @GetMapping("/type/{dictType}")
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    public R<List<SysDictData>> dictType(@PathVariable String dictType) {
        return R.ok(sysDictDataService.queryDictDataByType(dictType));
    }

    @SaCheckPermission("system:dict:add")
    @PostMapping("/add")
    @Operation(summary = "新增字典数据")
    public R<Boolean> add(@Validated @RequestBody SysDictData dict) {
        boolean saved = sysDictDataService.save(dict);
        if (saved) {
            sysDictTypeService.resetDictCache();
        }
        return R.ok(saved);
    }


    @SaCheckPermission("system:dict:edit")
    @PostMapping("/edit")
    @Operation(summary = "编辑字典数据")
    public R<Boolean> edit(@Validated @RequestBody SysDictData sysDictData) {
        boolean updated = sysDictDataService.updateById(sysDictData);
        if (updated) {
            sysDictTypeService.resetDictCache();
        }
        return R.ok(updated);
    }

    @SaCheckPermission("system:dict:remove")
    @PostMapping("/remove")
    @Operation(summary = "删除字典数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        boolean removed = sysDictDataService.removeByIds(ids);
        if (removed) {
            sysDictTypeService.resetDictCache();
        }
        return R.ok(removed);
    }

}
