package com.snail.sys.controller;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysOperateLog;
import com.snail.sys.service.SysOperateLogService;
import com.snail.sys.dto.SysOperateLogPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;
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

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysOperateLog> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOperateLogService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysOperateLog sysOperateLog) {
        return R.ok(sysOperateLogService.save(sysOperateLog));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysOperateLog sysOperateLog) {
        return R.ok(sysOperateLogService.updateById(sysOperateLog));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysOperateLogService.removeByIds(ids));
    }

}

