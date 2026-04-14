package com.snail.common.log.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 操作日志记录
 *
 * @author makejava
 * @since 1.0
 */
@Data
@TableName("sys_operate_log")
@Schema(description = "操作日志记录")
public class SysOperateLog implements Serializable {

    private static final long serialVersionUID = 602922838570574584L;

    @TableId(type = IdType.INPUT)
    @Schema(description = "日志主键")
    private Long id;

    @Schema(description = "模块标题")
    private String title;

    @Schema(description = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @Schema(description = "方法名称")
    private String method;

    @Schema(description = "请求方式")
    private String requestMethod;

    @Schema(description = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    @Schema(description = "操作人员")
    private String operatorName;

    @Schema(description = "部门名称")
    private String deptName;

    @Schema(description = "请求URL")
    private String operateUrl;

    @Schema(description = "主机地址")
    private String operateIp;

    @Schema(description = "操作地点")
    private String operateLocation;

    @Schema(description = "请求参数")
    private String operateParam;

    @Schema(description = "返回参数")
    private String jsonResult;

    @Schema(description = "操作状态（0正常 1异常）")
    private Integer status;

    @Schema(description = "错误消息")
    private String errorMsg;

    @Schema(description = "消耗时间(毫秒)")
    private Long costTime;

    @Schema(description = "操作时间")
    private Date operateTime;
}
