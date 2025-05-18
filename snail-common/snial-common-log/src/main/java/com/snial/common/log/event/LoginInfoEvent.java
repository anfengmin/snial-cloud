package com.snial.common.log.event;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录事件
 *
 * @author Levi.
 * Created time 2025/5/18
 * @since 1.0
 */
@Data
public class LoginInfoEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 登录状态 0成功 1失败
     */
    private Integer status;

    /**
     * ip地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String msg;

}
