package com.snail.common.utils;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.snail.common.constant.HttpStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

/**
 * 响应信息主体
 *
 * @param <T> 泛型参数
 */
@Data
@NoArgsConstructor
public class R<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息状态码
     */
    @ApiModelProperty(value = "消息状态码", example = "200:成功 500:失败")
    private int code;

    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    @JsonAlias("message")
    private String msg;

    private String extendMsg;

    /**
     * 数据对象
     */
    @ApiModelProperty(value = "数据对象")
    private T data;

    /**
     * <p style="color: #AE00AE"> ok </p>
     *
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> ok() {
        return restResult(null, HttpStatus.SUCCESS, "处理成功");
    }

    /**
     * <p style="color: #FF60AF"> ok </p>
     *
     * @param data data
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> ok(T data) {

        if (data instanceof Boolean && Boolean.FALSE.equals(data)) {
            // 失败
            return fail();
        }
        // 成功
        return restResult(data, HttpStatus.SUCCESS, "处理成功");
    }

    public static <T> R<T> ok(String msg) {
        return restResult(null, HttpStatus.SUCCESS, msg);
    }

    /**
     * <p style="color: #FF60AF"> ok </p>
     *
     * @param msg  msg
     * @param data data
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> ok(T data, String msg) {
        return restResult(data, HttpStatus.SUCCESS, msg);
    }

    /**
     * <p style="color: #0066CC"> fail </p>
     *
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> fail() {
        return restResult(null, HttpStatus.ERROR, "处理失败");
    }

    /**
     * <p style="color: #921AFF"> fail </p>
     *
     * @param msg msg
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> fail(String msg) {
        return restResult(null, HttpStatus.ERROR, msg);
    }

    /**
     * <p style="color: #FF60AF"> fail </p>
     *
     * @param data data
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> fail(T data) {
        return restResult(data, HttpStatus.ERROR, "处理失败");
    }

    /**
     * <p style="color: #AE00AE"> fail </p>
     *
     * @param msg  msg
     * @param data data
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> fail(T data, String msg) {
        return restResult(data, HttpStatus.ERROR, msg);
    }

    /**
     * <p style="color: #921AFF"> fail </p>
     *
     * @param code code
     * @param msg  msg
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> R<T> fail(int code, String msg) {
        return restResult(null, code, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg) {
        return restResult(null, HttpStatus.WARN, msg);
    }

    /**
     * 返回警告消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static <T> R<T> warn(String msg, T data) {
        return restResult(data, HttpStatus.WARN, msg);
    }


    /**
     * <p style="color: #0066CC"> isOk </p>
     *
     * @param flag flag
     * @return com.haier.hwork.expense.common.util.R<java.lang.Boolean>
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static R<Boolean> isOk(boolean flag) {
        if (flag) {
            return ok(true, "处理成功");
        } else {
            return fail(false, "处理失败");
        }
    }

    /**
     * <p style="color: #0066CC"> restResult </p>
     *
     * @param data data
     * @param code code
     * @param msg  msg
     * @return com.haier.hwork.expense.common.util.R<T>
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    private static <T> R<T> restResult(T data, int code, String msg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }

    /**
     * <p style="color: #0066CC"> isError </p>
     *
     * @param ret ret
     * @return java.lang.Boolean
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> Boolean isError(R<T> ret) {
        return !isSuccess(ret);
    }

    /**
     * <p style="color: #AE00AE"> isSuccess </p>
     *
     * @param ret ret
     * @return java.lang.Boolean
     * @author Anfengmin(✪ ω ✪)
     * @since <span style="color:#FFD306;"> 1.0 </span>
     * <p> 1.0 Initialization method </p>
     */
    public static <T> Boolean isSuccess(R<T> ret) {
        return Optional.ofNullable(ret)
                .map(r -> HttpStatus.SUCCESS == ret.getCode())
                .orElse(false);
    }
}