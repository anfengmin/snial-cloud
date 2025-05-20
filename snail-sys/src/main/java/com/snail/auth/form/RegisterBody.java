package com.snail.auth.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/5/20
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RegisterBody extends LoginBody {

    @ApiModelProperty(value = "用户类型")
    private String userType;

    @ApiModelProperty(value = "用户名称")
    private String userName;

}