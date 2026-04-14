package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "AT transaction mode undo table")
public class UndoLog extends Model<UndoLog> {

    private static final long serialVersionUID = 634391168589750851L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "branch transaction id")
    private Long branchId;

    @Schema(description = "global transaction id")
    private String xid;

    @Schema(description = "undo_log context,such as serialization")
    private String context;

    @Schema(description = "rollback info")
    private String rollbackInfo;

    @Schema(description = "0:normal status,1:defense status")
    private Integer logStatus;

    @Schema(description = "create datetime")
    private Object logCreated;

    @Schema(description = "modify datetime")
    private Object logModified;

}
