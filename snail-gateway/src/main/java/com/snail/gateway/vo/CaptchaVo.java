package com.snail.gateway.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * class
 *
 * @author Levi.
 * Created time 2025/5/18
 * @since 1.0
 */

@Data
@ApiModel(value = "验证码")
public class CaptchaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "验证码开关")
    private Boolean captchaEnabled;

    @ApiModelProperty(value = "唯一标识")
    private String uuid;

    @ApiModelProperty(value = "验证码")
    private String img;
}
