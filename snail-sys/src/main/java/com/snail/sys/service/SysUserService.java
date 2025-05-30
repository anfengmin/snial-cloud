package com.snail.sys.service;


import com.snail.common.satoken.vo.LoginUser;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.dto.SysUserPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:06:59
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysUser>> queryByPage(SysUserPageDTO dto);

    /**
     * getUserInfo
     *
     * @param userCode userCode
     * @return com.snail.common.satoken.vo.LoginUser
     * @since 1.0
     */
    LoginUser getUserInfo(String userCode);

    /**
     * registerUserInfo
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     */
    boolean registerUserInfo(SysUser sysUser);


}
