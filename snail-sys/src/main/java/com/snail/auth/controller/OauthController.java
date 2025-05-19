package com.snail.auth.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.snail.sys.api.form.LoginBody;
import com.snail.common.core.utils.MessageUtils;
import com.snail.common.core.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        JSONObject user = getUser();


        return R.ok();
    }

    private  JSONObject getUser() {
        HttpResponse response;
        JSONObject entries;
        try {
            response = HttpRequest.post("").form("", "").execute();
            int status = response.getStatus();
            if (status != 200) {
                throw new RuntimeException("登录失败");
            }
            String body = response.body();
            return JSONUtil.parseObj(body);
        } catch (Exception e) {
            log.error("登录失败信息：{}", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test1")
    public R<Void> test1(String code) {
        return R.ok(MessageUtils.message(code));
    }
}
