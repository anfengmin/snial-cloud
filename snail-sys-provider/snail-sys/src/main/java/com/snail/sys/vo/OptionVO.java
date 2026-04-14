package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "下拉列表")
public class OptionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "值")
    private Long value;

    @Schema(description = "标签")
    private String label;


}
