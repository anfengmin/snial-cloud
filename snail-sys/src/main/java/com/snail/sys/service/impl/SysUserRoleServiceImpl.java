package com.snail.sys.service.impl;

import com.snail.common.core.constant.UserConstants;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dao.SysUserRoleDao;
import com.snail.sys.dto.SysUserRolePageDTO;
import com.snail.sys.service.SysUserRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;


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
        return this.lambdaQuery()
                .eq(SysUserRole::getUserId, userId)
                .eq(SysUserRole::getRoleId, UserConstants.ADMIN_ID).exists();
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


}
