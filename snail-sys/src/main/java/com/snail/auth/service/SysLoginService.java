package com.snail.auth.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.snail.auth.form.RegisterBody;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.core.constant.Constants;
import com.snail.common.core.enums.LoginType;
import com.snail.common.core.exception.user.UserException;
import com.snail.common.core.utils.MessageUtils;
import com.snail.common.core.utils.ServletUtils;
import com.snail.common.core.utils.SpringUtils;
import com.snail.common.core.utils.ip.AddressUtils;
import com.snail.common.log.event.LoginInfoEvent;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.satoken.vo.LoginUser;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.service.SysUserService;
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
     * @param passWord passWord
     * @return java.lang.String
     * @since 1.0
     */
    public String login(String userCode, String passWord) {
        LoginUser userInfo = sysUserService.getUserInfo(userCode);
        checkLogin(LoginType.PASSWORD, userCode, () -> !BCrypt.checkpw(passWord, userInfo.getPassWord()));
        LoginUtils.login(userInfo);
        recordLoginInfo(userInfo.getUserCode(), Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success"));

        return StpUtil.getTokenValue();
    }

    /**
     * 用户登出
     *
     * @since 1.0
     */
    public void logout() {
        try {
            // 获取当前登录用户并记录登出信息
            LoginUser loginUser = LoginUtils.getLoginUser();
            if (loginUser != null) {
                recordLoginInfo(loginUser.getUserCode(), Constants.LOGOUT, MessageUtils.message("user.logout.success"));
            }
            // 执行登出操作
            StpUtil.logout();
        } catch (NotLoginException e) {
            log.warn("用户登出时未检测到有效登录: {}", e.getMessage());
        } catch (Exception e) {
            log.error("用户登出异常: ", e);
        }
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
                recordLoginInfo(userCode, loginFail, MessageUtils.message(loginType.getRetryLimitExceed(), maxRetryCount, lockTime));
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                recordLoginInfo(userCode, loginFail, MessageUtils.message(loginType.getRetryLimitCount(), errorNumber));
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }
        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }


    /**
     * register
     *
     * @param registerBody registerBody
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    public void register(RegisterBody registerBody) {
        // 校验用户类型是否存在
        // 注册用户信息
        SysUser sysUser = new SysUser();
        sysUser.setUserCode(registerBody.getUserCode());
        sysUser.setUserName(registerBody.getUserName());
        sysUser.setNickName(registerBody.getUserName());
        sysUser.setPassWord(BCrypt.hashpw(registerBody.getPassWord()));
        sysUser.setCreateBy(registerBody.getUserCode());
        sysUser.setUpdateBy(registerBody.getUserCode());
        sysUser.setUserType(registerBody.getUserType());
        boolean regFlag = sysUserService.registerUserInfo(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLoginInfo(registerBody.getUserCode(), Constants.REGISTER, MessageUtils.message("user.register.success"));


    }

    /**
     * 记录登录信息
     *
     * @param username username
     * @param status status
     * @param message message
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    public void recordLoginInfo(String username, String status, String message) {
        HttpServletRequest request = ServletUtils.getRequest();
        assert request != null;
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        final String ip = ServletUtils.getClientIP(request);

        String address = AddressUtils.getRealAddressByIP(ip);
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        LoginInfoEvent loginInfo = new LoginInfoEvent();
        loginInfo.setUserName(username);
        loginInfo.setIpaddr(ip);
        loginInfo.setLoginLocation(address);
        loginInfo.setBrowser(browser);
        loginInfo.setOs(os);
        loginInfo.setMsg(message);
        // 日志状态
        if (StrUtil.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
        SpringUtils.context().publishEvent(loginInfo);
    }

}


