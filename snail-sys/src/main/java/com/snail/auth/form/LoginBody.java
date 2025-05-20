package com.snail.auth.form;

import com.snail.common.core.constant.UserConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Data
@ApiModel("登录对象")
@NoArgsConstructor
public class LoginBody {

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Length(min = UserConstants.USERNAME_MIN_LENGTH, max = UserConstants.USERNAME_MAX_LENGTH, message = "用户名长度2-20个字符")
    private String userCode;

    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "密码不能为空")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "密码长度5-20个字符")
    private String passWord;
}
