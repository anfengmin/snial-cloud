package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.common.core.domain.PageBaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * method
 *
 * @author Anfm
 * Created time 2026/1/19
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_login_info")
@ApiModel(value = "系统访问记录")
public class SysLogPageDTO extends PageBaseEntity {

    @ApiModelProperty(value = "访问ID")
    private Long id;

    @ApiModelProperty(value = "用户账号")
    private String userName;

    @ApiModelProperty(value = "登录IP地址")
    private String ipaddr;

    @ApiModelProperty(value = "登录地点")
    private String loginLocation;

    @ApiModelProperty(value = "浏览器类型")
    private String browser;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "登录状态（0成功 1失败）")
    private String status;

    @ApiModelProperty(value = "提示消息")
    private String msg;

    @ApiModelProperty(value = "访问时间")
    private Date loginTime;
}
