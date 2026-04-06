package com.snail.sys.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.api.domain.SysDept;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.api.vo.OptionVO;
import com.snail.sys.dao.SysRoleDao;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysRolePageDTO;
import com.snail.sys.service.SysRoleDeptService;
import com.snail.sys.service.SysRoleMenuService;
import com.snail.sys.service.SysRoleService;
import com.snail.sys.service.SysUserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:12
 */
@RequiredArgsConstructor
@Service("sysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRole> implements SysRoleService {

    private final SysRoleMenuService sysRoleMenuService;
    private final SysUserRoleService sysUserRoleService;
    private final SysRoleDeptService sysRoleDeptService;

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
     * @return com.snail.sys.api.domain.SysRole
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public SysRole selectRoleById(Long roleId) {
        this.checkRoleDataScope(roleId);
        SysRole role = this.getById(roleId);
        if (ObjectUtil.isNotNull(role)) {
            List<Long> menuIds = sysRoleMenuService.querySysRoleMenuListByRoleId(roleId)
                    .stream()
                    .map(item -> item.getMenuId())
                    .collect(Collectors.toList());
            role.setMenuIds(menuIds.toArray(new Long[0]));
        }
        return role;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertRole(SysRole role) {
        boolean saved = this.save(role);
        if (!saved) {
            return false;
        }
        return sysRoleMenuService.insertRoleMenu(role);
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
                .ne(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId())
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
                .ne(ObjectUtil.isNotNull(role.getId()), SysRole::getId, role.getId())
                .eq(SysRole::getRoleKey, role.getRoleKey())
                .exists();
    }

    /**
     * 更新角色信息
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateRole(SysRole role) {
        // 修改角色信息
        boolean updated = this.updateById(role);
        // 删除角色与菜单关联
        sysRoleMenuService.deleteRoleMenuByRoleId(role.getId());
        // 新增角色菜单信息
        return updated && sysRoleMenuService.insertRoleMenu(role);
    }


    /**
     * 清除角色在线用户缓存
     *
     * @param roleId roleId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void cleanOnlineUserByRole(Long roleId) {
        // 如果角色未绑定用户 直接返回
        Long num = sysUserRoleService.selectCount(roleId);
        if (num == 0) {
            return;
        }
        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        if (CollUtil.isEmpty(keys)) {
            return;
        }

        // 角色关联的在线用户量过大会导致redis阻塞卡顿 谨慎操作
        keys.parallelStream().forEach(key -> {
            String token = StrUtil.subAfter(key, StrUtil.COLON, true);
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1) {
                return;
            }
            LoginUser loginUser = LoginUtils.getLoginUser(token);
            if (loginUser.getRoles().stream().anyMatch(r -> r.getRoleId().equals(roleId))) {
                try {
                    StpUtil.logoutByTokenValue(token);
                } catch (NotLoginException ignored) {
                }
            }
        });
    }

    /**
     * 修改角色数据权限
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean authDataScope(SysRole role) {
        // 修改角色信息
        this.updateById(role);
        // 删除角色与部门关联
        sysRoleDeptService.deleteRoleDeptByRoleId(role.getId());
        // 新增角色和部门信息（数据权限）
        return sysRoleDeptService.insertRoleDept(role);
    }

    /**
     * 修改角色状态
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean updateRoleStatus(SysRole role) {
        if (UserConstants.ROLE_DISABLE.equals(role.getStatus()) && sysUserRoleService.selectCount(role.getId()) > 0) {
            throw new ServiceException("角色已分配，不能禁用!");
        }
        return this.updateById(role);
    }

    /**
     * 查询所有角色
     *
     * @return List<OptionVO>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public List<OptionVO> selectRoleAll() {
        List<SysRole> list = this.lambdaQuery()
                .select(SysRole::getId, SysRole::getRoleName).orderByAsc(SysRole::getRoleSort)
                .list();
        return list.stream()
                .map(role -> new OptionVO(role.getId(), role.getRoleName()))
                .collect(Collectors.toList());
    }

    /**
     * 批量取消授权用户
     *
     * @param userRole userRole
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean deleteAuthUser(SysUserRole userRole) {
        boolean flag = sysUserRoleService.deleteAuthUser(userRole);
        if (flag) {
            cleanOnlineUserByRole(userRole.getRoleId());
        }
        return flag;
    }

    /**
     * 批量取消授权用户
     *
     * @param roleId   roleId
     * @param userIds userIds
     * @return java.lang.boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean deleteAuthUsers(Long roleId, Long[] userIds) {
        boolean flag = sysUserRoleService.deleteAuthUsers(roleId, userIds);
        if (flag) {
            cleanOnlineUserByRole(roleId);
        }
        return flag;
    }

    /**
     * 批量选择授权用户
     *
     * @param roleId   roleId
     * @param userIds userIds
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void insertAuthUsers(Long roleId, Long[] userIds) {
        List<SysUserRole> list = Arrays.stream(userIds).map(userId -> {
            SysUserRole userRole = new SysUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            return userRole;
        }).collect(Collectors.toList());
        sysUserRoleService.saveBatch(list);
        Optional.of(list)
                .filter(CollUtil::isNotEmpty)
                .ifPresent(l -> cleanOnlineUserByRole(roleId));

    }

    @Override
    public Set<String> selectRolePermissionByUserId(Long userId) {
        MPJLambdaWrapper<SysRole> wrapper = new MPJLambdaWrapper<SysRole>()
                .distinct()
                .select(SysRole::getRoleKey)
                .leftJoin(SysUserRole.class, SysUserRole::getRoleId, SysRole::getId)
                .leftJoin(SysUser.class, SysUser::getId, SysUserRole::getUserId)
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .eq(SysUser::getId, userId)
                .ne(SysRole::getStatus, UserConstants.ROLE_DISABLE);

        List<SysRole> roleList = baseMapper.selectJoinList(SysRole.class, wrapper);
        return roleList.stream()
                .map(SysRole::getRoleKey)
                .collect(Collectors.toSet());
    }

}
