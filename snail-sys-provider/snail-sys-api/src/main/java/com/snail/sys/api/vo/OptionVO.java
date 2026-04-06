package com.snail.sys.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/27
 * @since 1.0
 */
@AllArgsConstructor
@NoArgsConstructor()
@Data
@ApiModel("下拉列表")
public class OptionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("值")
    private Long value;

    @ApiModelProperty("标签")
    private String label;


}
