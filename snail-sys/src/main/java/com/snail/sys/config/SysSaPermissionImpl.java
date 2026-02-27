package com.snail.sys.config;

import cn.dev33.satoken.stp.StpInterface;
import cn.hutool.core.collection.CollUtil;
import com.snail.common.satoken.core.service.SaPermissionImpl;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.service.SysRoleService;
import com.snail.sys.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Sa-Token 权限管理实现类 - Sys模块专用
 * <p>
 * 从数据库动态获取用户权限信息
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@Component
@RequiredArgsConstructor
@ConditionalOnMissingBean(SaPermissionImpl.class)
public class SysSaPermissionImpl implements StpInterface {

    private final SysUserRoleService sysUserRoleService;
    private final SysMenuService sysMenuService;
    private final SysRoleService sysRoleService;

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        LoginUser loginUser = LoginUtils.getLoginUser();
        if (loginUser == null) {
            return Collections.emptyList();
        }

        Long userId = loginUser.getId();
        if (userId == null) {
            return Collections.emptyList();
        }

        // 获取用户角色
        List<SysUserRole> userRoles = sysUserRoleService.queryRoleListByUserId(userId);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        // 判断是否包含超级管理员
        boolean isAdmin = userRoles.stream()
                .anyMatch(item -> item.getRoleId().equals(1L));

        if (isAdmin) {
            // 超级管理员拥有所有权限
            List<SysMenu> allMenus = sysMenuService.lambdaQuery().list();
            return allMenus.stream()
                    .filter(m -> cn.hutool.core.util.StrUtil.isNotBlank(m.getPerms()))
                    .map(SysMenu::getPerms)
                    .collect(Collectors.toList());
        } else {
            // 普通用户获取对应角色的菜单权限
            List<SysMenu> menus = sysMenuService.queryMenuList(new SysMenu(), userId);
            return menus.stream()
                    .filter(m -> cn.hutool.core.util.StrUtil.isNotBlank(m.getPerms()))
                    .map(SysMenu::getPerms)
                    .collect(Collectors.toList());
        }
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        LoginUser loginUser = com.snail.common.satoken.utils.LoginUtils.getLoginUser();
        if (loginUser == null) {
            return Collections.emptyList();
        }

        Long userId = loginUser.getId();
        if (userId == null) {
            return Collections.emptyList();
        }

        // 获取用户角色
        List<SysUserRole> userRoles = sysUserRoleService.queryRoleListByUserId(userId);
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream()
                .map(SysUserRole::getRoleId)
                .collect(Collectors.toList());

        // 获取角色信息
        List<SysRole> roles = sysRoleService.lambdaQuery()
                .in(SysRole::getId, roleIds)
                .list();

        return roles.stream()
                .filter(r -> cn.hutool.core.util.StrUtil.isNotBlank(r.getRoleKey()))
                .map(r -> "role:" + r.getRoleKey())
                .collect(Collectors.toList());
    }
}
