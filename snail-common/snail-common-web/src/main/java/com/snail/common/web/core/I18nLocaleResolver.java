package com.snail.common.web.core;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.LocaleResolver;
import org.wildfly.common.annotation.NotNull;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

/**
 * 获取请求头国际化信息
 *
 * @author Levi.
 * Created time 2025/5/18
 * @since 1.0
 */
public class I18nLocaleResolver implements LocaleResolver {
    
    /**
     * 国际化配置，读取请求头参数 Accept-Language
     * 支持格式：en_US, en-US, en, en,en-US;q=0.9 等
     *
     * @param httpServletRequest httpServletRequest
     * @return java.util.Locale
     * @since 1.0
     */
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        String language = httpServletRequest.getHeader("accept-language");
        Locale locale = Locale.getDefault();

        if (language != null && !language.isEmpty()) {
            String[] split = language.split("_");
            locale = new Locale(split[0], split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }
}
