package com.snail.camunda.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 请假申请实体类
 */
@Data
@TableName("leave_request")
public class LeaveRequest {
    
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 流程实例ID
     */
    private String processInstanceId;
    
    /**
     * 业务标识
     */
    private String businessKey;
    
    /**
     * 申请人
     */
    private String applicant;
    
    /**
     * 申请人姓名
     */
    private String name;
    
    /**
     * 请假类型（sick-病假，annual-年假，personal-事假）
     */
    private String leaveType;
    
    /**
     * 开始日期
     */
    private Date startDate;
    
    /**
     * 结束日期
     */
    private Date endDate;
    
    /**
     * 请假原因
     */
    private String reason;
    
    /**
     * 实际开始日期
     */
    private Date actualStartDate;
    
    /**
     * 实际结束日期
     */
    private Date actualEndDate;
    
    /**
     * 流程状态（0-进行中，1-已完成，2-已取消）
     */
    private Integer status;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 更新时间
     */
    private Date updateTime;
}