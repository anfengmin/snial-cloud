package com.snail.auth.listener;

import cn.dev33.satoken.config.SaTokenConfig;
import cn.dev33.satoken.listener.SaTokenListener;
import cn.dev33.satoken.stp.parameter.SaLoginParameter;
import cn.hutool.core.util.ObjectUtil;
import com.snail.auth.model.LoginContextInfo;
import com.snail.auth.util.LoginContextResolver;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysUserOnline;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 用户行为 侦听器的实现
 *
 * @author Anfm
 * Created time 2026/2/28
 * @since 1.0
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserActionListener implements SaTokenListener {

    private final SaTokenConfig tokenConfig;

    /**
     * 每次登录时触发
     *
     * @param loginType      账号类别
     * @param loginId        账号id
     * @param tokenValue     本次登录产生的 token 值
     * @param loginParameter 登录参数
     */
    @Override
    public void doLogin(String loginType, Object loginId, String tokenValue, SaLoginParameter loginParameter) {
        // 注意：loginId 当前实现使用的是 userCode（如 admin），不包含 sys_user/app_user 前缀，
        // 因此不能从 loginId 解析 UserType。应以登录态中的 LoginUser.userType 为准，并提供兜底。
        LoginContextInfo loginContextInfo = LoginContextResolver.resolveCurrentRequest();
        LoginUser user = LoginUtils.getLoginUser();
        SysUserOnline userOnline = new SysUserOnline();
        userOnline.setUserId(user.getId());
        userOnline.setIpaddr(loginContextInfo.getIp());
        userOnline.setLoginLocation(loginContextInfo.getLoginLocation());
        userOnline.setBrowser(loginContextInfo.getBrowser());
        userOnline.setOs(loginContextInfo.getOs());
        userOnline.setClientType(loginContextInfo.getClientType());
        userOnline.setDeviceId(loginContextInfo.getDeviceId());
        userOnline.setDeviceName(loginContextInfo.getDeviceName());
        userOnline.setLoginTime(System.currentTimeMillis());
        userOnline.setTokenId(tokenValue);
        userOnline.setUserName(user.getUserName());
        userOnline.setUserCode(user.getUserCode());
        if (ObjectUtil.isNotNull(user.getDeptName())) {
            userOnline.setDeptName(user.getDeptName());
        }
        if(tokenConfig.getTimeout() == -1) {
            RedisUtils.setCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue, userOnline);
        } else {
            RedisUtils.setCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue, userOnline, Duration.ofSeconds(tokenConfig.getTimeout()));
        }
        log.info("user doLogin, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次注销时触发
     */
    @Override
    public void doLogout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doLogout, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被踢下线时触发
     */
    @Override
    public void doKickout(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doLogoutByLoginId, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被顶下线时触发
     */
    @Override
    public void doReplaced(String loginType, Object loginId, String tokenValue) {
        RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue);
        log.info("user doReplaced, useId:{}, token:{}", loginId, tokenValue);
    }

    /**
     * 每次被封禁时触发
     */
    @Override
    public void doDisable(String loginType, Object loginId, String service, int level, long disableTime) {
    }

    /**
     * 每次被解封时触发
     */
    @Override
    public void doUntieDisable(String loginType, Object loginId, String service) {
    }

    /**
     * 每次打开二级认证时触发
     */
    @Override
    public void doOpenSafe(String loginType, String tokenValue, String service, long safeTime) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCloseSafe(String loginType, String tokenValue, String service) {
    }

    /**
     * 每次创建Session时触发
     */
    @Override
    public void doCreateSession(String id) {
    }

    /**
     * 每次注销Session时触发
     */
    @Override
    public void doLogoutSession(String id) {
    }

    /**
     * 每次Token续期时触发
     */
    @Override
    public void doRenewTimeout(String tokenValue, Object loginId, long timeout) {
    }

}
