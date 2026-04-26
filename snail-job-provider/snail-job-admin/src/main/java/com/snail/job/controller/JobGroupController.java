package com.snail.job.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.utils.R;
import com.snail.job.core.model.XxlJobGroup;
import com.snail.job.core.model.XxlJobRegistry;
import com.snail.job.core.util.I18nUtil;
import com.snail.job.controller.support.JobResponseUtils;
import com.snail.job.dao.XxlJobGroupDao;
import com.snail.job.dao.XxlJobInfoDao;
import com.snail.job.dao.XxlJobRegistryDao;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.enums.RegistryConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 执行器管理接口
 */
@RestController
@RequestMapping("/jobgroup")
public class JobGroupController {

    @Resource
    public XxlJobInfoDao xxlJobInfoDao;
    @Resource
    public XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobRegistryDao xxlJobRegistryDao;

    @RequestMapping("/pageList")
    @ResponseBody
    @SaCheckPermission("job:group:list")
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String appname,
                                        String title) {

        List<XxlJobGroup> list = xxlJobGroupDao.pageList(start, length, appname, title);
        int listCount = xxlJobGroupDao.pageListCount(start, length, appname, title);

        Map<String, Object> maps = new HashMap<>(3);
        maps.put("recordsTotal", listCount);
        maps.put("recordsFiltered", listCount);
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/queryByPage")
    @ResponseBody
    @SaCheckPermission("job:group:list")
    public R<Map<String, Object>> queryByPage(@RequestParam(required = false, defaultValue = "1") int current,
                                              @RequestParam(required = false, defaultValue = "10") int size,
                                              String appname,
                                              String title) {
        int start = Math.max((current - 1) * size, 0);
        List<XxlJobGroup> list = xxlJobGroupDao.pageList(start, size, appname, title);
        int total = xxlJobGroupDao.pageListCount(start, size, appname, title);

        Map<String, Object> result = new HashMap<>(4);
        result.put("records", list);
        result.put("current", current);
        result.put("size", size);
        result.put("total", total);
        return R.ok(result);
    }

    @RequestMapping("/save")
    @ResponseBody
    @SaCheckPermission("job:group:add")
    public R<String> save(XxlJobGroup xxlJobGroup) {
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().isEmpty()) {
            return R.fail(I18nUtil.getString("system_please_input") + "AppName");
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return R.fail(I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getAppname().contains(">") || xxlJobGroup.getAppname().contains("<")) {
            return R.fail("AppName" + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().isEmpty()) {
            return R.fail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title"));
        }
        if (xxlJobGroup.getTitle().contains(">") || xxlJobGroup.getTitle().contains("<")) {
            return R.fail(I18nUtil.getString("jobgroup_field_title") + I18nUtil.getString("system_unvalid"));
        }
        if (xxlJobGroup.getAddressType() != 0) {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().isEmpty()) {
                return R.fail(I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            if (xxlJobGroup.getAddressList().contains(">") || xxlJobGroup.getAddressList().contains("<")) {
                return R.fail(I18nUtil.getString("jobgroup_field_registryList") + I18nUtil.getString("system_unvalid"));
            }

            String[] addresses = xxlJobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().isEmpty()) {
                    return R.fail(I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        xxlJobGroup.setUpdateTime(new Date());
        int ret = xxlJobGroupDao.save(xxlJobGroup);
        return ret > 0 ? R.ok("保存成功") : R.fail("保存失败");
    }

    @RequestMapping("/update")
    @ResponseBody
    @SaCheckPermission("job:group:edit")
    public R<String> update(XxlJobGroup xxlJobGroup) {
        if (xxlJobGroup.getAppname() == null || xxlJobGroup.getAppname().trim().isEmpty()) {
            return R.fail(I18nUtil.getString("system_please_input") + "AppName");
        }
        if (xxlJobGroup.getAppname().length() < 4 || xxlJobGroup.getAppname().length() > 64) {
            return R.fail(I18nUtil.getString("jobgroup_field_appname_length"));
        }
        if (xxlJobGroup.getTitle() == null || xxlJobGroup.getTitle().trim().isEmpty()) {
            return R.fail(I18nUtil.getString("system_please_input") + I18nUtil.getString("jobgroup_field_title"));
        }
        if (xxlJobGroup.getAddressType() == 0) {
            List<String> registryList = findRegistryByAppName(xxlJobGroup.getAppname());
            String addressListStr = null;
            if (registryList != null && !registryList.isEmpty()) {
                Collections.sort(registryList);
                addressListStr = String.join(",", registryList);
            }
            xxlJobGroup.setAddressList(addressListStr);
        } else {
            if (xxlJobGroup.getAddressList() == null || xxlJobGroup.getAddressList().trim().isEmpty()) {
                return R.fail(I18nUtil.getString("jobgroup_field_addressType_limit"));
            }
            String[] addresses = xxlJobGroup.getAddressList().split(",");
            for (String item : addresses) {
                if (item == null || item.trim().isEmpty()) {
                    return R.fail(I18nUtil.getString("jobgroup_field_registryList_unvalid"));
                }
            }
        }

        xxlJobGroup.setUpdateTime(new Date());
        int ret = xxlJobGroupDao.update(xxlJobGroup);
        return ret > 0 ? R.ok("更新成功") : R.fail("更新失败");
    }

    private List<String> findRegistryByAppName(String appnameParam) {
        HashMap<String, List<String>> appAddressMap = new HashMap<>();
        List<XxlJobRegistry> list = xxlJobRegistryDao.findAll(RegistryConfig.DEAD_TIMEOUT, new Date());
        if (list != null) {
            for (XxlJobRegistry item : list) {
                if (RegistryConfig.RegistType.EXECUTOR.name().equals(item.getRegistryGroup())) {
                    String appname = item.getRegistryKey();
                    List<String> registryList = appAddressMap.computeIfAbsent(appname, key -> new ArrayList<>());
                    if (!registryList.contains(item.getRegistryValue())) {
                        registryList.add(item.getRegistryValue());
                    }
                }
            }
        }
        return appAddressMap.get(appnameParam);
    }

    @RequestMapping("/remove")
    @ResponseBody
    @SaCheckPermission("job:group:remove")
    public R<String> remove(int id) {
        int count = xxlJobInfoDao.pageListCount(0, 10, id, -1, null, null, null);
        if (count > 0) {
            return R.fail(I18nUtil.getString("jobgroup_del_limit_0"));
        }

        List<XxlJobGroup> allList = xxlJobGroupDao.findAll();
        if (allList.size() == 1) {
            return R.fail(I18nUtil.getString("jobgroup_del_limit_1"));
        }

        int ret = xxlJobGroupDao.remove(id);
        return ret > 0 ? R.ok("删除成功") : R.fail("删除失败");
    }

    @RequestMapping("/loadById")
    @ResponseBody
    @SaCheckPermission("job:group:detail")
    public R<XxlJobGroup> loadById(int id) {
        XxlJobGroup jobGroup = xxlJobGroupDao.load(id);
        return jobGroup != null ? R.ok(jobGroup) : R.fail("执行器不存在");
    }
}
