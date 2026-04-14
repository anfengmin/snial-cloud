package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户和角色关联表(SysUserRole)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_role")
@Schema(description = "用户和角色关联表")
public class SysUserRole extends Model<SysUserRole> {

    private static final long serialVersionUID = -26215685220310436L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "角色ID")
    private Long roleId;

}
