package com.snail.auth.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.snail.sys.api.form.LoginBody;
import com.snial.common.core.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/5/16
 * @since 1.0
 */
@Slf4j
@Api(tags = "登录")
@Validated
@RequestMapping("oauth/v1")
@RestController
public class OauthController {


    @ApiModelProperty(value = "登录")
    @PostMapping("token")
    public R<Map<String, Object>> token(@ModelAttribute LoginBody form) {
        log.info("登录信息：{}", form);
        HttpResponse response = HttpRequest.post("").form("", "").execute();
        int status = response.getStatus();
        if (status != 200) {
            return R.fail("登录失败");
        }
        String body = response.body();
        JSONObject entries = JSONUtil.parseObj(body);
        log.info("登录信息：{}", entries);
        // body转换成json

        return R.ok();
    }
}
