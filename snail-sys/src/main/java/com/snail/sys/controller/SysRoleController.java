package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysRole;
import com.snail.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色信息(SysRole)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Api(tags = "角色信息")
@RestController
@RequestMapping("/v1/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;


    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysRole> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysRole sysRole) {
        return R.ok(sysRoleService.save(sysRole));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysRole sysRole) {
        return R.ok(sysRoleService.updateById(sysRole));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysRoleService.removeByIds(ids));
    }

}

