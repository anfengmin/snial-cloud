package com.snail.common.core.service;

/**
 * 脱敏服务 默认管理员不过滤 需自行根据业务重写实现
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
public interface SensitiveService {

    /**
     * 是否脱敏
     *
     * @return boolean
     * @since 1.0
     */
    boolean isSensitive();
}
