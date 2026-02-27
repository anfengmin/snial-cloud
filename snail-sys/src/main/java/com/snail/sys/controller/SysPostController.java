package com.snail.sys.controller;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.log.annotation.Log;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.domain.SysPost;
import com.snail.sys.service.SysPostService;
import com.snail.sys.dto.SysPostPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;
import javax.annotation.Resource;
import java.util.List;

/**
 * 岗位信息
 *
 * @author makejava
 * @since 2025-05-30 23:05:39
 */
@Api(tags = "岗位信息")
@RestController
@RequestMapping("/v1/sysPost")
public class SysPostController {
    
    @Resource
    private SysPostService sysPostService;

    @SaCheckPermission("system:post:list")
    @PostMapping("queryByPage")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public R<Page<SysPost>>queryByPage(@RequestBody SysPostPageDTO dto){
        return R.ok(sysPostService.queryByPage(dto));
    }

    @SaCheckPermission("system:post:query")
    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<SysPost> queryById(@PathVariable("id") Long id) {
        return R.ok(sysPostService.getById(id));
    }

    @SaCheckPermission("system:post:add")
    @Log(title = "岗位管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(@RequestBody SysPost sysPost) {
        if (sysPostService.checkPostNameExists(sysPost)) {
            return R.fail("新增岗位'" + sysPost.getPostName() + "'失败，岗位名称已存在");
        } else if (sysPostService.checkPostCodeExists(sysPost)) {
            return R.fail("新增岗位'" + sysPost.getPostName() + "'失败，岗位编码已存在");
        }
        return R.ok(sysPostService.save(sysPost));
    }

    @SaCheckPermission("system:post:edit")
    @Log(title = "岗位管理", businessType = BusinessType.UPDATE)
    @PutMapping("edit")
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(@RequestBody SysPost sysPost) {
        if (sysPostService.checkPostNameExists(sysPost)) {
            return R.fail("修改岗位'" + sysPost.getPostName() + "'失败，岗位名称已存在");
        } else if (sysPostService.checkPostCodeExists(sysPost)) {
            return R.fail("修改岗位'" + sysPost.getPostName() + "'失败，岗位编码已存在");
        }
        return R.ok(sysPostService.updateById(sysPost));
    }

    @SaCheckPermission("system:post:remove")
    @Log(title = "岗位管理", businessType = BusinessType.DELETE)
    @DeleteMapping("delete")
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysPostService.removeByIds(ids));
    }

    @GetMapping("/optionselect")
    @ApiOperation(value = "获取岗位选择框列表")
    public R<List<SysPost>> optionselect() {
        SysPost post = new SysPost();
        post.setStatus(UserConstants.POST_NORMAL);
        List<SysPost> posts = sysPostService.selectPostList(post);
        return R.ok(posts);
    }
}

