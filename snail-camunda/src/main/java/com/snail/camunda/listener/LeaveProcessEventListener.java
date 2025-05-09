package com.snail.camunda.listener;

import com.snail.camunda.domain.LeaveRequest;
import com.snail.camunda.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 请假流程事件监听器
 * 用于在流程事件发生时更新请假申请状态
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeaveProcessEventListener implements ExecutionListener {

    private final LeaveRequestService leaveRequestService;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void notify(DelegateExecution execution) {
        String eventName = execution.getEventName();
        String processInstanceId = execution.getProcessInstanceId();
        String businessKey = execution.getProcessBusinessKey();
        
        log.info("流程事件触发: {}, 流程实例ID: {}, 业务标识: {}", eventName, processInstanceId, businessKey);
        
        try {
            // 根据事件类型处理
            switch (eventName) {
                case ExecutionListener.EVENTNAME_START:
                    handleProcessStart(execution);
                    break;
                case ExecutionListener.EVENTNAME_END:
                    handleProcessEnd(execution);
                    break;
                case ExecutionListener.EVENTNAME_TAKE:
                    handleProcessTransition(execution);
                    break;
                default:
                    log.info("未处理的事件类型: {}", eventName);
            }
        } catch (Exception e) {
            log.error("处理流程事件异常", e);
        }
    }
    
    /**
     * 处理流程启动事件
     */
    private void handleProcessStart(DelegateExecution execution) {
        // 获取流程变量
        Map<String, Object> variables = execution.getVariables();
        String processInstanceId = execution.getProcessInstanceId();
        String businessKey = execution.getProcessBusinessKey();
        
        // 检查是否已存在请假申请记录
        LeaveRequest existingRequest = leaveRequestService.getByProcessInstanceId(processInstanceId);
        if (existingRequest != null) {
            log.info("请假申请已存在，流程实例ID: {}", processInstanceId);
            return;
        }
        
        try {
            // 创建请假申请记录
            LeaveRequest leaveRequest = new LeaveRequest();
            leaveRequest.setProcessInstanceId(processInstanceId);
            leaveRequest.setBusinessKey(businessKey);
            
            // 设置申请人信息
            String initiator = (String) variables.get("initiator");
            leaveRequest.setApplicant(initiator);
            leaveRequest.setName((String) variables.get("name"));
            
            // 设置请假信息
            leaveRequest.setLeaveType((String) variables.get("leaveType"));
            leaveRequest.setReason((String) variables.get("reason"));
            
            // 设置日期
            Object startDateObj = variables.get("startDate");
            Object endDateObj = variables.get("endDate");
            
            if (startDateObj instanceof Date) {
                leaveRequest.setStartDate((Date) startDateObj);
            } else if (startDateObj instanceof String) {
                leaveRequest.setStartDate(DATE_FORMAT.parse((String) startDateObj));
            }
            
            if (endDateObj instanceof Date) {
                leaveRequest.setEndDate((Date) endDateObj);
            } else if (endDateObj instanceof String) {
                leaveRequest.setEndDate(DATE_FORMAT.parse((String) endDateObj));
            }
            
            // 设置状态为进行中
            leaveRequest.setStatus(0);
            
            // 保存请假申请
            leaveRequestService.createLeaveRequest(leaveRequest);
            log.info("创建请假申请成功，流程实例ID: {}", processInstanceId);
            
        } catch (Exception e) {
            log.error("创建请假申请失败", e);
        }
    }
    
    /**
     * 处理流程结束事件
     */
    private void handleProcessEnd(DelegateExecution execution) {
        String processInstanceId = execution.getProcessInstanceId();
        
        // 更新请假申请状态为已完成
        boolean updated = leaveRequestService.updateStatus(processInstanceId, 1);
        if (updated) {
            log.info("请假流程已完成，更新状态成功，流程实例ID: {}", processInstanceId);
        } else {
            log.warn("请假流程已完成，但更新状态失败，流程实例ID: {}", processInstanceId);
        }
        
        // 获取实际请假日期并更新
        Map<String, Object> variables = execution.getVariables();
        Object actualStartDateObj = variables.get("actualStartDate");
        Object actualEndDateObj = variables.get("actualEndDate");
        
        if (actualStartDateObj != null && actualEndDateObj != null) {
            String actualStartDate = actualStartDateObj.toString();
            String actualEndDate = actualEndDateObj.toString();
            
            boolean updatedDates = leaveRequestService.updateActualDates(processInstanceId, actualStartDate, actualEndDate);
            if (updatedDates) {
                log.info("更新实际请假日期成功，流程实例ID: {}", processInstanceId);
            } else {
                log.warn("更新实际请假日期失败，流程实例ID: {}", processInstanceId);
            }
        }
    }
    
    /**
     * 处理流程流转事件
     */
    private void handleProcessTransition(DelegateExecution execution) {
        // 获取当前活动ID
        ExecutionEntity entity = (ExecutionEntity) execution;
        String sourceActivityId = entity.getActivityId();
        String targetActivityId = entity.getActivityId();
        
        log.info("流程流转: {} -> {}, 流程实例ID: {}", 
                sourceActivityId, targetActivityId, execution.getProcessInstanceId());
        
        // 可以根据特定的流转路径执行相应的业务逻辑
        // 例如，当流程被驳回时，可以更新请假申请的状态
        Map<String, Object> variables = execution.getVariables();
        Boolean approved = (Boolean) variables.get("approved");
        
        if (approved != null && !approved) {
            // 流程被驳回，可以记录驳回原因等信息
            String comment = (String) variables.get("comment");
            log.info("请假申请被驳回，原因: {}, 流程实例ID: {}", 
                    comment, execution.getProcessInstanceId());
        }
    }
}