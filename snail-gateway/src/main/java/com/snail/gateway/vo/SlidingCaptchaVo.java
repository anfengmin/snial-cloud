package com.snail.gateway.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 滑动验证码返回对象
 *
 * @author ruoyi
 */
@Data
@Schema(description = "滑动验证码")
public class SlidingCaptchaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "唯一标识")
    private String uuid;

    @Schema(description = "背景图(base64)")
    private String backgroundImg;

    @Schema(description = "滑块图(base64)")
    private String sliderImg;
}


