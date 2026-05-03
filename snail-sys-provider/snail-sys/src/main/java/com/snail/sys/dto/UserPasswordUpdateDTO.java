package com.snail.sys.dto;

import com.snail.common.core.constant.UserConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 当前登录用户密码更新参数
 */
@Data
@Schema(description = "个人密码更新参数")
public class UserPasswordUpdateDTO {

    @Schema(description = "原密码")
    @NotBlank(message = "{user.password.not.blank}")
    private String oldPassword;

    @Schema(description = "新密码")
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    private String newPassword;

    @Schema(description = "确认新密码")
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    private String confirmPassword;
}
