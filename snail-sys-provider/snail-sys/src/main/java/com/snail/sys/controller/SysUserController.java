package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.log.annotation.Log;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysUser;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.SysDeptService;
import com.snail.sys.service.SysUserService;
import com.snail.sys.vo.SysUserVo;
import com.snail.sys.vo.UserVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:06:58
 */

@Tag(name = "用户")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sysUser")
public class SysUserController {

    private final SysUserService sysUserService;
    private final SysDeptService sysDeptService;

    @SaCheckPermission("system:user:list")
    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysUser>> queryByPage(@RequestBody SysUserPageDTO dto) {
        return R.ok(sysUserService.queryByPage(dto));
    }


    @GetMapping("getUserInfo")
    @Operation(summary = "获取用户信息")
    public R<SysUserVo> getInfo() {
        LoginUser loginUser = LoginUtils.getLoginUser();
        assert loginUser != null;
        SysUser user = sysUserService.getById(loginUser.getId());
        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        SysUserVo build = SysUserVo.builder()
                .user(userVO)
                .rolePermission(loginUser.getRolePermission())
                .menuPermission(loginUser.getMenuPermission())
                .build();
        return R.ok(build);
    }


    @SaCheckPermission("system:user:query")
    @GetMapping("/info/{userId}")
    @Operation(summary = "用户id获取用户信息")
    public R<SysUserVo> getInfo(@PathVariable(value = "userId", required = false) Long userId) {
        SysUserVo sysUserVo = sysUserService.getInfo(userId);
        return R.ok(sysUserVo);
    }

    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysUser> queryById(@PathVariable("id") Long id) {
        return R.ok(sysUserService.getById(id));
    }

    @SaCheckPermission("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @Operation(summary = "新增用户")
    public R<Boolean> add(@Validated @RequestBody SysUser user) {
        sysDeptService.checkDeptDataScope(user.getDeptId());
        if (sysUserService.checkUserCodeUnique(user)) {
            return R.fail("新增用户'" + user.getUserCode() + "'失败，登录账号已存在");
        } else if (StrUtil.isNotEmpty(user.getPhoneNo())
                && sysUserService.checkPhoneExists(user)) {
            return R.fail("新增用户'" + user.getUserCode() + "'失败，手机号码已存在");
        } else if (StrUtil.isNotEmpty(user.getEmail())
                && sysUserService.checkEmailExists(user)) {
            return R.fail("新增用户'" + user.getUserCode() + "'失败，邮箱账号已存在");
        }
        return R.ok(sysUserService.insertUser(user));
    }

    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("edit")
    @Operation(summary = "编辑用户")
    public R<Boolean> edit(@Validated @RequestBody SysUser user) {
        sysUserService.checkUserAllowed(user);
        sysUserService.checkUserDataScope(user.getId());
        sysDeptService.checkDeptDataScope(user.getDeptId());
        if (sysUserService.checkUserCodeUnique(user)) {
            return R.fail("修改用户'" + user.getUserCode() + "'失败，登录账号已存在");
        } else if (StrUtil.isNotEmpty(user.getPhoneNo())
                && sysUserService.checkPhoneExists(user)) {
            return R.fail("修改用户'" + user.getUserCode() + "'失败，手机号码已存在");
        } else if (StrUtil.isNotEmpty(user.getEmail())
                && sysUserService.checkEmailExists(user)) {
            return R.fail("修改用户'" + user.getUserCode() + "'失败，邮箱账号已存在");
        }
        return R.ok(sysUserService.updateUser(user));
    }

    @SaCheckPermission("system:sysUser:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("resetPwd/{userId}")
    @Operation(summary = "重置密码")
    public R<String> resetPwd(@PathVariable("userId") Long userId) {
        return R.ok(sysUserService.resetUserPassword(userId));
    }

    @SaCheckPermission("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("remove")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysUserService.removeByIds(ids));
    }

}
