package com.snail.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.utils.R;
import com.snail.job.core.complete.XxlJobCompleter;
import com.snail.job.core.model.XxlJobGroup;
import com.snail.job.core.model.XxlJobInfo;
import com.snail.job.core.model.XxlJobLog;
import com.snail.job.core.scheduler.XxlJobScheduler;
import com.snail.job.dao.XxlJobGroupDao;
import com.snail.job.dao.XxlJobInfoDao;
import com.snail.job.dao.XxlJobLogDao;
import com.xxl.job.core.biz.ExecutorBiz;
import com.xxl.job.core.biz.model.KillParam;
import com.xxl.job.core.biz.model.LogParam;
import com.xxl.job.core.biz.model.LogResult;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调度日志接口
 */
@RestController
@RequestMapping("/joblog")
public class JobLogController {

    private static final Logger logger = LoggerFactory.getLogger(JobLogController.class);

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobLogDao xxlJobLogDao;

    @GetMapping("/metadata")
    @SaCheckPermission("job:log:list")
    public R<Map<String, Object>> metadata(@RequestParam(required = false, defaultValue = "0") Integer jobId) {
        List<XxlJobGroup> jobGroupList = JobInfoController.filterJobGroupByRole(null, xxlJobGroupDao.findAll());

        Map<String, Object> data = new HashMap<>(2);
        data.put("jobGroupList", jobGroupList);
        if (jobId != null && jobId > 0) {
            data.put("jobInfo", xxlJobInfoDao.loadById(jobId));
        }
        return R.ok(data);
    }

    @RequestMapping("/getJobsByGroup")
    @ResponseBody
    @SaCheckPermission("job:log:list")
    public R<List<XxlJobInfo>> getJobsByGroup(int jobGroup) {
        return R.ok(xxlJobInfoDao.getJobsByGroup(jobGroup));
    }

    @RequestMapping("/pageList")
    @ResponseBody
    @SaCheckPermission("job:log:list")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        int jobGroup,
                                        int jobId,
                                        int logStatus,
                                        String filterTime) {

        Date triggerTimeStart = null;
        Date triggerTimeEnd = null;
        if (filterTime != null && filterTime.trim().length() > 0) {
            String[] temp = filterTime.split(" - ");
            if (temp.length == 2) {
                triggerTimeStart = DateUtil.parseDateTime(temp[0]);
                triggerTimeEnd = DateUtil.parseDateTime(temp[1]);
            }
        }

        List<XxlJobLog> list = xxlJobLogDao.pageList(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);
        int listCount = xxlJobLogDao.pageListCount(start, length, jobGroup, jobId, triggerTimeStart, triggerTimeEnd, logStatus);

        Map<String, Object> maps = new HashMap<>(3);
        maps.put("recordsTotal", listCount);
        maps.put("recordsFiltered", listCount);
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/queryByPage")
    @ResponseBody
    @SaCheckPermission("job:log:list")
    public R<Map<String, Object>> queryByPage(@RequestParam(required = false, defaultValue = "1") int current,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              @RequestParam(required = false, defaultValue = "-1") int jobGroup,
                                              @RequestParam(required = false, defaultValue = "0") int jobId,
                                              @RequestParam(required = false, defaultValue = "-1") int logStatus,
                                              String filterTime) {
        int start = Math.max((current - 1) * size, 0);
        Map<String, Object> raw = pageList(start, size, jobGroup, jobId, logStatus, filterTime);

        Map<String, Object> result = new HashMap<>(4);
        result.put("records", raw.getOrDefault("data", java.util.Collections.emptyList()));
        result.put("current", current);
        result.put("size", size);
        result.put("total", raw.getOrDefault("recordsTotal", 0));
        return R.ok(result);
    }

    @RequestMapping("/logDetailCat")
    @ResponseBody
    @SaCheckPermission("job:log:detail")
    public R<LogResult> logDetailCat(long logId, int fromLineNum) {
        try {
            XxlJobLog jobLog = xxlJobLogDao.load(logId);
            if (jobLog == null) {
                return R.fail("日志不存在");
            }

            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(jobLog.getExecutorAddress());
            ReturnT<LogResult> logResult = executorBiz.log(new LogParam(jobLog.getTriggerTime().getTime(), logId, fromLineNum));

            if (logResult.getContent() != null && logResult.getContent().getFromLineNum() > logResult.getContent().getToLineNum()) {
                if (jobLog.getHandleCode() > 0) {
                    logResult.getContent().setEnd(true);
                }
            }

            if (logResult.getContent() != null && StringUtils.hasText(logResult.getContent().getLogContent())) {
                String newLogContent = HtmlUtils.htmlEscape(logResult.getContent().getLogContent(), "UTF-8");
                logResult.getContent().setLogContent(newLogContent);
            }

            return logResult.getCode() == ReturnT.SUCCESS_CODE
                    ? R.ok(logResult.getContent())
                    : R.fail(logResult.getCode(), logResult.getMsg());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return R.fail(e.getMessage());
        }
    }

    @RequestMapping("/logKill")
    @ResponseBody
    @SaCheckPermission("job:log:kill")
    public R<String> logKill(int id) {
        XxlJobLog log = xxlJobLogDao.load(id);
        XxlJobInfo jobInfo = xxlJobInfoDao.loadById(log.getJobId());
        if (jobInfo == null) {
            return R.fail("任务不存在");
        }
        if (ReturnT.SUCCESS_CODE != log.getTriggerCode()) {
            return R.fail("当前日志不支持终止执行");
        }

        ReturnT<String> runResult;
        try {
            ExecutorBiz executorBiz = XxlJobScheduler.getExecutorBiz(log.getExecutorAddress());
            runResult = executorBiz.kill(new KillParam(jobInfo.getId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = new ReturnT<>(500, e.getMessage());
        }

        if (ReturnT.SUCCESS_CODE == runResult.getCode()) {
            log.setHandleCode(ReturnT.FAIL_CODE);
            log.setHandleMsg("人工终止执行:" + (runResult.getMsg() != null ? runResult.getMsg() : ""));
            log.setHandleTime(new Date());
            XxlJobCompleter.updateHandleInfoAndFinish(log);
            return R.ok(runResult.getMsg());
        }
        return R.fail(runResult.getMsg());
    }

    @RequestMapping("/clearLog")
    @ResponseBody
    @SaCheckPermission("job:log:clear")
    public R<String> clearLog(int jobGroup, int jobId, int type) {
        Date clearBeforeTime = null;
        int clearBeforeNum = 0;
        if (type == 1) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -1);
        } else if (type == 2) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -3);
        } else if (type == 3) {
            clearBeforeTime = DateUtil.addMonths(new Date(), -6);
        } else if (type == 4) {
            clearBeforeTime = DateUtil.addYears(new Date(), -1);
        } else if (type == 5) {
            clearBeforeNum = 1000;
        } else if (type == 6) {
            clearBeforeNum = 10000;
        } else if (type == 7) {
            clearBeforeNum = 30000;
        } else if (type == 8) {
            clearBeforeNum = 100000;
        } else if (type == 9) {
            clearBeforeNum = 0;
        } else {
            return R.fail("清理类型无效");
        }

        List<Long> logIds;
        do {
            logIds = xxlJobLogDao.findClearLogIds(jobGroup, jobId, clearBeforeTime, clearBeforeNum, 1000);
            if (logIds != null && !logIds.isEmpty()) {
                xxlJobLogDao.clearLog(logIds);
            }
        } while (logIds != null && !logIds.isEmpty());

        return R.ok("清理成功");
    }
}
