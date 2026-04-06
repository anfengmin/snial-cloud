package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AT transaction mode undo table(UndoLog)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("undo_log")
@ApiModel(value = "AT transaction mode undo table")
public class UndoLog extends Model<UndoLog> {

    private static final long serialVersionUID = 634391168589750851L;

    @TableId(type = IdType.INPUT)
    @ApiModelProperty(value = "branch transaction id")
    private Long branchId;

    @ApiModelProperty(value = "global transaction id")
    private String xid;

    @ApiModelProperty(value = "undo_log context,such as serialization")
    private String context;

    @ApiModelProperty(value = "rollback info")
    private String rollbackInfo;

    @ApiModelProperty(value = "0:normal status,1:defense status")
    private Integer logStatus;

    @ApiModelProperty(value = "create datetime")
    private Object logCreated;

    @ApiModelProperty(value = "modify datetime")
    private Object logModified;

}
