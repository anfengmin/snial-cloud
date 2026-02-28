package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.service.SysOperateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 操作日志记录
 *
 * @author makejava
 * @since 2025-05-29 21:52:16
 */
@Api(tags = "操作日志记录")
@RestController
@RequestMapping("/v1/sysOperateLog")
public class SysOperateLogController {
    
    @Resource
    private SysOperateLogService sysOperateLogService;

    @SaCheckPermission("system:operatelog:list")
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysOperateLog>>queryByPage(@RequestBody SysOperateLogPageDTO dto){
        return R.ok(sysOperateLogService.queryByPage(dto));
    }


    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @SaCheckPermission("system:operatelog:remove")
    @DeleteMapping("delete")
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysOperateLogService.removeByIds(ids));
    }

    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @SaCheckPermission("system:operatelog:remove")
    @DeleteMapping("/clean")
    @ApiOperation(value = "清空操作日志记录")
    public R<Void> clean() {
        sysOperateLogService.cleanOperateLog();
        return R.ok();
    }

}

