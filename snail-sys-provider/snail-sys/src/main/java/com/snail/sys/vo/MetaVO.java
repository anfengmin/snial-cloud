package com.snail.sys.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由显示信息
 *
 * 本类放在 snail-sys 模块中，供系统服务内部使用。
 *
 * @author Anfm
 * @since 1.0
 */
@Schema(description = "路由显示信息")
@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MetaVO {

    @Schema(description = "设置该路由在侧边栏和面包屑中展示的名字")
    private String title;

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    @Schema(description = "设置该路由的图标")
    private String icon;

    @Schema(description = "是否缓存该路由")
    private boolean keepAlive;

    @Schema(description = "动态路由固定路由")
    private boolean constant;

    @Schema(description = "是否隐藏路由")
    private boolean hideInMenu;

    @Schema(description = "内链地址")
    private String link;
}

