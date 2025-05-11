package com.snail.auth.controller;

import com.snail.auth.service.SysLoginService;
import com.snail.sys.api.form.LoginBody;

import common.core.constant.Constants;
import common.core.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Api(tags = "登录")
@Validated
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final SysLoginService sysLoginService;

    @ApiModelProperty(value = "登录")
    @PostMapping("login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody form) {
        // 用户登录
        String accessToken = sysLoginService.login(form.getUsername(), form.getPassword());

        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        rspMap.put(Constants.ACCESS_TOKEN, accessToken);
        return R.ok(rspMap);
    }
}
