package com.snail.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 前端路由树节点
 *
 * 本类放在 snail-sys 模块中，供系统服务内部使用。
 *
 * @author Anfm
 * @since 1.0
 */
@ApiModel("树形结构")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouterVO {

    @ApiModelProperty("路由名字")
    private String name;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("组件地址")
    private String component;

    @ApiModelProperty("其他元素")
    private MetaVO meta;

    @ApiModelProperty("子路由")
    private List<RouterVO> children;
}

