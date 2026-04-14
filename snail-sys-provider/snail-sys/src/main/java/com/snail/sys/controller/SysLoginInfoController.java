package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.log.domain.SysLoginInfo;
import com.snail.sys.dto.SysLogPageDTO;
import com.snail.sys.service.SysLoginInfoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
@Tag(name = "系统访问记录")
@RestController
@RequestMapping("/loginfo")
public class SysLoginInfoController {

    @Resource
    private SysLoginInfoService sysLoginInfoService;


    @SaCheckPermission("system:loginfo:list")
    @GetMapping("/list")
    @Operation(summary = "获取系统访问记录列表")
    public R<Page<SysLoginInfo>> list(SysLogPageDTO dto) {
        return R.ok(sysLoginInfoService.queryByPage(dto));
    }


    @SaCheckPermission("system:loginfo:remove")
    @PostMapping("/remove")
    @Operation(summary = "删除系统访问记录")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysLoginInfoService.removeByIds(ids));
    }

    @SaCheckPermission("system:loginfo:remove")
    @DeleteMapping("/clean")
    public R<Void> clean() {
        sysLoginInfoService.cleanLogInfo();
        return R.ok();
    }

    @SaCheckPermission("system:loginfo:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    public R<Void> unlock(@PathVariable("userName") String userName) {
        String loginName = CacheConstants.PWD_ERR_CNT_KEY + userName;
        if (RedisUtils.hasKey(loginName)) {
            RedisUtils.deleteObject(loginName);
        }
        return R.ok();
    }

}
