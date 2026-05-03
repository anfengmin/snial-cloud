package com.snail.sys.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 当前在线会话
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
@Data
@NoArgsConstructor
public class SysUserOnline implements Serializable {

    /**
     * 会话编号
     */
    private String tokenId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用户名称
     */
    private String userCode;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地址
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
     * 终端类型
     */
    private String clientType;

    /**
     * 设备标识
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 是否为当前设备
     */
    private Boolean currentDevice;

}
