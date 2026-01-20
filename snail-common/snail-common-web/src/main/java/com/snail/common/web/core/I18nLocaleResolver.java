package com.snail.common.web.core;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.LocaleResolver;

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
    public @NonNull Locale resolveLocale(@NonNull HttpServletRequest httpServletRequest) {
        // header 名不区分大小写，但部分容器取值时可能敏感，双取一次兜底
        String language = httpServletRequest.getHeader("Accept-Language");
        if (language == null || language.isEmpty()) {
            language = httpServletRequest.getHeader("accept-language");
        }

        Locale locale = parseLocale(language);
        // 写入线程上下文，保证 MessageUtils/校验等使用同一个 Locale
        LocaleContextHolder.setLocale(locale);
        return locale;
    }

    @Override
    public void setLocale(@NonNull HttpServletRequest httpServletRequest,
                          @Nullable HttpServletResponse httpServletResponse,
                          @Nullable Locale locale) {
        if (locale == null) {
            locale = Locale.getDefault();
        }
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * 解析 Accept-Language
     * 支持：en_US / en-US / en / en,en-US;q=0.9
     */
    private Locale parseLocale(String language) {
        if (language == null || language.trim().isEmpty()) {
            return Locale.getDefault();
        }
        try {
            // 取第一个 language-range
            String first = language.split(",")[0].trim();
            // 去掉 q 参数
            int qIndex = first.indexOf(';');
            if (qIndex > -1) {
                first = first.substring(0, qIndex).trim();
            }
            // 分隔符兼容 '-' 和 '_'
            String normalized = first.replace('-', '_');
            String[] parts = normalized.split("_");
            if (parts.length >= 2) {
                return new Locale(parts[0], parts[1]);
            }
            return new Locale(parts[0]);
        } catch (Exception ignore) {
            return Locale.getDefault();
        }
    }
}
