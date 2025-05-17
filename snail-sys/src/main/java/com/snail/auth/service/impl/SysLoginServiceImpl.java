package com.snail.auth.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.snail.auth.service.SysLoginService;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.vo.UserVo;
import com.snail.sys.service.UserService;
import com.snial.common.core.constant.CacheConstants;
import com.snial.common.core.constant.Constants;
import com.snial.common.core.utils.MessageUtils;
import com.snial.common.core.utils.ServletUtils;
import com.snial.common.core.utils.SpringUtils;
import com.snial.common.core.utils.ip.AddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
        LoginUtils.login(userInfo);
        return StpUtil.getTokenValue();
    }

    /**
     * logout
     *
     * @since 1.0
     */
    @Override
    public void logout() {
        try {
            UserVo loginUser = LoginUtils.getLoginUser();
            HttpServletRequest request = ServletUtils.getRequest();
            final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
            final String ip = ServletUtils.getClientIP(request);

            String address = AddressUtils.getRealAddressByIP(ip);
            // 获取客户端操作系统
            String os = userAgent.getOs().getName();
            // 获取客户端浏览器
            String browser = userAgent.getBrowser().getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                StpUtil.logout();
            } catch (NotLoginException ignored) {
                log.error("用户退出异常");
            }
        }
        // 封装对象
//        LogininforEvent logininfor = new LogininforEvent();
//        logininfor.setUserName(username);
//        logininfor.setIpaddr(ip);
//        logininfor.setLoginLocation(address);
//        logininfor.setBrowser(browser);
//        logininfor.setOs(os);
//        logininfor.setMsg(message);
//        // 日志状态
//        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
//            logininfor.setStatus(Constants.LOGIN_SUCCESS_STATUS);
//        } else if (Constants.LOGIN_FAIL.equals(status)) {
//            logininfor.setStatus(Constants.LOGIN_FAIL_STATUS);
//        }
//        SpringUtils.context().publishEvent(logininfor);
    }


    /**
     * checkLogin
     *
     * @param userCode userCode
     * @param supplier supplier
     * @since 1.0
     */
    private void checkLogin(String userCode, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + userCode;
        String loginFail = Constants.LOGIN_FAIL;
        RedisUtils.setCacheObject("user", 1);
        RedisUtils.setCacheObject(errorKey, RedisUtils.getCacheObject(errorKey));
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        System.out.println(errorNumber);
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        if (!supplier.get()) {
            throw new RuntimeException(MessageUtils.message(loginFail));
        }
    }
}
