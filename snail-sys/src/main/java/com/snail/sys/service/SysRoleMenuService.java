package com.snail.sys.service;

import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.dto.SysRoleMenuPageDTO;
import com.snail.sys.api.domain.SysRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 角色和菜单关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:44
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysRoleMenu>> queryByPage(SysRoleMenuPageDTO dto);

    /**
     * 角色id查询菜单列表
     *
     * @param roleId roleId
     * @return java.util.List<com.snail.sys.domain.SysRoleMenu>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    List<SysRoleMenu> querySysRoleMenuListByRoleId(Long roleId);

    /**
     * 判断菜单是否被角色使用
     *
     * @param ids ids
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkMenuExistRole(List<Long> ids);

    /**
     * 根据角色id删除角色菜单
     *
     * @param id id
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void deleteRoleMenuByRoleId(Long id);

    /**
     * 添加角色菜单
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean insertRoleMenu(SysRole role);
}
