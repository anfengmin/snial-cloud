package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "OSS对象存储")
@RestController
@RequestMapping("/v1/sysOss")
public class SysOssController {

    @Resource
    private SysOssService sysOssService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysOss>> queryByPage(@RequestBody SysOssPageDTO dto) {
        return sysOssService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysOss> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOssService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysOss sysOss) {
        return R.ok(sysOssService.save(sysOss));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysOss sysOss) {
        return R.ok(sysOssService.updateById(sysOss));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysOssService.removeByIds(ids));
    }

}

