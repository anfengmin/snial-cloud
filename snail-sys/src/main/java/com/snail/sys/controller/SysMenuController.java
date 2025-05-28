package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单信息
 *
 * @author makejava
 * @since 2025-05-21 21:36:54
 */
@Api(tags = "菜单权限表")
@RestController
@RequestMapping("/v1/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;



    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysMenu> queryById(@PathVariable("id") Long id) {
        return R.ok(sysMenuService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysMenu sysMenu) {
        return R.ok(sysMenuService.save(sysMenu));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysMenu sysMenu) {
        return R.ok(sysMenuService.updateById(sysMenu));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysMenuService.removeByIds(ids));
    }

}

