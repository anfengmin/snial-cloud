package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.service.SysRoleMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和菜单关联(SysRoleMenu)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:53:02
 */
@Api(tags = "角色和菜单关联")
@RestController
@RequestMapping("/v1/sysRoleMenu")
public class SysRoleMenuController {

    @Resource
    private SysRoleMenuService sysRoleMenuService;

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysRoleMenu> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleMenuService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysRoleMenu sysRoleMenu) {
        return R.ok(sysRoleMenuService.save(sysRoleMenu));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysRoleMenu sysRoleMenu) {
        return R.ok(sysRoleMenuService.updateById(sysRoleMenu));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysRoleMenuService.removeByIds(ids));
    }

}

