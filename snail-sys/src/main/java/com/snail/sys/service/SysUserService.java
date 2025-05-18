package com.snail.sys.service;

import com.snail.sys.api.domain.SysUser;
import com.snail.sys.api.vo.SysUserVo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 用户信息表(User)表服务接口
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
public interface SysUserService extends IService<SysUser> {


    /**
     * getUserInfo
     *
     * @param userCode userCode
     * @return com.snail.sys.api.vo.UserVo
     * @author Anfm(✪ ω ✪)
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    SysUserVo getUserInfo(String userCode);
}
