package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysConfig;
import com.snail.sys.service.SysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 参数配置表(SysConfig)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
@Api(tags = "参数配置表")
@RestController
@RequestMapping("/v1/sysConfig")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;



    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysConfig> queryById(@PathVariable("id") Long id) {
        return R.ok(sysConfigService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysConfig sysConfig) {
        return R.ok(sysConfigService.save(sysConfig));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysConfig sysConfig) {
        return R.ok(sysConfigService.updateById(sysConfig));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysConfigService.removeByIds(ids));
    }

}

