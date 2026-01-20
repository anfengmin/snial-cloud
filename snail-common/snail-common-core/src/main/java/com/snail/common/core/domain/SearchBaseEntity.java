package com.snail.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/11/3
 * @since 1.0
 */
@Data
public class SearchBaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @ApiModelProperty(value = "搜索值")
    @TableField(exist = false)
    private String searchValue;

    @ApiModelProperty(value = "开始时间")
    private String beginTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

    @ApiModelProperty(value = "排序列")
    private String column;

    @ApiModelProperty(value = "排序的方向desc或者asc")
    private boolean orderType;
}
