package com.snail.sys.domain.dto;

import lombok.Data;

/**
 * 登录信息DTO
 */
@Data
public class LoginDTO {
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    /**
     * 验证码
     */
    private String code;
    
    /**
     * 唯一标识
     */
    private String uuid;
}