package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.SysDept;
import com.snail.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门信息
 *
 * @author makejava
 * @since 2025-05-21 21:30:46
 */
@Api(tags = "部门表")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/dept")
public class SysDeptController {

    private final SysDeptService sysDeptService;


    @SaCheckPermission("system:dept:list")
    @PostMapping("/list")
    @ApiOperation(value = "获取部门列表")
    public R<List<SysDept>> list(@RequestBody SysDept dept) {
        List<SysDept> depts = sysDeptService.queryDeptList(dept);
        return R.ok(depts);
    }

    @SaCheckPermission("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    @ApiOperation(value = "查询部门列表（排除节点）")
    public R<List<SysDept>> excludeChild(@PathVariable(value = "deptId", required = false) Long deptId) {
        List<SysDept> depts = sysDeptService.queryDeptList(new SysDept());

        depts.removeIf(d -> d.getId().intValue() == deptId
                || CollUtil.contains(Convert.toList(String.class, StrUtil.split(d.getAncestors(), StrUtil.COMMA)), Convert.toStr(deptId)));

        return R.ok(depts);
    }


    @SaCheckPermission("system:dept:query")
    @GetMapping("/{deptId}")
    @ApiOperation(value = "根据部门编号获取详细信息")
    public R<SysDept> getInfo(@PathVariable Long deptId) {
        return R.ok(sysDeptService.getById(deptId));
    }

    @SaCheckPermission("system:dept:add")
    @PostMapping("/add")
    @ApiOperation(value = "新增部门")
    public R<Boolean> add(@Validated @RequestBody SysDept dept) {
        if (sysDeptService.checkDeptNameExists(dept)) {
            return R.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        return R.isOk(sysDeptService.save(dept));
    }

    @SaCheckPermission("system:dept:edit")
    @PostMapping("/edit")
    @ApiOperation(value = "修改部门")
    public R<Boolean> edit(@Validated @RequestBody SysDept dept) {
        Long deptId = dept.getId();
        sysDeptService.checkDeptDataScope(deptId);
        if (sysDeptService.checkDeptNameExists(dept)) {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(deptId)) {
            return R.fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");

        } else if (ObjectUtil.equals(UserConstants.DEPT_DISABLE, dept.getStatus())) {
            if (sysDeptService.selectNormalChildrenDeptById(deptId) > 0) {
                return R.fail("该部门包含未停用的子部门!");
            } else if (sysDeptService.checkDeptExistUser(deptId)) {
                return R.fail("该部门下存在已分配用户，不能禁用!");
            }
        }
        return sysDeptService.updateDept(dept);
    }

    @SaCheckPermission("system:dept:remove")
    @DeleteMapping("/{deptId}")
    @ApiOperation(value = "删除部门")
    public R<Boolean> remove(@PathVariable Long deptId) {
        if (sysDeptService.hasChildByDeptId(deptId)) {
            return R.warn("存在下级部门,不允许删除");
        }
        if (sysDeptService.checkDeptExistUser(deptId)) {
            return R.warn("部门存在用户,不允许删除");
        }
        return R.isOk(sysDeptService.removeById(deptId));
    }

}

