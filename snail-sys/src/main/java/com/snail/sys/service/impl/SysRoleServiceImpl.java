package com.snail.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.dao.SysRoleDao;
import com.snail.sys.domain.SysRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.service.SysRoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:12
 */
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysRole> queryByPage(SysRolePageDTO dto) {
        Page<SysRole> page = new Page<>(dto.getCurrent(), dto.getSize());
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dto.getId()), SysRole::getId, dto.getId())
                .like(StrUtil.isNotBlank(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysRole::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getRoleKey()), SysRole::getRoleKey, dto.getRoleKey())
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime()),
                        SysRole::getCreateTime, dto.getBeginTime(), dto.getEndTime())
                .orderByAsc(SysRole::getRoleSort)
                .page(page);
    }

    /**
     * selectRoleById
     *
     * @param roleId roleId
     * @return com.snail.sys.domain.SysRole
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        this.checkRoleDataScope(roleId);
        return this.getById(roleId);
    }

    /**
     * checkRoleDataScope
     *
     * @param roleId roleId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkRoleDataScope(Long roleId) {
        if (!LoginUtils.isAdmin()) {
            Optional.ofNullable(this.lambdaQuery()
                            .eq(SysRole::getId, roleId).list())
                    .orElseThrow(() -> new ServiceException("角色不存在"));
        }
    }


    /**
     * 校验角色是否允许操作
     *
     * @param role role
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkRoleAllowed(SysRole role) {

        if (ObjectUtil.isNotNull(role.getId()) && LoginUtils.isAdmin(role.getId())) {
            throw new ServiceException("不允许操作超级管理员角色");
        }
        // 新增不允许使用 管理员标识符
        if (ObjectUtil.isNull(role.getId())
                && StrUtil.equals(role.getRoleKey(), UserConstants.ADMIN_ROLE_KEY)) {
            throw new ServiceException("不允许使用系统内置管理员角色标识符!");
        }
        // 修改不允许修改 管理员标识符
        if (ObjectUtil.isNotNull(role.getId())) {
            SysRole sysRole = baseMapper.selectById(role.getId());
            // 如果标识符不相等 判断为修改了管理员标识符
            if (!StrUtil.equals(sysRole.getRoleKey(), role.getRoleKey())) {
                if (StrUtil.equals(sysRole.getRoleKey(), UserConstants.ADMIN_ROLE_KEY)) {
                    throw new ServiceException("不允许修改系统内置管理员角色标识符!");
                } else if (StrUtil.equals(role.getRoleKey(), UserConstants.ADMIN_ROLE_KEY)) {
                    throw new ServiceException("不允许使用系统内置管理员角色标识符!");
                }
            }
        }

    }


    /**
     * 校验角色名称是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkRoleNameExists(SysRole role) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId())
                .eq(SysRole::getRoleName, role.getRoleName())
                .exists();
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkRoleKeyExists(SysRole role) {
        return this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId())
                .eq(SysRole::getRoleKey, role.getRoleKey())
                .exists();
    }
}
