package com.snail.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型 针对一套 用户体系
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc"),

    /**
     * app端
     */
    APP("app"),

    /**
     * 小程序端
     */
    XCX("xcx");

    private final String device;
}
