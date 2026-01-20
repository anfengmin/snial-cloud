package com.snail.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.dto.SysOperateLogPageDTO;
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
    
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysOperateLog>>queryByPage(@RequestBody SysOperateLogPageDTO dto){
        return sysOperateLogService.queryByPage(dto);
    }


    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysOperateLogService.removeByIds(ids));
    }

    /**
     * 清空操作日志记录
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public R<Void> clean() {
        sysOperateLogService.cleanOperateLog();
        return R.ok();
    }

}

