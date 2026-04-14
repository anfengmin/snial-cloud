package com.snail.sys.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.sys.domain.SysOss;
import com.snail.sys.service.SysOssService;
import com.snail.sys.dto.SysOssPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:27
 */
@Tag(name = "OSS对象存储")
@RestController
@RequestMapping("/sysOss")
public class SysOssController {

    @Resource
    private SysOssService sysOssService;

    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysOss>> queryByPage(@RequestBody SysOssPageDTO dto) {
        return sysOssService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysOss> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOssService.getById(id));
    }

    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(SysOss sysOss) {
        return R.ok(sysOssService.save(sysOss));
    }

    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(SysOss sysOss) {
        return R.ok(sysOssService.updateById(sysOss));
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysOssService.removeByIds(ids));
    }

}

