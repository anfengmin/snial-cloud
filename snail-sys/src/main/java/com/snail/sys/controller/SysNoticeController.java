package com.snail.sys.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysNotice;
import com.snail.sys.service.SysNoticeService;
import com.snail.sys.dto.SysNoticePageDTO;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;
import javax.annotation.Resource;
import java.util.List;

/**
 * 通知公告
 *
 * @author makejava
 * @since 2025-05-29 21:50:39
 */
@Api(tags = "通知公告")
@RestController
@RequestMapping("/v1/sysNotice")
public class SysNoticeController {
    
    @Resource
    private SysNoticeService sysNoticeService;

    @SaCheckPermission("system:notice:list")
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysNotice>>queryByPage(@RequestBody SysNoticePageDTO dto){
        return R.ok(sysNoticeService.queryByPage(dto));
    }

    @SaCheckPermission("system:notice:query")
    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysNotice> queryById(@PathVariable("id") Long id) {
        return R.ok(sysNoticeService.getById(id));
    }

    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysNotice sysNotice) {
        return R.ok(sysNoticeService.save(sysNotice));
    }

    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysNotice sysNotice) {
        return R.ok(sysNoticeService.updateById(sysNotice));
    }

    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @PostMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysNoticeService.removeByIds(ids));
    }

}

