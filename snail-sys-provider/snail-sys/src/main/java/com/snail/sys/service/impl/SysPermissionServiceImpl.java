package com.snail.sys.service.impl;

import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.domain.SysUser;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.service.SysPermissionService;
import com.snail.sys.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
@RequiredArgsConstructor
@Service
public class SysPermissionServiceImpl implements SysPermissionService {


    private final SysRoleService sysRoleService;
    private final SysMenuService sysMenuService;


    /**
     * 获取角色数据权限
     *
     * @param user 用户
     * @return 角色权限信息
     */
    @Override
    public Set<String> getRolePermission(SysUser user) {
        Set<String> roles = new HashSet<>();
        // 管理员拥有所有权限
        if (LoginUtils.isAdmin(user.getId())) {
            roles.add("admin");
        } else {
            roles.addAll(sysRoleService.selectRolePermissionByUserId(user.getId()));
        }
        return roles;
    }

    /**
     * 获取菜单数据权限
     *
     * @param user 用户
     * @return 菜单权限信息
     */
    @Override
    public Set<String> getMenuPermission(SysUser user) {
        Set<String> perms = new HashSet<>();
        // 管理员拥有所有权限
        if (LoginUtils.isAdmin(user.getId())) {
            perms.add("*:*:*");
        } else {
            perms.addAll(sysMenuService.selectMenuPermsByUserId(user.getId()));
        }
        return perms;
    }
}
