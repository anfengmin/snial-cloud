package com.snail.camunda.service;

import com.snail.camunda.domain.LeaveRequest;

import java.util.List;

/**
 * 请假申请服务接口
 */
public interface LeaveRequestService {
    
    /**
     * 创建请假申请
     * 
     * @param leaveRequest 请假申请信息
     * @return 创建的请假申请
     */
    LeaveRequest createLeaveRequest(LeaveRequest leaveRequest);
    
    /**
     * 更新请假申请
     * 
     * @param leaveRequest 请假申请信息
     * @return 更新后的请假申请
     */
    LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest);
    
    /**
     * 根据流程实例ID查询请假申请
     * 
     * @param processInstanceId 流程实例ID
     * @return 请假申请
     */
    LeaveRequest getByProcessInstanceId(String processInstanceId);
    
    /**
     * 根据申请人查询请假申请列表
     * 
     * @param applicant 申请人
     * @return 请假申请列表
     */
    List<LeaveRequest> getByApplicant(String applicant);
    
    /**
     * 更新请假申请状态
     * 
     * @param processInstanceId 流程实例ID
     * @param status 状态
     * @return 是否更新成功
     */
    boolean updateStatus(String processInstanceId, Integer status);
    
    /**
     * 更新请假申请的实际日期
     * 
     * @param processInstanceId 流程实例ID
     * @param actualStartDate 实际开始日期
     * @param actualEndDate 实际结束日期
     * @return 是否更新成功
     */
    boolean updateActualDates(String processInstanceId, String actualStartDate, String actualEndDate);
}