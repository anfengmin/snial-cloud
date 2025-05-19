package com.snail.common.core.exception;

import cn.hutool.http.HttpStatus;
import lombok.Getter;

/**
 * No explanation is needed - Levi
 *
 * @author Anfm
 * Created time 2025/5/8
 * @since 1.0
 */
@Getter
public class ServiceInvokeException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private final Integer code;

    public ServiceInvokeException(String message) {
        super(message);
        code = HttpStatus.HTTP_BAD_REQUEST;
    }

    public ServiceInvokeException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
