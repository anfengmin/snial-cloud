package com.snail.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.utils.R;
import com.snail.job.core.exception.XxlJobException;
import com.snail.job.core.model.XxlJobGroup;
import com.snail.job.core.model.XxlJobInfo;
import com.snail.job.core.route.ExecutorRouteStrategyEnum;
import com.snail.job.core.scheduler.MisfireStrategyEnum;
import com.snail.job.core.scheduler.ScheduleTypeEnum;
import com.snail.job.core.thread.JobScheduleHelper;
import com.snail.job.core.util.I18nUtil;
import com.snail.job.controller.support.JobResponseUtils;
import com.snail.job.dao.XxlJobGroupDao;
import com.snail.job.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务管理接口
 */
@RestController
@RequestMapping("/jobinfo")
public class JobInfoController {

    private static final Logger logger = LoggerFactory.getLogger(JobInfoController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobService xxlJobService;

    @GetMapping("/metadata")
    @SaCheckPermission("job:info:list")
    public R<Map<String, Object>> metadata(@RequestParam(required = false, defaultValue = "-1") int jobGroup) {
        List<XxlJobGroup> jobGroupList = filterJobGroupByRole(null, xxlJobGroupDao.findAll());
        if (jobGroupList.isEmpty()) {
            throw new XxlJobException(I18nUtil.getString("jobgroup_empty"));
        }

        Map<String, Object> data = new HashMap<>(7);
        data.put("jobGroup", jobGroup);
        data.put("jobGroupList", jobGroupList);
        data.put("executorRouteStrategies", ExecutorRouteStrategyEnum.values());
        data.put("glueTypes", GlueTypeEnum.values());
        data.put("executorBlockStrategies", ExecutorBlockStrategyEnum.values());
        data.put("scheduleTypes", ScheduleTypeEnum.values());
        data.put("misfireStrategies", MisfireStrategyEnum.values());
        return R.ok(data);
    }

    public static List<XxlJobGroup> filterJobGroupByRole(HttpServletRequest request, List<XxlJobGroup> jobGroupListAll) {
        if (jobGroupListAll == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(jobGroupListAll);
    }

    public static void validPermission(HttpServletRequest request, int jobGroup) {
        // snail-job-admin 已改为使用 sys_menu + Sa-Token 控制访问范围，
        // 不再使用 xxl_job_user 维度的执行器分组过滤。
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @SaCheckPermission("job:info:list")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        int jobGroup,
                                        int triggerStatus,
                                        String jobDesc,
                                        String executorHandler,
                                        String author) {

        return xxlJobService.pageList(start, length, jobGroup, triggerStatus, jobDesc, executorHandler, author);
    }

    @RequestMapping("/queryByPage")
    @ResponseBody
    @SaCheckPermission("job:info:list")
    public R<Map<String, Object>> queryByPage(@RequestParam(required = false, defaultValue = "1") int current,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(required = false, defaultValue = "-1") int jobGroup,
                                              @RequestParam(required = false, defaultValue = "-1") int triggerStatus,
                                              String jobDesc,
                                              String executorHandler,
                                              String author) {
        int start = Math.max((current - 1) * size, 0);
        Map<String, Object> result = xxlJobService.pageList(start, size, jobGroup, triggerStatus, jobDesc, executorHandler, author);
        return R.ok(buildPageResult(result, current, size));
    }

    @RequestMapping("/add")
    @ResponseBody
    @SaCheckPermission("job:info:add")
    public R<String> add(XxlJobInfo jobInfo) {
        return JobResponseUtils.fromReturnT(xxlJobService.add(jobInfo));
    }

    @RequestMapping("/update")
    @ResponseBody
    @SaCheckPermission("job:info:edit")
    public R<String> update(XxlJobInfo jobInfo) {
        return JobResponseUtils.fromReturnT(xxlJobService.update(jobInfo));
    }

    @RequestMapping("/remove")
    @ResponseBody
    @SaCheckPermission("job:info:remove")
    public R<String> remove(int id) {
        return JobResponseUtils.fromReturnT(xxlJobService.remove(id));
    }

    @RequestMapping("/stop")
    @ResponseBody
    @SaCheckPermission("job:info:stop")
    public R<String> pause(int id) {
        return JobResponseUtils.fromReturnT(xxlJobService.stop(id));
    }

    @RequestMapping("/start")
    @ResponseBody
    @SaCheckPermission("job:info:start")
    public R<String> start(int id) {
        return JobResponseUtils.fromReturnT(xxlJobService.start(id));
    }

    @RequestMapping("/trigger")
    @ResponseBody
    @SaCheckPermission("job:info:trigger")
    public R<String> triggerJob(int id, String executorParam, String addressList) {
        return JobResponseUtils.fromReturnT(xxlJobService.trigger(id, executorParam, addressList));
    }

    @RequestMapping("/nextTriggerTime")
    @ResponseBody
    @SaCheckPermission("job:info:nextTriggerTime")
    public R<List<String>> nextTriggerTime(String scheduleType, String scheduleConf) {
        XxlJobInfo paramXxlJobInfo = new XxlJobInfo();
        paramXxlJobInfo.setScheduleType(scheduleType);
        paramXxlJobInfo.setScheduleConf(scheduleConf);

        List<String> result = new ArrayList<>();
        try {
            java.util.Date lastTime = new java.util.Date();
            for (int i = 0; i < 5; i++) {
                lastTime = JobScheduleHelper.generateNextValidTime(paramXxlJobInfo, lastTime);
                if (lastTime == null) {
                    break;
                }
                result.add(DateUtil.formatDateTime(lastTime));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return R.fail(I18nUtil.getString("schedule_type") + I18nUtil.getString("system_unvalid") + e.getMessage());
        }
        return R.ok(result);
    }

    private Map<String, Object> buildPageResult(Map<String, Object> source, int current, int size) {
        List<?> records = (List<?>) source.getOrDefault("data", new ArrayList<>());
        int total = ((Number) source.getOrDefault("recordsTotal", 0)).intValue();
        Map<String, Object> result = new HashMap<>(4);
        result.put("records", records);
        result.put("current", current);
        result.put("size", size);
        result.put("total", total);
        return result;
    }
}
