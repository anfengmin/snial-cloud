package com.snail.common.core.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 全局异常
 *
 * @author Levi.
 * Created time 2025/5/10
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 错误明细，内部调试错误
     */
    private String detailMessage;

    public GlobalException(String message) {
        this.message = message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public GlobalException setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
        return this;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public GlobalException setMessage(String message) {
        this.message = message;
        return this;
    }
}