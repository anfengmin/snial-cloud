//package com.snail.camunda.controller;
//
//import com.snail.common.core.util.R;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.camunda.bpm.engine.RepositoryService;
//import org.camunda.bpm.engine.RuntimeService;
//import org.camunda.bpm.engine.TaskService;
//import org.camunda.bpm.engine.repository.Deployment;
//import org.camunda.bpm.engine.repository.ProcessDefinition;
//import org.camunda.bpm.engine.runtime.ProcessInstance;
//import org.camunda.bpm.engine.task.Task;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 工作流控制器
// * 提供流程部署、发起、审批等功能
// */
//@Slf4j
//@RestController
//@RequestMapping("/workflow")
//@RequiredArgsConstructor
//public class WorkflowController {
//
//    private final RepositoryService repositoryService;
//    private final RuntimeService runtimeService;
//    private final TaskService taskService;
//
//    /**
//     * 部署流程
//     *
//     * @param file BPMN文件
//     * @return 部署结果
//     */
//    @PostMapping("/deploy")
//    public R<Map<String, Object>> deployProcess(@RequestParam("file") MultipartFile file) {
//        try {
//            String fileName = file.getOriginalFilename();
//            Deployment deployment = repositoryService.createDeployment()
//                    .addInputStream(fileName, file.getInputStream())
//                    .name(fileName)
//                    .deploy();
//
//            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                    .deploymentId(deployment.getId())
//                    .singleResult();
//
//            Map<String, Object> result = new HashMap<>();
//            result.put("deploymentId", deployment.getId());
//            result.put("processDefinitionId", processDefinition.getId());
//            result.put("processDefinitionKey", processDefinition.getKey());
//            result.put("processDefinitionName", processDefinition.getName());
//
//            return R.ok(result, "流程部署成功");
//        } catch (IOException e) {
//            log.error("流程部署失败", e);
//            return R.fail("流程部署失败：" + e.getMessage());
//        }
//    }
//
//    /**
//     * 启动流程实例
//     *
//     * @param processDefinitionKey 流程定义Key
//     * @param businessKey 业务标识
//     * @param variables 流程变量
//     * @return 启动结果
//     */
//    @PostMapping("/start")
//    public R<Map<String, Object>> startProcess(
//            @RequestParam("processDefinitionKey") String processDefinitionKey,
//            @RequestParam("businessKey") String businessKey,
//            @RequestBody(required = false) Map<String, Object> variables) {
//
//        if (variables == null) {
//            variables = new HashMap<>();
//        }
//
//        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(
//                processDefinitionKey, businessKey, variables);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("processInstanceId", processInstance.getId());
//        result.put("processDefinitionId", processInstance.getProcessDefinitionId());
//        result.put("businessKey", processInstance.getBusinessKey());
//
//        return R.ok(result, "流程启动成功");
//    }
//
//    /**
//     * 查询待办任务
//     *
//     * @param assignee 办理人
//     * @return 任务列表
//     */
//    @GetMapping("/tasks")
//    public R<List<Map<String, Object>>> getTasks(@RequestParam("assignee") String assignee) {
//        List<Task> tasks = taskService.createTaskQuery()
//                .taskAssignee(assignee)
//                .list();
//
//        List<Map<String, Object>> result = tasks.stream().map(task -> {
//            Map<String, Object> taskMap = new HashMap<>();
//            taskMap.put("taskId", task.getId());
//            taskMap.put("taskName", task.getName());
//            taskMap.put("processInstanceId", task.getProcessInstanceId());
//            taskMap.put("createTime", task.getCreateTime());
//            taskMap.put("assignee", task.getAssignee());
//            return taskMap;
//        }).collect(Collectors.toList());
//
//        return R.ok(result);
//    }
//
//    /**
//     * 完成任务
//     *
//     * @param taskId 任务ID
//     * @param variables 流程变量
//     * @return 完成结果
//     */
//    @PostMapping("/complete")
//    public R<Void> completeTask(
//            @RequestParam("taskId") String taskId,
//            @RequestBody(required = false) Map<String, Object> variables) {
//
//        if (variables == null) {
//            variables = new HashMap<>();
//        }
//
//        taskService.complete(taskId, variables);
//        return R.ok(null, "任务完成");
//    }
//
//    /**
//     * 驳回任务
//     *
//     * @param taskId 任务ID
//     * @param reason 驳回原因
//     * @return 驳回结果
//     */
//    @PostMapping("/reject")
//    public R<Void> rejectTask(
//            @RequestParam("taskId") String taskId,
//            @RequestParam("reason") String reason) {
//
//        // 获取当前任务
//        Task task = taskService.createTaskQuery()
//                .taskId(taskId)
//                .singleResult();
//
//        if (task == null) {
//            return R.fail("任务不存在");
//        }
//
//        // 添加驳回意见
//        taskService.addComment(taskId, task.getProcessInstanceId(), "reject", reason);
//
//        // 设置流程变量，标记为驳回
//        Map<String, Object> variables = new HashMap<>();
//        variables.put("approved", false);
//        variables.put("rejectReason", reason);
//
//        // 完成任务，流程会根据变量走向驳回路径
//        taskService.complete(taskId, variables);
//
//        return R.ok(null, "任务已驳回");
//    }
//
//    /**
//     * 添加候选人（加签）
//     *
//     * @param taskId 任务ID
//     * @param candidateUser 候选人
//     * @return 结果
//     */
//    @PostMapping("/addCandidate")
//    public R<Void> addCandidate(
//            @RequestParam("taskId") String taskId,
//            @RequestParam("candidateUser") String candidateUser) {
//
//        taskService.addCandidateUser(taskId, candidateUser);
//        return R.ok(null, "加签成功");
//    }
//}