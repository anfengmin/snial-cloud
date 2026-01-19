package com.snail.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.sys.domain.SysLoginInfo;
import com.snail.sys.dto.SysLogPageDTO;
import com.snail.sys.service.SysLoginInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统访问记录
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
@Api(tags = "系统访问记录")
@RestController
@RequestMapping("/v1/loginfo")
public class SysLoginInfoController {

    @Resource
    private SysLoginInfoService sysLoginInfoService;


    @GetMapping("/list")
    @ApiOperation(value = "获取系统访问记录列表")
    public R<Page<SysLoginInfo>> list(SysLogPageDTO dto) {
        return sysLoginInfoService.queryByPage(dto);
    }


    @DeleteMapping
    @ApiOperation(value = "删除系统访问记录")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysLoginInfoService.removeByIds(ids));
    }

    @DeleteMapping("/clean")
    public R<Void> clean() {
        sysLoginInfoService.cleanLogInfo();
        return R.ok();
    }

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

