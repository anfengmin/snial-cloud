package com.snail.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:10
 */
@Api(tags = "角色信息")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/sysRole")
public class SysRoleController {

    private final SysRoleService sysRoleService;

    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询")
    public R<Page<SysRole>> queryByPage(@RequestBody SysRolePageDTO dto) {
        return sysRoleService.queryByPage(dto);
    }


    @GetMapping(value = "/{roleId}")
    @ApiOperation(value = "根据角色编号获取详细信息")
    public R<SysRole> getInfo(@PathVariable Long roleId) {
        return R.ok(sysRoleService.selectRoleById(roleId));
    }

}

