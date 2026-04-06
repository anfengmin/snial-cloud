package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.vo.OptionVO;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysRolePageDTO;

import java.util.List;
import java.util.Set;

/**
 * 角色信息
 *
 * @author makejava
 * @since 2025-05-30 23:06:11
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    Page<SysRole> queryByPage(SysRolePageDTO dto);

    /**
     * selectRoleById
     *
     * @param roleId roleId
     * @return com.snail.sys.api.domain.SysRole
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    SysRole selectRoleById(Long roleId);

    /**
     * 新增角色并保存菜单关联
     *
     * @param role role
     * @return boolean
     */
    boolean insertRole(SysRole role);

    /**
     * checkRoleDataScope
     *
     * @param roleId roleId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkRoleDataScope(Long roleId);

    /**
     * 校验角色是否允许操作
     *
     * @param role role
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 校验角色名称是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkRoleNameExists(SysRole role);

    /**
     * 校验角色权限是否唯一
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkRoleKeyExists(SysRole role);

    /**
     * 修改角色信息
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean updateRole(SysRole role);

    /**
     * 清除角色在线用户缓存
     *
     * @param roleId roleId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void cleanOnlineUserByRole(Long roleId);

    /**
     * 授权数据权限
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean authDataScope(SysRole role);

    /**
     * 修改角色状态
     *
     * @param role role
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean updateRoleStatus(SysRole role);

    /**
     * 获取所有角色
     *
     * @return java.util.List<com.snail.sys.vo.OptionVO>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    List<OptionVO> selectRoleAll();

    /**
     * 取消授权用户
     *
     * @param userRole userRole
     * @return java.lang.boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean deleteAuthUser(SysUserRole userRole);

    /**
     * 批量取消授权用户
     *
     * @param roleId   roleId
     * @param userIds userIds
     * @return java.lang.boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean deleteAuthUsers(Long roleId, Long[] userIds);

    /**
     * 批量授权用户
     *
     * @param roleId   roleId
     * @param userIds userIds
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void insertAuthUsers(Long roleId, Long[] userIds);

    /**
     * 获取角色权限
     *
     * @param id id
     * @return java.util.Set<java.lang.String>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    Set<String> selectRolePermissionByUserId(Long id);
}
