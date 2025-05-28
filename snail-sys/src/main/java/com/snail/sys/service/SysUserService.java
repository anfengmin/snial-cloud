package com.snail.sys.service;

import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.SysUser;
import com.snail.common.satoken.vo.LoginUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.dto.SysUserPageDTO;
import org.springframework.data.domain.Page;


/**
 * 用户信息表(User)表服务接口
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
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
     * @return com.snail.sys.api.vo.UserVo
     * @author Anfm(✪ ω ✪)
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    LoginUser getUserInfo(String userCode);

    /**
     * registerUserInfo
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean registerUserInfo(SysUser sysUser);

}
