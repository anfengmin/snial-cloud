package com.snail.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.snail.auth.service.SysLoginService;
import com.snail.sys.domain.User;
import com.snail.sys.service.UserService;
import common.core.constant.CacheConstants;
import common.core.constant.Constants;
import common.core.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Service
public class SysLoginServiceImpl implements SysLoginService {

    @Resource
    private UserService userService;

    /**
     * 密码最大错误次数(默认5次)
     */
    @Value("${user.password.maxRetryCount:5}")
    private Integer maxRetryCount;
    /**
     * 密码锁定时间（默认10分钟）
     */
    @Value("${user.password.lockTime:10}")
    private Integer lockTime;
    /**
     * login
     *
     * @param username username
     * @param password password
     * @return java.lang.String
     * @since 1.0
     */
    @Override
    public String login(String username, String password) {
        User userInfo = userService.lambdaQuery().eq(User::getUserName, username).one();
        checkLogin(username, () -> !BCrypt.checkpw(password, userInfo.getPassword()));
        return "assas";
    }

    /**
     * 登录校验
     */
    private void checkLogin( String username, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        if (!supplier.get()) {
            throw new RuntimeException(MessageUtils.message(loginFail));
        }
    }
}
