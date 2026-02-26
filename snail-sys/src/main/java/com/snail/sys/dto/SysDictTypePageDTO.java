package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.common.core.domain.PageBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/2/26
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_type")
@ApiModel(value = "字典类型")
public class SysDictTypePageDTO extends PageBaseEntity {


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "字典主键")
    private Long id;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典类型")
    private String dictType;

    @ApiModelProperty(value = "状态（0:正常 1:停用）")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;

}
