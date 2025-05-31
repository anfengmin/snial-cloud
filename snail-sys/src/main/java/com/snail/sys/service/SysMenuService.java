package com.snail.sys.service;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.dto.SysMenuPageDTO;

import java.util.List;

/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:45:00
 */
public interface SysMenuService extends IService<SysMenu> {


    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysMenu>> queryByPage(SysMenuPageDTO dto);

    /**
     * 获取菜单列表
     *
     * @param menu   menu
     * @param userId userId
     * @return java.util.List<com.snail.sys.domain.SysMenu>
     * @since 1.0
     */
    List<SysMenu> queryMenuList(SysMenu menu, Long userId);

    /**
     * 获取菜单下拉树列表
     *
     * @param menus menus
     * @return java.util.List<cn.hutool.core.lang.tree.Tree<java.lang.Long>>
     * @since 1.0
     */
    List<Tree<Long>> queryMenuTree(List<SysMenu> menus);

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId roleId
     * @return java.util.List<java.lang.Long>
     * @since 1.0
     */
    List<Long> queryMenuListByRoleId(Long roleId);
}
