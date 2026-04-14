package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.snail.common.core.constant.UserConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 角色信息(SysRole)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:53:00
 */
@Data
@TableName("sys_role")
@Schema(description = "角色信息")
public class SysRole implements Serializable {

    private static final long serialVersionUID = 361781560607356976L;

    @TableId(type = IdType.AUTO)
    @Schema(description = "角色ID")
    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String roleName;

    @NotBlank(message = "权限字符不能为空")
    @Schema(description = "角色权限字符串")
    private String roleKey;

    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序")
    private Integer roleSort;

    @Schema(description = "数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）")
    private String dataScope;

    @Schema(description = "菜单树选择项是否关联显示")
    private Integer menuCheckStrictly;

    @Schema(description = "部门树选择项是否关联显示")
    private Integer deptCheckStrictly;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0:存在 1:删除）")
    private Integer deleted;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "用户是否存在此角色标识 默认不存在")
    @TableField(exist = false)
    private boolean flag = false;

    @Schema(description = "菜单组")
    @TableField(exist = false)
    private Long[] menuIds;

    @Schema(description = "部门组（数据权限）")
    @TableField(exist = false)
    private Long[] deptIds;
    @Schema(description = "是否管理员")
    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.id);
    }

}
