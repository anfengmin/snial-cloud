package com.snail.sys.service;

import com.snail.sys.domain.SysUser;

import java.util.Set;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
public interface SysPermissionService {

    /**
     * 获取角色数据权限
     *
     * @param user 用户
     * @return 角色权限信息
     */
    Set<String> getRolePermission(SysUser user);

    /**
     * 获取菜单数据权限
     *
     * @param user 用户
     * @return 菜单权限信息
     */
    Set<String> getMenuPermission(SysUser user);
}
