package com.snail.sys.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDept;
import com.snail.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表(SysDept)表控制层
 *
 * @author makejava
 * @since 2025-05-21 21:30:46
 */
@Api(tags = "部门表")
@RestController
@RequestMapping("/v1/dept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;


    @GetMapping("/list")
    @ApiOperation(value = "获取部门列表")
    public R<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = sysDeptService.queryDeptList(dept);
        return R.ok(depts);
    }

    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation(value = "查询部门列表（排除节点）")
    public R<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = sysDeptService.queryDeptList(new SysDept());

        depts.removeIf(d -> d.getId().intValue() == deptId
                || CollUtil.contains(Convert.toList(String.class, StrUtil.split(d.getAncestors(), StrUtil.COMMA)), Convert.toStr(deptId)));

        return R.ok(depts);
    }


    @GetMapping("/{deptId}")
    @ApiOperation(value = "根据部门编号获取详细信息")
    public R<SysDept> getInfo(@PathVariable Long deptId) {
        return R.ok(sysDeptService.getById(deptId));
    }

    @PostMapping("/add")
    @ApiOperation(value = "新增部门")
    public R<Boolean> add(@Validated @RequestBody SysDept dept) {
        if (sysDeptService.checkDeptNameUnique(dept)) {
            return R.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return R.isOk(sysDeptService.save(dept));
    }

}

