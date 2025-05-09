package com.snail.camunda.service.impl;

import com.snail.camunda.service.LeaveProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 请假流程服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveProcessServiceImpl implements LeaveProcessService {

    private final RepositoryService repositoryService;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    
    private static final String PROCESS_DEFINITION_KEY = "leave-approval";
    private static final String BPMN_FILE_PATH = "bpmn/leave-approval.bpmn";

    @Override
    public Map<String, Object> deployLeaveProcess() {
        try {
            // 从classpath加载BPMN文件
            ClassPathResource resource = new ClassPathResource(BPMN_FILE_PATH);
            InputStream inputStream = resource.getInputStream();
            
            // 部署流程
            Deployment deployment = repositoryService.createDeployment()
                    .addInputStream(BPMN_FILE_PATH, inputStream)
                    .name("请假审批流程")
                    .deploy();
            
            // 获取流程定义
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                    .deploymentId(deployment.getId())
                    .singleResult();
            
            // 返回部署信息
            Map<String, Object> result = new HashMap<>();
            result.put("deploymentId", deployment.getId());
            result.put("processDefinitionId", processDefinition.getId());
            result.put("processDefinitionKey", processDefinition.getKey());
            result.put("processDefinitionName", processDefinition.getName());
            
            return result;
        } catch (IOException e) {
            log.error("部署请假流程失败", e);
            throw new RuntimeException("部署请假流程失败: " + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> startLeaveProcess(String initiator, Map<String, Object> variables) {
        if (variables == null) {
            variables = new HashMap<>();
        }
        
        // 设置流程发起人
        variables.put("initiator", initiator);
        
        // 生成业务标识
        String businessKey = "LEAVE_" + System.currentTimeMillis();
        
        // 启动流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
                PROCESS_DEFINITION_KEY, businessKey, variables);
        
        // 返回流程实例信息
        Map<String, Object> result = new HashMap<>();
        result.put("processInstanceId", processInstance.getId());
        result.put("processDefinitionId", processInstance.getProcessDefinitionId());
        result.put("businessKey", processInstance.getBusinessKey());
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getUserTasks(String assignee) {
        // 查询用户待办任务
        List<Task> tasks = taskService.createTaskQuery()
                .taskAssignee(assignee)
                .list();
        
        // 转换为前端所需格式
        List<Map<String, Object>> result = new ArrayList<>();
        for (Task task : tasks) {
            Map<String, Object> taskMap = new HashMap<>();
            taskMap.put("taskId", task.getId());
            taskMap.put("taskName", task.getName());
            taskMap.put("processInstanceId", task.getProcessInstanceId());
            taskMap.put("createTime", task.getCreateTime());
            taskMap.put("assignee", task.getAssignee());
            
            // 获取流程实例的业务数据
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(task.getProcessInstanceId())
                    .singleResult();
            if (processInstance != null) {
                taskMap.put("businessKey", processInstance.getBusinessKey());
            }
            
            // 获取表单数据
            Map<String, Object> variables = taskService.getVariables(task.getId());
            taskMap.put("formData", variables);
            
            result.add(taskMap);
        }
        
        return result;
    }

    @Override
    public void completeLeaveApplyTask(String taskId, Map<String, Object> variables) {
        // 获取当前任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 设置部门经理和人事经理（实际应用中应从组织结构中获取）
        variables.put("deptManager", "manager1");
        variables.put("hrManager", "hr1");
        
        // 完成任务
        taskService.complete(taskId, variables);
    }

    @Override
    public void completeApprovalTask(String taskId, boolean approved, String comment) {
        // 获取当前任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 添加审批意见
        taskService.addComment(taskId, task.getProcessInstanceId(), "comment", comment);
        
        // 设置审批结果变量
        Map<String, Object> variables = new HashMap<>();
        variables.put("approved", approved);
        variables.put("comment", comment);
        
        // 完成任务
        taskService.complete(taskId, variables);
    }

    @Override
    public void completeLeaveFinishTask(String taskId, String actualStartDate, String actualEndDate) {
        // 获取当前任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 设置实际请假日期
        Map<String, Object> variables = new HashMap<>();
        variables.put("actualStartDate", actualStartDate);
        variables.put("actualEndDate", actualEndDate);
        
        // 完成任务
        taskService.complete(taskId, variables);
    }

    @Override
    public Map<String, Object> getProcessInstanceDetail(String processInstanceId) {
        // 查询流程实例
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(processInstanceId)
                .singleResult();
        
        if (processInstance == null) {
            throw new RuntimeException("流程实例不存在");
        }
        
        // 获取流程变量
        Map<String, Object> variables = runtimeService.getVariables(processInstanceId);
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("processInstanceId", processInstance.getId());
        result.put("processDefinitionId", processInstance.getProcessDefinitionId());
        result.put("businessKey", processInstance.getBusinessKey());
        result.put("variables", variables);
        
        return result;
    }

    @Override
    public Map<String, Object> getTaskDetail(String taskId) {
        // 查询任务
        Task task = taskService.createTaskQuery()
                .taskId(taskId)
                .singleResult();
        
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        
        // 获取任务表单变量
        Map<String, Object> variables = taskService.getVariables(taskId);
        
        // 构建结果
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", task.getId());
        result.put("taskName", task.getName());
        result.put("processInstanceId", task.getProcessInstanceId());
        result.put("createTime", task.getCreateTime());
        result.put("assignee", task.getAssignee());
        result.put("formData", variables);
        
        return result;
    }
}