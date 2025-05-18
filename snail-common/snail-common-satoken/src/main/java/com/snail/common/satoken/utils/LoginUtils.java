package com.snail.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.snail.sys.api.vo.SysUserVo;
import com.snial.common.core.constant.CacheConstants;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUtils {

    public static final String LOGIN_USER_KEY = "loginUser";
    public static final String USER_KEY = "userId";

    /**
     * 登录系统
     *
     * @param sysUserVo 登录用户信息
     */
    public static void login(SysUserVo sysUserVo) {
        loginByDevice(sysUserVo);
    }

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param sysUserVo 登录用户信息
     */
    public static void loginByDevice(SysUserVo sysUserVo) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, sysUserVo);
        storage.set(USER_KEY, sysUserVo.getId());
        SaLoginModel model = new SaLoginModel();

        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
//        UserType userType = UserType.getUserType(loginUser.getUserType());
//        if (userType == UserType.SYS_USER) {
//            model.setTimeout(86400).setActiveTimeout(1800);
//        } else if (userType == UserType.APP_USER) {
//            model.setTimeout(86400).setActiveTimeout(1800);
//        }
//        userType + CacheConstants.LOGINID_JOIN_CODE + userId
        String loginId = sysUserVo.getUserType() + CacheConstants.LOGINID_JOIN_CODE + sysUserVo.getId();
        StpUtil.login(sysUserVo.getUserCode(), model.setExtra(USER_KEY, sysUserVo.getUserCode()));
        StpUtil.getTokenSession().set(LOGIN_USER_KEY, sysUserVo);
    }

    /**
     * 获取用户(多级缓存)
     */
    public static SysUserVo getLoginUser() {
        SysUserVo loginUser = (SysUserVo) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }
        SaSession session = StpUtil.getTokenSession();
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        loginUser = (SysUserVo) session.get(LOGIN_USER_KEY);
        SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
        return loginUser;
    }

    /**
     * 获取用户基于token
     */
    public static SysUserVo getLoginUser(String token) {
        SaSession session = StpUtil.getTokenSessionByToken(token);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        return (SysUserVo) session.get(LOGIN_USER_KEY);
    }

    /**
     * 获取用户id
     */
    public static Long getUserId() {
        Long userId;
        try {
            userId = Convert.toLong(SaHolder.getStorage().get(USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                userId = Convert.toLong(StpUtil.getExtra(USER_KEY));
                SaHolder.getStorage().set(USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    /**
     * 获取部门ID
     */
    public static Long getDeptId() {
        return getLoginUser().getDeptId();
    }

    /**
     * 获取用户账户
     */
    public static String getUsername() {
        return getLoginUser().getUserName();
    }


}
