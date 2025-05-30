package com.snail.sys.dto;

import com.snail.sys.domain.SysUserRole;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户和角色关联表(SysUserRole)
 *
 * @author makejava
 * @since 2025-05-30 23:14:08
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "用户和角色关联表")
public class SysUserRolePageDTO extends PageDTO<SysUserRole> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "角色ID")
    private Long roleId;

}
