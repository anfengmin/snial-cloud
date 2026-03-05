package com.snail.sys.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 路由返回数据
 *
 * @author snail
 */
@ApiModel("路由返回数据")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouterDataVO {

    @ApiModelProperty("首页路由名称")
    private String home;

    @ApiModelProperty("路由列表")
    private List<RouterVO> routes;
}

