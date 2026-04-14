package com.snail.sys.dto;
import java.util.Date;

import com.snail.sys.domain.SysPost;
import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

/**
 * 岗位信息(SysPost)
 *
 * @author makejava
 * @since 2025-05-30 23:05:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "岗位信息")
public class SysPostPageDTO extends PageDTO<SysPost> implements Serializable {

    private static final long serialVersionUID = 1L;

                    @Schema(description = "岗位ID")
    private Long id;

                    @Schema(description = "岗位编码")
    private String postCode;

                    @Schema(description = "岗位名称")
    private String postName;

                    @Schema(description = "显示顺序")
    private Integer postSort;

                    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

                    @Schema(description = "删除标志（0:存在 1:删除）")
    private Integer deleted;

                    @Schema(description = "创建者")
    private String createBy;

                    @Schema(description = "创建时间")
    private Date createTime;

                    @Schema(description = "更新者")
    private String updateBy;

                    @Schema(description = "更新时间")
    private Date updateTime;

                    @Schema(description = "备注")
    private String remark;

    }
