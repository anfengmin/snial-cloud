package com.snail.sys.dto;

import com.snail.sys.domain.SysUserPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "用户与岗位关联表")
public class SysUserPostPageDTO extends PageDTO<SysUserPost> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "岗位ID")
    private Long postId;

}
