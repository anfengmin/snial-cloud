package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.sys.api.domain.PageBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "参数配置")
public class SysConfigPageDTO extends PageBaseEntity {

    private static final long serialVersionUID = -80967284801503921L;


    @ApiModelProperty(value = "参数主键")
    private Long id;

    @ApiModelProperty(value = "参数名称")
    private String configName;

    @ApiModelProperty(value = "参数键名")
    private String configKey;

    @ApiModelProperty(value = "参数键值")
    private String configValue;

    @ApiModelProperty(value = "系统内置（Y是 N否）")
    private String configType;


}
