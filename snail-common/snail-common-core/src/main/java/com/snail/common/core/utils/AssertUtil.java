package com.snail.common.core.utils;


import com.snail.common.core.exception.ServiceInvokeException;

/**
 * No explanation is needed - Levi
 *
 * @author Anfm
 * Created time 2025/5/8
 * @since 1.0
 */
public class AssertUtil {

    /**
     * 服务调用异常 为true时抛异常
     * @param expression
     * @param message
     */
    public static void checkService(boolean expression, String message) {
        if (expression) {
            throw new ServiceInvokeException(message);
        }
    }
}
