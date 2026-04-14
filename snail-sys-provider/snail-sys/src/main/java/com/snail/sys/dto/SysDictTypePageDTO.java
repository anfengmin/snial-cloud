package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.common.core.domain.PageBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "字典类型")
public class SysDictTypePageDTO extends PageBaseEntity {


    @TableId(type = IdType.AUTO)
    @Schema(description = "字典主键")
    private Long id;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "字典类型")
    private String dictType;

    @Schema(description = "状态（0:正常 1:停用）")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

}
