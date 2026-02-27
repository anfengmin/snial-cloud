package com.snail.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.domain.SysRoleMenu;
import com.snail.sys.dao.SysRoleMenuDao;
import com.snail.sys.dto.SysRoleMenuPageDTO;
import com.snail.sys.service.SysRoleMenuService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色和菜单关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:44
 */
@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenu> implements SysRoleMenuService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysRoleMenu>> queryByPage(SysRoleMenuPageDTO dto) {
        Page<SysRoleMenu> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysRoleMenu> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }

    /**
     * 角色id查询菜单列表
     *
     * @param roleId roleId
     * @return java.util.List<com.snail.sys.domain.SysRoleMenu>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public List<SysRoleMenu> querySysRoleMenuListByRoleId(Long roleId) {
        return this.lambdaQuery().eq(SysRoleMenu::getRoleId, roleId).list();
    }

    /**
     * 判断菜单是否被角色使用
     *
     * @param ids ids
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkMenuExistRole(List<Long> ids) {
        return this.lambdaQuery().in(SysRoleMenu::getMenuId, ids).exists();
    }

    /**
     * 删除角色菜单
     *
     * @param id id
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void deleteRoleMenuByRoleId(Long id) {
        this.lambdaUpdate().eq(SysRoleMenu::getRoleId, id).remove();
    }

    /**
     * 批量插入角色菜单
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertRoleMenu(SysRole role) {
        // 新增用户与角色管理
        List<SysRoleMenu> list = Arrays.stream(role.getMenuIds())
                .map(menuId -> {
                    SysRoleMenu rm = new SysRoleMenu();
                    rm.setRoleId(role.getId());
                    rm.setMenuId(menuId);
                    return rm;
                })
                .collect(Collectors.toList());
        if (CollUtil.isNotEmpty(list)) {
            return this.saveBatch(list);
        }
        return false;
    }
}
