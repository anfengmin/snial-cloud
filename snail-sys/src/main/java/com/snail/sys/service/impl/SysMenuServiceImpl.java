package com.snail.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.utils.R;
import com.snail.sys.dao.SysMenuDao;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysMenuPageDTO;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.service.SysRoleMenuService;
import com.snail.sys.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:45:02
 */
@RequiredArgsConstructor
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {

    private final SysUserRoleService sysUserRoleService;
    private final SysRoleMenuService  sysRoleMenuService;

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
        List<SysUserRole> userRoles = sysUserRoleService.queryRoleListByUserId(userId);
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
     * 获取菜单下拉树列表
     *
     * @param menus menus
     * @return java.util.List<cn.hutool.core.lang.tree.Tree < java.lang.Long>>
     * @since 1.0
     */
    @Override
    public List<Tree<Long>> queryMenuTree(List<SysMenu> menus) {
        if (CollUtil.isEmpty(menus)) {
            return Collections.emptyList();
        }
        return TreeUtil.build(menus, 0L,
                (menu, tree) -> tree.setId(menu.getId())
                        .setParentId(menu.getParentId())
                        .setName(menu.getMenuName())
                        .setWeight(menu.getOrderNum()));
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId roleId
     * @return java.util.List<java.lang.Long>
     * @since 1.0
     */
    @Override
    public List<Long> queryMenuListByRoleId(Long roleId) {
        List<SysRoleMenu> userRoles = sysRoleMenuService.querySysRoleMenuListByRoleId(roleId);
        return userRoles.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    /**
     * 构建菜单列表
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

    /**
     * 新增菜单
     *
     * @param sysMenu sysMenu
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     */
    @Override
    public R<Boolean> addMenu(SysMenu sysMenu) {
        if (this.checkMenuNameExits(sysMenu)) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && Validator.isUrl(sysMenu.getPath()) ) {
            return R.fail("新增菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        }

        return R.ok(this.save(sysMenu));
    }

    /**
     * 校验菜单名称是否存在
     *
     * @param sysMenu sysMenu
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkMenuNameExits(SysMenu sysMenu) {
        return this.lambdaQuery()
                .eq(SysMenu::getMenuName, sysMenu.getMenuName())
                .eq(SysMenu::getParentId, sysMenu.getParentId())
                .ne(ObjectUtil.isNotNull(sysMenu.getId()), SysMenu::getId, sysMenu.getId())
                .exists();
    }

    /**
     * 修改菜单
     *
     * @param sysMenu sysMenu
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public R<Boolean> editMenu(SysMenu sysMenu) {
        if (this.checkMenuNameExits(sysMenu)) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (UserConstants.YES_FRAME.equals(sysMenu.getIsFrame()) && Validator.isUrl(sysMenu.getPath()) ) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，地址必须以http(s)://开头");
        } else if (sysMenu.getParentId().equals(sysMenu.getId())) {
            return R.fail("修改菜单'" + sysMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        return R.ok(this.updateById(sysMenu));
    }

    /**
     * 删除菜单权限信息
     *
     * @param ids 菜单ID
     * @return 结果
     */
    @Override
    public R<Boolean> deleteMenuByIds(List<Long> ids) {
        if (this.hasChildByMenuIds(ids)) {
            return R.warn("存在子菜单，不允许删除");
        } else if (sysRoleMenuService.checkMenuExistRole(ids)) {
            return R.warn("菜单已分配,不允许删除");
        }
        return null;
    }

    /**
     * 判断是否存在子菜单
     *
     * @param ids 菜单ID
     * @return 结果
     */
    public boolean hasChildByMenuIds(List<Long> ids) {
        return this.lambdaQuery().in(SysMenu::getParentId, ids).exists();
    }

}
