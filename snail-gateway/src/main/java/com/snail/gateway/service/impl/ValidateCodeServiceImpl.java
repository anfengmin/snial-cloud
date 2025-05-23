package com.snail.gateway.service.impl;

import cn.hutool.captcha.AbstractCaptcha;
import cn.hutool.captcha.generator.CodeGenerator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.gateway.config.properties.CaptchaProperties;
import com.snail.gateway.enums.CaptchaType;
import com.snail.gateway.service.ValidateCodeService;
import com.snail.gateway.vo.CaptchaVo;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.constant.Constants;
import com.snail.common.core.exception.CaptchaException;
import com.snail.common.core.exception.user.CaptchaExpireException;
import com.snail.common.core.utils.R;
import com.snail.common.core.utils.SpringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

/**
 * 验证码实现处理
 *
 * @author ruoyi
 */
@Service
public class ValidateCodeServiceImpl implements ValidateCodeService {

    @Resource
    private CaptchaProperties captchaProperties;


    /**
     * 生成验证码
     *
     * @return common.core.utils.R<java.util.Map < java.lang.String, java.lang.Object>>
     * @since 1.0
     */
    @Override
    public R<CaptchaVo> createCaptcha() {
        CaptchaVo captchaVo = new CaptchaVo();
        boolean captchaEnabled = captchaProperties.getEnabled();
        captchaVo.setCaptchaEnabled(captchaEnabled);
        if (!captchaEnabled) {
            return R.ok(captchaVo);
        }

        // 保存验证码信息
        String uuid = IdUtil.simpleUUID();
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        // 生成验证码
        CaptchaType captchaType = captchaProperties.getType();
        boolean isMath = CaptchaType.MATH == captchaType;
        Integer length = isMath ? captchaProperties.getNumberLength() : captchaProperties.getCharLength();

        // 使用 ReflectUtil 替代 ClassUtil
        CodeGenerator codeGenerator = ReflectUtil.newInstance(captchaType.getClazz(), length);

        AbstractCaptcha captcha = SpringUtils.getBean(captchaProperties.getCategory().getClazz());
        captcha.setGenerator(codeGenerator);
        captcha.createCode();
        String code = captcha.getCode();
        if (isMath) {
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(StringUtils.remove(code, "="));
            code = exp.getValue(String.class);
        }
        RedisUtils.setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION));


        captchaVo.setUuid(uuid);
        captchaVo.setImg(captcha.getImageBase64());
        return R.ok(captchaVo);
    }

    /**
     * 校验验证码
     *
     * @param code code
     * @param uuid uuid
     * @since 1.0
     */
    @Override
    public void checkCaptcha(String code, String uuid) throws CaptchaException {
        if (StringUtils.isEmpty(code)) {
            throw new CaptchaException("user.jcaptcha.not.blank");
        }
        if (StringUtils.isEmpty(uuid)) {
            throw new CaptchaExpireException();
        }
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + uuid;
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);

        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
    }
}
