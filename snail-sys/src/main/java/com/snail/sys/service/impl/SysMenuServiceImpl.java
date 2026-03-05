package com.snail.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.utils.R;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.dao.SysMenuDao;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysMenuPageDTO;
import com.snail.sys.service.SysMenuService;
import com.snail.sys.service.SysRoleMenuService;
import com.snail.sys.service.SysUserRoleService;
import com.snail.sys.vo.MetaVO;
import com.snail.sys.vo.RouterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    private final SysRoleMenuService sysRoleMenuService;

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
     * 校验菜单名称是否存在
     *
     * @param sysMenu sysMenu
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkMenuNameExists(SysMenu sysMenu) {
        return this.lambdaQuery()
                .eq(SysMenu::getMenuName, sysMenu.getMenuName())
                .eq(SysMenu::getParentId, sysMenu.getParentId())
                .ne(ObjectUtil.isNotNull(sysMenu.getId()), SysMenu::getId, sysMenu.getId())
                .exists();
    }

    /**
     * 判断是否存在子菜单
     *
     * @param ids 菜单ID
     * @return 结果
     */
    @Override
    public boolean hasChildByMenuIds(List<Long> ids) {
        return this.lambdaQuery().in(SysMenu::getParentId, ids).exists();
    }

    @Override
    public Set<String> selectMenuPermsByUserId(Long userId) {
        List<SysMenu> menus = getSysMenusByUserId(userId);
        return menus.stream()
                .map(SysMenu::getPerms)
                .filter(StrUtil::isNotBlank)
                .collect(Collectors.toSet());
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    private List<SysMenu> getSysMenusByUserId(Long userId) {
        MPJLambdaWrapper<SysMenu> wrapper = new MPJLambdaWrapper<SysMenu>()
                .distinct()
                .leftJoin(SysRoleMenu.class, SysRoleMenu::getMenuId, SysMenu::getId)
                .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRoleMenu::getRoleId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                .eq(SysMenu::getStatus, UserConstants.MENU_NORMAL)
                .eq(SysRole::getStatus, UserConstants.MENU_NORMAL)
                .eq(SysUserRole::getUserId, userId)
                .in(SysMenu::getMenuType, UserConstants.TYPE_MENU, UserConstants.TYPE_DIR);

        List<SysMenu> menus = baseMapper.selectJoinList(SysMenu.class, wrapper);
        return menus;
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public List<RouterVO> selectMenuTreeByUserId(Long userId) {
        List<SysMenu> menus;
        if (LoginUtils.isAdmin(userId)) {
            menus = this.lambdaQuery()
                    .eq(SysMenu::getStatus, UserConstants.MENU_NORMAL)
                    .in(SysMenu::getMenuType, UserConstants.TYPE_MENU, UserConstants.TYPE_DIR)
                    .orderByAsc(SysMenu::getParentId)
                    .orderByAsc(SysMenu::getOrderNum)
                    .list();
        } else {
            menus = getSysMenusByUserId(userId);
        }

        if (CollUtil.isEmpty(menus)) {
            return Collections.emptyList();
        }

        // 递归构建路由树，从根节点（parentId = 0）开始
        return buildRouters(menus, 0L);
    }

    /**
     * 递归构建前端路由树
     *
     * @param menus    当前用户拥有的菜单集合
     * @param parentId 父菜单ID
     * @return 路由树
     */
    private List<RouterVO> buildRouters(List<SysMenu> menus, Long parentId) {
        return menus.stream()
                .filter(menu -> ObjectUtil.equal(menu.getParentId(), parentId))
                .sorted(Comparator.comparing(SysMenu::getOrderNum))
                .map(menu -> {
                    RouterVO router = new RouterVO();
                    router.setName(menu.getMenuName());
                    router.setPath(menu.getPath());

                    String component = menu.getComponent();
                    // 目录默认使用 Layout 组件
                    if (StrUtil.isBlank(component) && UserConstants.TYPE_DIR.equals(menu.getMenuType())) {
                        component = UserConstants.LAYOUT;
                    }
                    router.setComponent(component);

                    MetaVO meta = new MetaVO();
                    meta.setTitle(menu.getMenuName());
                    meta.setI18nKey(menu.getMenuName());
                    meta.setIcon(menu.getIcon());
                    meta.setKeepAlive(ObjectUtil.equal(menu.getIsCache(), 0));
                    meta.setHideInMenu(ObjectUtil.equal(menu.getVisible(), 1));
                    meta.setConstant(false);
                    // 外链路由，记录链接地址
                    if (ObjectUtil.equal(menu.getIsFrame(), UserConstants.YES_FRAME)) {
                        meta.setLink(menu.getPath());
                    }
                    router.setMeta(meta);

                    // 递归设置子路由
                    List<RouterVO> childRouters = buildRouters(menus, menu.getId());
                    router.setChildren(childRouters);

                    return router;
                })
                .collect(Collectors.toList());
    }

}
