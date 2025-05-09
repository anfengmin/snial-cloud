package com.snail.sys.service.impl;

import com.snail.sys.domain.dto.RegisterDTO;
import com.snail.sys.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /**
     * 验证用户登录信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 是否验证通过
     */
    @Override
    public boolean validateUser(String username, String password) {
        // TODO: 实际项目中需要从数据库查询用户信息并验证密码
        // 此处仅为演示，默认返回true
        log.info("验证用户: {}", username);
        return true;
    }

    /**
     * 注册用户
     *
     * @param registerDTO 注册信息
     * @return 是否注册成功
     */
    @Override
    public boolean registerUser(RegisterDTO registerDTO) {
        // TODO: 实际项目中需要将用户信息保存到数据库
        // 此处仅为演示，默认返回true
        log.info("注册用户: {}", registerDTO.getUsername());
        return true;
    }

    /**
     * 根据用户名获取用户ID
     *
     * @param username 用户名
     * @return 用户ID
     */
    @Override
    public Long getUserIdByUsername(String username) {
        // TODO: 实际项目中需要从数据库查询用户ID
        // 此处仅为演示，默认返回1L
        return 1L;
    }
}