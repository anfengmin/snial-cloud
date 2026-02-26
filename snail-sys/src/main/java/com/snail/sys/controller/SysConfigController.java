package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysConfig;
import com.snail.sys.dto.SysConfigPageDTO;
import com.snail.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
@Api(tags = "参数配置表")
@RequiredArgsConstructor
@RequestMapping("/v1/sysConfig")
public class SysConfigController {


    private final SysConfigService sysConfigService;


    /**
     * 获取参数配置列表
     */
    @SaCheckPermission("system:config:list")
    @GetMapping("/queryByPage")
    public R<Page<SysConfig>> queryByPage(@RequestBody SysConfigPageDTO dto) {
        return R.ok(sysConfigService.queryByPage(dto));
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysConfig> queryById(@PathVariable("id") Long id) {
        return R.ok(sysConfigService.getById(id));
    }

    @GetMapping(value = "/configKey/{configKey}")
    public R<Void> getConfigKey(@PathVariable String configKey) {
        return R.ok(sysConfigService.selectConfigByKey(configKey));
    }

    @SaCheckPermission("system:config:add")
    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(@Validated @RequestBody SysConfig sysConfig) {
        return R.ok(sysConfigService.save(sysConfig));
    }

    @SaCheckPermission("system:config:edit")
    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(@Validated @RequestBody SysConfig sysConfig) {
        return R.ok(sysConfigService.updateById(sysConfig));
    }

    @SaCheckPermission("system:config:remove")
    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysConfigService.removeByIds(ids));
    }

}

