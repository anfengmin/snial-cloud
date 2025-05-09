package com.snail.camunda.service;

import java.util.List;
import java.util.Map;

/**
 * 请假流程服务接口
 */
public interface LeaveProcessService {
    
    /**
     * 部署请假流程
     * 
     * @return 部署结果信息
     */
    Map<String, Object> deployLeaveProcess();
    
    /**
     * 启动请假流程
     * 
     * @param initiator 发起人
     * @param variables 流程变量
     * @return 流程实例信息
     */
    Map<String, Object> startLeaveProcess(String initiator, Map<String, Object> variables);
    
    /**
     * 获取用户待办任务
     * 
     * @param assignee 办理人
     * @return 任务列表
     */
    List<Map<String, Object>> getUserTasks(String assignee);
    
    /**
     * 完成请假申请任务
     * 
     * @param taskId 任务ID
     * @param variables 表单变量
     */
    void completeLeaveApplyTask(String taskId, Map<String, Object> variables);
    
    /**
     * 完成审批任务
     * 
     * @param taskId 任务ID
     * @param approved 是否同意
     * @param comment 审批意见
     */
    void completeApprovalTask(String taskId, boolean approved, String comment);
    
    /**
     * 完成销假任务
     * 
     * @param taskId 任务ID
     * @param actualStartDate 实际开始日期
     * @param actualEndDate 实际结束日期
     */
    void completeLeaveFinishTask(String taskId, String actualStartDate, String actualEndDate);
    
    /**
     * 查询流程实例详情
     * 
     * @param processInstanceId 流程实例ID
     * @return 流程实例详情
     */
    Map<String, Object> getProcessInstanceDetail(String processInstanceId);
    
    /**
     * 查询任务详情
     * 
     * @param taskId 任务ID
     * @return 任务详情
     */
    Map<String, Object> getTaskDetail(String taskId);
}