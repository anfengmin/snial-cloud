package com.snail.sys.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.snail.common.core.domain.R;
import com.snail.sys.domain.dto.LoginDTO;
import com.snail.sys.domain.dto.RegisterDTO;
import com.snail.sys.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 认证控制器
 * 提供用户登录、注册等功能
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     *
     * @param loginDTO 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        // 模拟登录，实际项目中需要从数据库验证用户名和密码
        String username = loginDTO.getUsername();
        String password = loginDTO.getPassword();
        
        // 此处应该调用service层验证用户名和密码
        // userService.validateUser(username, password);
        
        // 登录成功，使用Sa-Token记录用户登录状态
        StpUtil.login(username);
        
        // 返回Token信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", StpUtil.getTokenValue());
        result.put("tokenName", "snail-token");
        
        return R.ok(result, "登录成功");
    }

    /**
     * 用户注册
     *
     * @param registerDTO 注册信息
     * @return 结果
     */
    @PostMapping("/register")
    public R<Void> register(@RequestBody RegisterDTO registerDTO) {
        // 实际项目中需要调用service层进行用户注册
        // userService.registerUser(registerDTO);
        
        return R.ok(null, "注册成功");
    }

    /**
     * 退出登录
     *
     * @return 结果
     */
    @PostMapping("/logout")
    public R<Void> logout() {
        StpUtil.logout();
        return R.ok(null, "退出成功");
    }
}