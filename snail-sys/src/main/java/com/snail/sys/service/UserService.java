package com.snail.sys.service;

import com.snail.sys.api.vo.UserVo;
import com.snail.sys.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 用户信息表(User)表服务接口
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
public interface UserService extends IService<User> {


    /**
     * getUserInfo
     *
     * @param userCode userCode
     * @return com.snail.sys.api.vo.UserVo
     * @author Anfm(✪ ω ✪)
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    UserVo getUserInfo(String userCode);
}
