package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.excel.model.ExcelImportResult;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysOss;
import com.snail.sys.domain.SysUser;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.dto.UserProfileUpdateDTO;
import com.snail.sys.service.SysDeptService;
import com.snail.sys.service.SysOssService;
import com.snail.sys.service.SysUserService;
import com.snail.sys.vo.SysUserVo;
import com.snail.sys.vo.UserProfileVo;
import com.snail.sys.vo.UserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    private final SysOssService sysOssService;

    @SaCheckPermission("system:user:list")
    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysUser>> queryByPage(@RequestBody SysUserPageDTO dto) {
        return R.ok(sysUserService.queryByPage(dto));
    }

    @SaCheckPermission("system:user:list")
    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("export")
    @Operation(summary = "导出用户")
    public void export(@RequestBody SysUserPageDTO dto, HttpServletResponse response) {
        sysUserService.exportUsers(dto, response);
    }

    @SaCheckPermission("system:user:add")
    @GetMapping("importTemplate")
    @Operation(summary = "下载用户导入模板")
    public void importTemplate(HttpServletResponse response) {
        sysUserService.downloadImportTemplate(response);
    }

    @SaCheckPermission("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @PostMapping(value = "importData", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "导入用户")
    public R<ExcelImportResult> importData(@RequestParam("file") MultipartFile file,
                                           @RequestParam(value = "updateSupport", defaultValue = "false") boolean updateSupport) {
        return R.ok(sysUserService.importUsers(file, updateSupport));
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

    @SaCheckLogin
    @GetMapping("profile")
    @Operation(summary = "获取当前登录用户个人资料")
    public R<UserProfileVo> getProfile() {
        Long userId = LoginUtils.getUserId();
        return R.ok(sysUserService.getProfile(userId));
    }

    @SaCheckLogin
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("profile")
    @Operation(summary = "更新当前登录用户个人资料")
    public R<Boolean> updateProfile(@Validated @RequestBody UserProfileUpdateDTO dto) {
        Long userId = LoginUtils.getUserId();
        return R.ok(sysUserService.updateProfile(userId, dto));
    }

    @SaCheckLogin
    @Log(title = "个人头像", businessType = BusinessType.IMPORT)
    @PostMapping(value = "profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传当前登录用户头像")
    public R<SysOss> uploadProfileAvatar(@RequestParam("file") MultipartFile file) {
        SysOss result = sysOssService.upload(file, null);
        if (ObjectUtil.isNull(result) || StrUtil.isBlank(result.getUrl())) {
            return R.fail("头像上传失败");
        }
        return R.ok(result);
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

    @SaCheckPermission("system:user:resetPwd")
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
