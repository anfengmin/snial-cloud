package com.snail.job.controller.support;

import com.snail.common.core.utils.R;
import com.xxl.job.core.biz.model.ReturnT;

/**
 * 统一适配 XXL-JOB 返回对象到 snail-cloud 的 R 响应体。
 */
public final class JobResponseUtils {

    private JobResponseUtils() {
    }

    public static <T> R<T> fromReturnT(ReturnT<T> result) {
        if (result == null) {
            return R.fail("请求失败");
        }
        if (result.getCode() == ReturnT.SUCCESS_CODE) {
            return R.ok(result.getContent(), result.getMsg() == null ? "操作成功" : result.getMsg());
        }
        return R.fail(result.getCode(), result.getMsg());
    }
}
