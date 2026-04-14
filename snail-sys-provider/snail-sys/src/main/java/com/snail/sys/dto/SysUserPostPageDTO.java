package com.snail.sys.dto;

import com.snail.sys.domain.SysUserPost;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户与岗位关联表(SysUserPost)
 *
 * @author makejava
 * @since 2025-05-30 23:13:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户与岗位关联表")
public class SysUserPostPageDTO extends PageDTO<SysUserPost> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "岗位ID")
    private Long postId;

}
