package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysLoginInfo;
import com.snail.sys.service.SysLoginInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问记录(SysLoginInfo)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
@Api(tags = "系统访问记录")
@RestController
@RequestMapping("/v1/loginfo")
public class SysLoginInfoController {

    @Resource
    private SysLoginInfoService sysLoginInfoService;

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysLoginInfo> queryById(@PathVariable("id") Long id) {
        return R.ok(sysLoginInfoService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysLoginInfo sysLoginInfo) {
        return R.ok(sysLoginInfoService.save(sysLoginInfo));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysLoginInfo sysLoginInfo) {
        return R.ok(sysLoginInfoService.updateById(sysLoginInfo));
    }

    @DeleteMapping
    @ApiOperation(value = "删除系统访问记录")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysLoginInfoService.removeByIds(ids));
    }

    @DeleteMapping("/clean")
    public R<Void> clean() {
        sysLoginInfoService.cleanLogInfo();
        return R.ok();
    }

}

