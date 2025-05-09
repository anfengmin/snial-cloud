package com.snail.camunda.controller;

import com.snail.camunda.service.LeaveProcessService;
import com.snail.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 请假流程控制器
 * 提供请假流程相关的API接口
 */
@Slf4j
@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveProcessController {

    private final LeaveProcessService leaveProcessService;

    /**
     * 部署请假流程
     *
     * @return 部署结果
     */
    @PostMapping("/deploy")
    public R<Map<String, Object>> deployLeaveProcess() {
        try {
            Map<String, Object> result = leaveProcessService.deployLeaveProcess();
            return R.ok(result, "请假流程部署成功");
        } catch (Exception e) {
            log.error("请假流程部署失败", e);
            return R.fail("请假流程部署失败：" + e.getMessage());
        }
    }

    /**
     * 发起请假申请
     *
     * @param initiator 发起人
     * @param leaveForm 请假表单数据
     * @return 流程实例信息
     */
    @PostMapping("/start")
    public R<Map<String, Object>> startLeaveProcess(
            @RequestParam("initiator") String initiator,
            @RequestBody Map<String, Object> leaveForm) {
        try {
            Map<String, Object> result = leaveProcessService.startLeaveProcess(initiator, leaveForm);
            return R.ok(result, "请假流程发起成功");
        } catch (Exception e) {
            log.error("请假流程发起失败", e);
            return R.fail("请假流程发起失败：" + e.getMessage());
        }
    }

    /**
     * 获取用户待办任务
     *
     * @param assignee 办理人
     * @return 任务列表
     */
    @GetMapping("/tasks")
    public R<List<Map<String, Object>>> getUserTasks(@RequestParam("assignee") String assignee) {
        try {
            List<Map<String, Object>> tasks = leaveProcessService.getUserTasks(assignee);
            return R.ok(tasks, "获取待办任务成功");
        } catch (Exception e) {
            log.error("获取待办任务失败", e);
            return R.fail("获取待办任务失败：" + e.getMessage());
        }
    }

    /**
     * 完成请假申请任务
     *
     * @param taskId 任务ID
     * @param leaveForm 请假表单数据
     * @return 完成结果
     */
    @PostMapping("/apply/complete")
    public R<Void> completeLeaveApplyTask(
            @RequestParam("taskId") String taskId,
            @RequestBody Map<String, Object> leaveForm) {
        try {
            leaveProcessService.completeLeaveApplyTask(taskId, leaveForm);
            return R.ok(null, "请假申请提交成功");
        } catch (Exception e) {
            log.error("请假申请提交失败", e);
            return R.fail("请假申请提交失败：" + e.getMessage());
        }
    }

    /**
     * 完成审批任务
     *
     * @param taskId 任务ID
     * @param approved 是否同意
     * @param comment 审批意见
     * @return 完成结果
     */
    @PostMapping("/approval/complete")
    public R<Void> completeApprovalTask(
            @RequestParam("taskId") String taskId,
            @RequestParam("approved") boolean approved,
            @RequestParam("comment") String comment) {
        try {
            leaveProcessService.completeApprovalTask(taskId, approved, comment);
            return R.ok(null, "审批完成");
        } catch (Exception e) {
            log.error("审批失败", e);
            return R.fail("审批失败：" + e.getMessage());
        }
    }

    /**
     * 完成销假任务
     *
     * @param taskId 任务ID
     * @param actualStartDate 实际开始日期
     * @param actualEndDate 实际结束日期
     * @return 完成结果
     */
    @PostMapping("/finish/complete")
    public R<Void> completeLeaveFinishTask(
            @RequestParam("taskId") String taskId,
            @RequestParam("actualStartDate") String actualStartDate,
            @RequestParam("actualEndDate") String actualEndDate) {
        try {
            leaveProcessService.completeLeaveFinishTask(taskId, actualStartDate, actualEndDate);
            return R.ok(null, "销假完成");
        } catch (Exception e) {
            log.error("销假失败", e);
            return R.fail("销假失败：" + e.getMessage());
        }
    }

    /**
     * 获取流程实例详情
     *
     * @param processInstanceId 流程实例ID
     * @return 流程实例详情
     */
    @GetMapping("/process/detail")
    public R<Map<String, Object>> getProcessInstanceDetail(
            @RequestParam("processInstanceId") String processInstanceId) {
        try {
            Map<String, Object> detail = leaveProcessService.getProcessInstanceDetail(processInstanceId);
            return R.ok(detail, "获取流程实例详情成功");
        } catch (Exception e) {
            log.error("获取流程实例详情失败", e);
            return R.fail("获取流程实例详情失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    @GetMapping("/task/detail")
    public R<Map<String, Object>> getTaskDetail(@RequestParam("taskId") String taskId) {
        try {
            Map<String, Object> detail = leaveProcessService.getTaskDetail(taskId);
            return R.ok(detail, "获取任务详情成功");
        } catch (Exception e) {
            log.error("获取任务详情失败", e);
            return R.fail("获取任务详情失败：" + e.getMessage());
        }
    }
}