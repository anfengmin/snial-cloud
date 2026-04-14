package com.snail.common.core.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "搜索值")
    @TableField(exist = false)
    private String searchValue;

    @Schema(description = "开始时间")
    private String beginTime;

    @Schema(description = "结束时间")
    private String endTime;

    @Schema(description = "排序列")
    private String column;

    @Schema(description = "排序的方向desc或者asc")
    private boolean orderType;
}
