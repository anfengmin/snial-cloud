package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户与岗位关联表(SysUserPost)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_post")
@Schema(description = "用户与岗位关联表")
public class SysUserPost extends Model<SysUserPost> {

    private static final long serialVersionUID = -52753061857241092L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "岗位ID")
    private Long postId;

}
