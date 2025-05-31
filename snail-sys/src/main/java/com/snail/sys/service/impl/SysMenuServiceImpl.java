package com.snail.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.AssertUtil;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.dao.SysMenuDao;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysMenuPageDTO;
import com.snail.sys.service.SysMenuService;
import com.snail.common.core.utils.R;
import com.snail.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:45:02
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {


    @Resource
    private SysUserRoleService sysUserRoleService;

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysMenu>> queryByPage(SysMenuPageDTO dto) {
        Page<SysMenu> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysMenu> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }

    /**
     * 获取菜单列表
     *
     * @param menu   menu
     * @param userId userId
     * @return java.util.List<com.snail.sys.domain.SysMenu>
     * @since 1.0
     */
    @Override
    public List<SysMenu> queryMenuList(SysMenu menu, Long userId) {
        // 获取用户角色
        List<SysUserRole> userRoles = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, userId).list();
        if (CollUtil.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        // 判断是否包含超级管理员角色
        boolean isAdmin = userRoles.stream().anyMatch(item -> item.getRoleId().equals(1L));
        if (isAdmin) {
            return menuList(menu, Collections.emptyList());
        } else {
            List<Long> roleIds = userRoles.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());
            return menuList(menu, roleIds);
        }
    }

    /**
     * menuList
     *
     * @param menu    menu
     * @param roleIds roleIds
     * @return java.util.List<com.snail.sys.domain.SysMenu>
     * @since 1.0
     */
    private List<SysMenu> menuList(SysMenu menu, List<Long> roleIds) {
        return this.lambdaQuery()
                .in(CollUtil.isNotEmpty(roleIds), SysMenu::getId, roleIds)
                .eq(StrUtil.isNotEmpty(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                .eq(ObjectUtil.isNotEmpty(menu.getVisible()), SysMenu::getVisible, menu.getVisible())
                .eq(ObjectUtil.isNotEmpty(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNum)
                .list();
    }
}
