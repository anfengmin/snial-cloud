package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysOss;
import com.snail.sys.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * OSS对象存储(SysOss)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:52:20
 */
@Api(tags = "OSS对象存储")
@RestController
@RequestMapping("/v1/sysOss")
public class SysOssController {

    @Resource
    private SysOssService sysOssService;

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

