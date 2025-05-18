package com.snail.auth.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.vo.SysUserVo;
import com.snail.sys.service.SysUserService;
import com.snial.common.core.constant.CacheConstants;
import com.snial.common.core.constant.Constants;
import com.snial.common.core.enums.LoginType;
import com.snial.common.core.exception.user.UserException;
import com.snial.common.core.utils.ServletUtils;
import com.snial.common.core.utils.ip.AddressUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
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
public class SysLoginService {

    @Resource
    private SysUserService sysUserService;

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
    public String login(String userCode, String password) {
        SysUserVo userInfo = sysUserService.getUserInfo(userCode);
        checkLogin(LoginType.PASSWORD, userCode, () -> !BCrypt.checkpw(password, userInfo.getPassWord()));
        LoginUtils.login(userInfo);
        return StpUtil.getTokenValue();
    }

    /**
     * logout
     *
     * @since 1.0
     */
    public void logout() {
        try {
            SysUserVo loginUser = LoginUtils.getLoginUser();
            HttpServletRequest request = ServletUtils.getRequest();
            assert request != null;
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
    private void checkLogin(LoginType loginType,String userCode, Supplier<Boolean> supplier) {
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + userCode;
        String loginFail = Constants.LOGIN_FAIL;
        RedisUtils.setCacheObject(errorKey, RedisUtils.getCacheObject(errorKey));
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }


}


