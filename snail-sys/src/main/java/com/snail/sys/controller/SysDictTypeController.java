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
@RequestMapping("/v1/dict/type")
public class SysDictTypeController {

    @Resource
    private SysDictTypeService sysDictTypeService;


    @GetMapping("/{dictId}")
    @ApiOperation(value = "查询字典类型详细")
    public R<SysDictType> queryById(@PathVariable("dictId") Long id) {
        return R.ok(sysDictTypeService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增字典类型")
    public R<Boolean> add(SysDictType sysDictType) {
        if (sysDictTypeService.checkDictTypeUnique(sysDictType)) {
            return R.fail("新增字典'" + sysDictType.getDictName() + "'失败，字典类型已存在");
        }
        return R.ok(sysDictTypeService.save(sysDictType));
    }

    @PutMapping
    @ApiOperation(value = "编辑字典类型")
    public R<Boolean> edit(SysDictType sysDictType) {
        if (sysDictTypeService.checkDictTypeUnique(sysDictType)) {
            return R.fail("修改字典'" + sysDictType.getDictName() + "'失败，字典类型已存在");
        }
        return R.ok(sysDictTypeService.updateById(sysDictType));
    }

    @DeleteMapping("/{ids}")
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@PathVariable("ids") List<Long> ids) {
        return R.ok(sysDictTypeService.removeByIds(ids));
    }

    @DeleteMapping("/refreshCache")
    public R<Void> refreshCache() {
        sysDictTypeService.resetDictCache();
        return R.ok();
    }

}

