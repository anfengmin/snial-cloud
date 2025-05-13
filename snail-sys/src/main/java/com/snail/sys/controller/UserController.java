package com.snail.sys.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.snail.sys.api.domain.User;
import com.snail.sys.service.UserService;
import org.springframework.web.bind.annotation.*;
import com.snial.common.core.utils.R;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户信息表(User)表控制层
 *
 * @author makejava
 * @since 2025-05-11 20:53:38
 */
@Api(tags = "用户信息表")
@RestController
@RequestMapping("/v1/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("{id}")
    @ApiOperation(value = "主键查询")
    public R<User> queryById(@PathVariable("id") Long id) {
        return R.ok(userService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增数据")
    public R<Boolean> add(User user) {
        return R.ok(userService.save(user));
    }

    @PutMapping
    @ApiOperation(value = "编辑数据")
    public R<Boolean> edit(User user) {
        return R.ok(userService.updateById(user));
    }

    @DeleteMapping
    @ApiOperation(value = "删除数据")
    public R<Boolean> deleteById(@RequestParam("ids") List<Long> ids) {
        return R.ok(userService.removeByIds(ids));
    }

}

