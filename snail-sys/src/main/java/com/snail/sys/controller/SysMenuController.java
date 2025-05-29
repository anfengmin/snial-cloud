package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.dto.SysMenuPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:44:59
 */
@Api(tags = "菜单权限")
@RestController
@RequestMapping("/v1/sysMenu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysMenu>> queryByPage(@RequestBody SysMenuPageDTO dto) {
        return sysMenuService.queryByPage(dto);
    }

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

