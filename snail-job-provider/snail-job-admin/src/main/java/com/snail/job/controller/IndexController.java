package com.snail.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.utils.R;
import com.snail.job.service.XxlJobService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 调度中心概览接口
 */
@RestController
public class IndexController {

    @Resource
    private XxlJobService xxlJobService;

    @GetMapping("/")
    @SaCheckPermission("job:dashboard:list")
    public R<Map<String, Object>> dashboardInfo() {
        return R.ok(xxlJobService.dashboardInfo());
    }

    @RequestMapping("/dashboardInfo")
    @SaCheckPermission("job:dashboard:list")
    public R<Map<String, Object>> dashboardSummary() {
        return R.ok(xxlJobService.dashboardInfo());
    }

    @RequestMapping("/chartInfo")
    @SaCheckPermission("job:dashboard:list")
    public R<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        return R.ok(xxlJobService.chartInfo(startDate, endDate).getContent());
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
