package com.snail.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 滑动验证码返回对象
 *
 * @author ruoyi
 */
@Data
@ApiModel(value = "滑动验证码")
public class SlidingCaptchaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "唯一标识")
    private String uuid;

    @ApiModelProperty(value = "背景图(base64)")
    private String backgroundImg;

    @ApiModelProperty(value = "滑块图(base64)")
    private String sliderImg;
}


