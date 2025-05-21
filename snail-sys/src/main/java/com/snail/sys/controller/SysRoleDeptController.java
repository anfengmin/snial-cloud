package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.service.SysRoleDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 角色和部门关联(SysRoleDept)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:53:01
 */
@Api(tags = "角色和部门关联")
@RestController
@RequestMapping("/v1/sysRoleDept")
public class SysRoleDeptController {

    @Resource
    private SysRoleDeptService sysRoleDeptService;


    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysRoleDept> queryById(@PathVariable("id") Long id) {
        return R.ok(sysRoleDeptService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.save(sysRoleDept));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysRoleDept sysRoleDept) {
        return R.ok(sysRoleDeptService.updateById(sysRoleDept));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysRoleDeptService.removeByIds(ids));
    }

}

