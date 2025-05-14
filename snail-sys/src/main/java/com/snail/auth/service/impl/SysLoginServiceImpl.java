package com.snail.auth.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.snail.auth.service.SysLoginService;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.sys.api.vo.UserVo;
import com.snail.sys.service.UserService;
import com.snial.common.core.constant.CacheConstants;
import com.snial.common.core.constant.Constants;
import com.snial.common.core.utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.Supplier;

/**
 * 登录
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Slf4j
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
     * @param userCode userCode
     * @param password password
     * @return java.lang.String
     * @since 1.0
     */
    @Override
    public String login(String userCode, String password) {
        UserVo userInfo = userService.getUserInfo(userCode);
        checkLogin(userCode, () -> !BCrypt.checkpw(password, userInfo.getPassWord()));
        return "assas";
    }



    /**
     * 登录校验
     */
    private void checkLogin( String userCode, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + userCode;
        String loginFail = Constants.LOGIN_FAIL;
        RedisUtils.setCacheObject("user",1);
        RedisUtils.setCacheObject(errorKey, RedisUtils.getCacheObject(errorKey));
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        System.out.println(errorNumber);
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        if (!supplier.get()) {
            throw new RuntimeException(MessageUtils.message(loginFail));
        }
    }
}
