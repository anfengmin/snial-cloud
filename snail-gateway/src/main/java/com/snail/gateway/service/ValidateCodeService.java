package com.snail.gateway.service;


import common.core.exception.CaptchaException;
import common.core.utils.R;

import java.io.IOException;
import java.util.Map;

/**
 * 验证码处理
 *
 * @author ruoyi
 */
public interface ValidateCodeService {

    /**
     * 生成验证码
     *
     * @return common.core.utils.R<java.util.Map < java.lang.String, java.lang.Object>>
     * @since 1.0
     */
    R<Map<String, Object>> createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     *
     * @param key   key
     * @param value value
     * @since 1.0
     */
    void checkCaptcha(String key, String value) throws CaptchaException;
}
