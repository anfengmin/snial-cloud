package com.snail.sys.domain;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 操作日志记录(SysOperateLog)实体类
 *
 * @author makejava
 * @since 2025-05-21 21:52:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_operate_log")
@ApiModel(value = "操作日志记录")
public class SysOperateLog extends Model<SysOperateLog> {

    private static final long serialVersionUID = 602922838570574584L;


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "日志主键")
    private Long id;

    @ApiModelProperty(value = "模块标题")
    private String title;

    @ApiModelProperty(value = "业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @ApiModelProperty(value = "方法名称")
    private String method;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    @ApiModelProperty(value = "操作人员")
    private String operatorName;

    @ApiModelProperty(value = "部门名称")
    private String deptName;

    @ApiModelProperty(value = "请求URL")
    private String operateUrl;

    @ApiModelProperty(value = "主机地址")
    private String operateIp;

    @ApiModelProperty(value = "操作地点")
    private String operateLocation;

    @ApiModelProperty(value = "请求参数")
    private String operateParam;

    @ApiModelProperty(value = "返回参数")
    private String jsonResult;

    @ApiModelProperty(value = "操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty(value = "错误消息")
    private String errorMsg;

    @ApiModelProperty(value = "操作时间")
    private Date operTime;

}
