package com.snail.sys.service;

import com.snail.sys.domain.dto.LoginDTO;
import com.snail.sys.domain.dto.RegisterDTO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 验证用户登录信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否验证通过
     */
    boolean validateUser(String username, String password);
    
    /**
     * 注册用户
     *
     * @param registerDTO 注册信息
     * @return 是否注册成功
     */
    boolean registerUser(RegisterDTO registerDTO);
    
    /**
     * 根据用户名获取用户ID
     *
     * @param username 用户名
     * @return 用户ID
     */
    Long getUserIdByUsername(String username);
}