package com.snail.camunda.controller;

import com.snail.camunda.domain.LeaveRequest;
import com.snail.camunda.service.LeaveRequestService;
import com.snail.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 请假申请控制器
 * 提供请假申请数据的管理接口
 */
@Slf4j
@RestController
@RequestMapping("/leave/request")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveRequestService;

    /**
     * 创建请假申请
     *
     * @param leaveRequest 请假申请信息
     * @return 创建结果
     */
    @PostMapping
    public R<LeaveRequest> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        try {
            LeaveRequest result = leaveRequestService.createLeaveRequest(leaveRequest);
            return R.ok(result, "创建请假申请成功");
        } catch (Exception e) {
            log.error("创建请假申请失败", e);
            return R.fail("创建请假申请失败：" + e.getMessage());
        }
    }

    /**
     * 更新请假申请
     *
     * @param leaveRequest 请假申请信息
     * @return 更新结果
     */
    @PutMapping
    public R<LeaveRequest> updateLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        try {
            LeaveRequest result = leaveRequestService.updateLeaveRequest(leaveRequest);
            return R.ok(result, "更新请假申请成功");
        } catch (Exception e) {
            log.error("更新请假申请失败", e);
            return R.fail("更新请假申请失败：" + e.getMessage());
        }
    }

    /**
     * 根据流程实例ID查询请假申请
     *
     * @param processInstanceId 流程实例ID
     * @return 请假申请信息
     */
    @GetMapping("/process/{processInstanceId}")
    public R<LeaveRequest> getByProcessInstanceId(@PathVariable("processInstanceId") String processInstanceId) {
        try {
            LeaveRequest leaveRequest = leaveRequestService.getByProcessInstanceId(processInstanceId);
            if (leaveRequest != null) {
                return R.ok(leaveRequest);
            } else {
                return R.fail("未找到对应的请假申请");
            }
        } catch (Exception e) {
            log.error("查询请假申请失败", e);
            return R.fail("查询请假申请失败：" + e.getMessage());
        }
    }

    /**
     * 根据申请人查询请假申请列表
     *
     * @param applicant 申请人
     * @return 请假申请列表
     */
    @GetMapping("/applicant/{applicant}")
    public R<List<LeaveRequest>> getByApplicant(@PathVariable("applicant") String applicant) {
        try {
            List<LeaveRequest> leaveRequests = leaveRequestService.getByApplicant(applicant);
            return R.ok(leaveRequests);
        } catch (Exception e) {
            log.error("查询请假申请列表失败", e);
            return R.fail("查询请假申请列表失败：" + e.getMessage());
        }
    }

    /**
     * 更新请假申请状态
     *
     * @param processInstanceId 流程实例ID
     * @param status 状态（0-进行中，1-已完成，2-已取消）
     * @return 更新结果
     */
    @PutMapping("/status")
    public R<Boolean> updateStatus(
            @RequestParam("processInstanceId") String processInstanceId,
            @RequestParam("status") Integer status) {
        try {
            boolean result = leaveRequestService.updateStatus(processInstanceId, status);
            if (result) {
                return R.ok(true, "更新状态成功");
            } else {
                return R.fail("更新状态失败");
            }
        } catch (Exception e) {
            log.error("更新请假申请状态失败", e);
            return R.fail("更新请假申请状态失败：" + e.getMessage());
        }
    }

    /**
     * 更新请假申请的实际日期
     *
     * @param processInstanceId 流程实例ID
     * @param actualStartDate 实际开始日期
     * @param actualEndDate 实际结束日期
     * @return 更新结果
     */
    @PutMapping("/actual-dates")
    public R<Boolean> updateActualDates(
            @RequestParam("processInstanceId") String processInstanceId,
            @RequestParam("actualStartDate") String actualStartDate,
            @RequestParam("actualEndDate") String actualEndDate) {
        try {
            boolean result = leaveRequestService.updateActualDates(processInstanceId, actualStartDate, actualEndDate);
            if (result) {
                return R.ok(true, "更新实际日期成功");
            } else {
                return R.fail("更新实际日期失败");
            }
        } catch (Exception e) {
            log.error("更新请假申请实际日期失败", e);
            return R.fail("更新请假申请实际日期失败：" + e.getMessage());
        }
    }
}