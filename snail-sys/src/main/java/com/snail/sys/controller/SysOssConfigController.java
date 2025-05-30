package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.service.SysOssConfigService;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:57
 */
@Api(tags = "对象存储配置表")
@RestController
@RequestMapping("/v1/sysOssConfig")
public class SysOssConfigController {

    @Resource
    private SysOssConfigService sysOssConfigService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysOssConfig>> queryByPage(@RequestBody SysOssConfigPageDTO dto) {
        return sysOssConfigService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysOssConfig> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOssConfigService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysOssConfig sysOssConfig) {
        return R.ok(sysOssConfigService.save(sysOssConfig));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysOssConfig sysOssConfig) {
        return R.ok(sysOssConfigService.updateById(sysOssConfig));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysOssConfigService.removeByIds(ids));
    }

}

