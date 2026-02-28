package com.snail.sys.service.impl;

import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.dao.SysUserRoleDao;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysUserRolePageDTO;
import com.snail.sys.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 用户和角色关联表
 *
 * @author makejava
 * @since 2025-05-30 23:14:08
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRole> implements SysUserRoleService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysUserRole>> queryByPage(SysUserRolePageDTO dto) {
        Page<SysUserRole> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUserRole> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }

    /**
     * 校验用户是管理员角色
     *
     * @param userId userId
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkAdminRole(Long userId) {
        return this.lambdaQuery().eq(SysUserRole::getUserId, userId).eq(SysUserRole::getRoleId, UserConstants.ADMIN_ID).exists();
    }

    /**
     * 用户ID获取用户角色信息
     *
     * @param userId userId
     * @return java.util.List<com.snail.sys.domain.SysUserRole>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public List<SysUserRole> queryRoleListByUserId(Long userId) {
        return this.lambdaQuery().eq(SysUserRole::getUserId, userId).list();
    }

    /**
     * 获取角色数量
     *
     * @param roleId roleId
     * @return java.lang.Long
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Long selectCount(Long roleId) {
        return this.lambdaQuery().eq(SysUserRole::getRoleId, roleId).count();
    }

    /**
     * 获取角色用户ID
     *
     * @param roleId roleId
     * @return java.util.List<java.lang.Long>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public List<Long> selectUserIdsByRoleId(Long roleId) {
        return this.lambdaQuery()
                .eq(SysUserRole::getRoleId, roleId)
                .select(SysUserRole::getUserId)
                .list().stream()
                .map(SysUserRole::getUserId)
                .collect(Collectors.toList());
    }

    /**
     * 删除用户角色
     *
     * @param userRole userRole
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean deleteAuthUser(SysUserRole userRole) {
        return this.lambdaUpdate()
                .eq(SysUserRole::getUserId, userRole.getUserId())
                .eq(SysUserRole::getRoleId, userRole.getRoleId())
                .remove();
    }

    /**
     * 批量删除用户角色
     *
     * @param roleId  roleId
     * @param userIds userIds
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean deleteAuthUsers(Long roleId, Long[] userIds) {
        return this.lambdaUpdate()
                .eq(SysUserRole::getRoleId, roleId)
                .in(SysUserRole::getUserId, Arrays.asList(userIds))
                .remove();
    }

    /**
     * 批量新增用户角色信息
     *
     * @param user user
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertUserRole(SysUser user) {
        List<SysUserRole> list = Arrays.stream(user.getRoleIds()).map(roleId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        this.saveBatch(list);
    }

    /**
     * 删除用户角色信息
     *
     * @param userId userId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void deleteUserRole(Long userId) {
        this.lambdaUpdate().eq(SysUserRole::getUserId, userId).remove();
    }


}
