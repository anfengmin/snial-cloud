package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.common.core.domain.PageBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 参数配置(SysConfig)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:47:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_config")
@Schema(description = "参数配置")
public class SysConfigPageDTO extends PageBaseEntity {

    private static final long serialVersionUID = -80967284801503921L;


    @Schema(description = "参数主键")
    private Long id;

    @Schema(description = "参数名称")
    private String configName;

    @Schema(description = "参数键名")
    private String configKey;

    @Schema(description = "参数键值")
    private String configValue;

    @Schema(description = "系统内置（Y是 N否）")
    private String configType;


}
