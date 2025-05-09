package com.snail.sys.domain.dto;

import lombok.Data;

/**
 * 注册信息DTO
 */
@Data
public class RegisterDTO {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 确认密码
     */
    private String confirmPassword;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 唯一标识
     */
    private String uuid;
}