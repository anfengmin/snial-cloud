package com.snail.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.sys.domain.User;
import com.snail.sys.service.UserService;
import com.snail.sys.dao.UserDao;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;


}
