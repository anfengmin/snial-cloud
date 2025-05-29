package com.snail.sys.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysNotice>>queryByPage(@RequestBody SysNoticePageDTO dto){
        return sysNoticeService.queryByPage(dto);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysNotice> queryById(@PathVariable("id") Long id) {
        return R.ok(sysNoticeService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(SysNotice sysNotice) {
        return R.ok(sysNoticeService.save(sysNotice));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(SysNotice sysNotice) {
        return R.ok(sysNoticeService.updateById(sysNotice));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(sysNoticeService.removeByIds(ids));
    }

}

