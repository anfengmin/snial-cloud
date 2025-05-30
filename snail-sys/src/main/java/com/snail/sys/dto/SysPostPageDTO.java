package com.snail.sys.dto;
import java.util.Date;
import com.snail.sys.domain.SysPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "岗位信息")
public class SysPostPageDTO extends PageDTO<SysPost> implements Serializable {

    private static final long serialVersionUID = 1L;

                    @ApiModelProperty(value="岗位ID")
    private Long id;

                    @ApiModelProperty(value="岗位编码")
    private String postCode;

                    @ApiModelProperty(value="岗位名称")
    private String postName;

                    @ApiModelProperty(value="显示顺序")
    private Integer postSort;

                    @ApiModelProperty(value="状态（0正常 1停用）")
    private Integer status;

                    @ApiModelProperty(value="删除标志（0:存在 1:删除）")
    private Integer deleted;

                    @ApiModelProperty(value="创建者")
    private String createBy;

                    @ApiModelProperty(value="创建时间")
    private Date createTime;

                    @ApiModelProperty(value="更新者")
    private String updateBy;

                    @ApiModelProperty(value="更新时间")
    private Date updateTime;

                    @ApiModelProperty(value="备注")
    private String remark;

    }
