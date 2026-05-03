package com.snail.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 行政区划级联树
 *
 * @author Levi.
 * @since 2026-05-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "行政区划级联树")
public class RegionTreeVO extends RegionInfoVO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "下级行政区划")
    private List<RegionTreeVO> children = new ArrayList<>();
}
