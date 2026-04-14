package com.snail.gateway.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "验证码")
public class CaptchaVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "验证码开关")
    private Boolean captchaEnabled;

    @Schema(description = "唯一标识")
    private String uuid;

    @Schema(description = "验证码")
    private String img;
}
