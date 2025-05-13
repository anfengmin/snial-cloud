package com.snail.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.sys.api.vo.UserVo;
import com.snail.sys.api.domain.User;
import com.snail.sys.service.UserService;
import com.snail.sys.dao.UserDao;
import com.snial.common.core.constant.UserConstants;
import com.snial.common.core.exception.user.UserException;
import com.snial.common.core.utils.MapstructUtils;
import com.snial.common.core.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
@Slf4j
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;

    /**
     * 根据用户账号获取用户信息
     *
     * @param userCode 用户账号
     * @return 用户信息
     */
    @Override
    public UserVo getUserInfo(String userCode) {
        User userInfo = this.lambdaQuery().eq(User::getUserCode, userCode).one();
        if (ObjectUtil.isEmpty(userInfo)) {
            throw new UserException("用户不存在", userCode);
        }
        if (UserConstants.USER_DISABLE.equals(userInfo.getStatus())) {
            throw new UserException("账号已禁用", userCode);
        }
        UserVo userVo = BeanUtil.copyProperties(userInfo, UserVo.class);

        log.info("userInfo:{}", userInfo);
        return userVo;
    }
}
