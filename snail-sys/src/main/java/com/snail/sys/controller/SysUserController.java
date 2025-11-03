package com.snail.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.satoken.vo.LoginUser;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.domain.vo.SysUserVo;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:06:58
 */

@Api(tags = "用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sysUser")
public class SysUserController {

    private final SysUserService sysUserService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysUser>> queryByPage(@RequestBody SysUserPageDTO dto) {
        return sysUserService.queryByPage(dto);
    }

    @GetMapping("getInfo")
    public R<SysUserVo> getInfo() {
        LoginUser loginUser = LoginUtils.getLoginUser();
        assert loginUser != null;
        SysUser user = sysUserService.getById(loginUser.getId());
        SysUserVo build = SysUserVo.builder()
                .sysUser(user)
                .rolePermission(loginUser.getRolePermission())
                .menuPermission(loginUser.getMenuPermission())
                .build();
        return R.ok(build);
    }
    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysUser> queryById(@PathVariable("id") Long id) {
        return R.ok(sysUserService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysUser sysUser) {
        return R.ok(sysUserService.save(sysUser));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysUser sysUser) {
        return R.ok(sysUserService.updateById(sysUser));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysUserService.removeByIds(ids));
    }

}

