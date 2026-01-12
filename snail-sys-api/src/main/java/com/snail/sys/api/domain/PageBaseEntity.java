package com.snail.sys.api.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/11/3
 * @since 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PageBaseEntity extends SearchBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @NotNull(message = "当前页码不能为空")
    @ApiModelProperty(value = "当前页码")
    private Integer current = 1;

    @NotNull(message = "每页条数不能为空")
    @ApiModelProperty(value = "每页条数")
    private Integer size = 10;
}
