package com.snail.sys.controller;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysDept;
import com.snail.sys.service.SysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/v1/sysDept")
public class SysDeptController {

    @Resource
    private SysDeptService sysDeptService;


    @GetMapping("/list")
    @ApiOperation(value = "获取部门列表")
    public R<List<SysDept>> list(SysDept dept) {
        List<SysDept> depts = sysDeptService.queryDeptList(dept);
        return R.ok(depts);
    }


}

