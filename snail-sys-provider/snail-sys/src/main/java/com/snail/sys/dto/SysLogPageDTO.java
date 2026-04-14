package com.snail.sys.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.snail.common.core.domain.PageBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "系统访问记录")
public class SysLogPageDTO extends PageBaseEntity {

    @Schema(description = "访问ID")
    private Long id;

    @Schema(description = "用户账号")
    private String userName;

    @Schema(description = "登录IP地址")
    private String ipaddr;

    @Schema(description = "登录地点")
    private String loginLocation;

    @Schema(description = "浏览器类型")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "登录状态（0成功 1失败）")
    private String status;

    @Schema(description = "提示消息")
    private String msg;

    @Schema(description = "访问时间")
    private Date loginTime;
}
