package com.snail.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.utils.R;
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
    public R<Page<SysRole>> queryByPage(SysRolePageDTO dto) {
        Page<SysRole> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysRole> result = this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dto.getId()), SysRole::getId, dto.getId())
                .like(StrUtil.isNotBlank(dto.getRoleName()), SysRole::getRoleName, dto.getRoleName())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysRole::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getRoleKey()), SysRole::getRoleKey, dto.getRoleKey())
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime()),
                        SysRole::getCreateTime, dto.getBeginTime(), dto.getEndTime())
                .orderByAsc(SysRole::getRoleSort)
                .page(page);
        return R.ok(result);
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
        return null;
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
}
