package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysConfig;
import com.snail.sys.domain.SysDictType;
import com.snail.sys.dto.SysConfigPageDTO;
import com.snail.sys.dto.SysDictTypePageDTO;
import com.snail.sys.service.SysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
@Api(tags = "字典类型")
@RestController
@RequestMapping("/v1/dict/type")
public class SysDictTypeController {

    @Resource
    private SysDictTypeService sysDictTypeService;


    @SaCheckPermission("system:dict:list")
    @GetMapping("/queryByPage")
    @ApiOperation(value = "查询字典类型详细")
    public R<Page<SysDictType>> queryByPage(@RequestBody SysDictTypePageDTO dto) {
        return R.ok(sysDictTypeService.queryByPage(dto));
    }

    @SaCheckPermission("system:dict:query")
    @GetMapping("/{dictId}")
    @ApiOperation(value = "查询字典类型详细")
    public R<SysDictType> queryById(@PathVariable("dictId") Long id) {
        return R.ok(sysDictTypeService.getById(id));
    }

    @SaCheckPermission("system:dict:add")
    @PostMapping
    @ApiOperation(value = "新增字典类型")
    public R<Boolean> add(@RequestBody SysDictType sysDictType) {
        if (sysDictTypeService.checkDictTypeUnique(sysDictType)) {
            return R.fail("新增字典'" + sysDictType.getDictName() + "'失败，字典类型已存在");
        }
        return R.ok(sysDictTypeService.save(sysDictType));
    }

    @SaCheckPermission("system:dict:edit")
    @PutMapping
    @ApiOperation(value = "编辑字典类型")
    public R<Boolean> edit(@RequestBody SysDictType sysDictType) {
        if (sysDictTypeService.checkDictTypeUnique(sysDictType)) {
            return R.fail("修改字典'" + sysDictType.getDictName() + "'失败，字典类型已存在");
        }
        return R.ok(sysDictTypeService.updateById(sysDictType));
    }

    @SaCheckPermission("system:dict:remove")
    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysDictTypeService.removeByIds(ids));
    }

    @SaCheckPermission("system:dict:remove")
    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysDictTypeService.resetDictCache();
        return R.ok();
    }

}

