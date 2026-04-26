package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.sys.service.SysOssConfigService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:57
 */
@Tag(name = "对象存储配置表")
@RestController
@RequestMapping("/sysOssConfig")
public class SysOssConfigController {

    @Resource
    private SysOssConfigService sysOssConfigService;

    @SaCheckPermission("system:oss:list")
    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysOssConfig>> queryByPage(@RequestBody SysOssConfigPageDTO dto) {
        return sysOssConfigService.queryByPage(dto);
    }

    @SaCheckPermission("system:oss:query")
    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysOssConfig> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOssConfigService.getById(id));
    }

    @SaCheckPermission("system:oss:add")
    @Log(title = "对象存储配置", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(@RequestBody SysOssConfig sysOssConfig) {
        return R.ok(sysOssConfigService.save(sysOssConfig));
    }

    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储配置", businessType = BusinessType.UPDATE)
    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(@RequestBody SysOssConfig sysOssConfig) {
        return R.ok(sysOssConfigService.updateById(sysOssConfig));
    }

    @SaCheckPermission("system:oss:remove")
    @Log(title = "对象存储配置", businessType = BusinessType.DELETE)
    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysOssConfigService.removeByIds(ids));
    }

}
