package com.snail.common.satoken.utils;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.context.model.SaStorage;
import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.snail.sys.api.domain.LoginUser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 登录鉴权助手
 * <p>
 * 使用Redis存储Token和Session，权限从数据库动态获取
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * device 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LoginUtils {

    /**
     * 登录用户Key
     */
    public static final String LOGIN_USER_KEY = "loginUser";

    /**
     * 用户ID Key
     */
    public static final String USER_KEY = "userId";

    /**
     * 登录系统
     *
     * @param loginUser 登录用户信息
     */
    public static void login(LoginUser loginUser) {
        SaStorage storage = SaHolder.getStorage();
        storage.set(LOGIN_USER_KEY, loginUser);
        storage.set(USER_KEY, loginUser.getId());

        // 使用userCode作为登录ID
        StpUtil.login(loginUser.getUserCode());
    }

    /**
     * 获取当前登录用户（多级缓存）
     * <p>
     * 优先从ThreadLocal获取，再从Session获取
     *
     * @return 登录用户信息
     */
    public static LoginUser getLoginUser() {
        // 1. 先从ThreadLocal存储获取（最高优先级）
        LoginUser loginUser = (LoginUser) SaHolder.getStorage().get(LOGIN_USER_KEY);
        if (loginUser != null) {
            return loginUser;
        }

        // 2. 从Session获取
        try {
            Object sessionObj = StpUtil.getTokenSession();
            if (sessionObj != null) {
                loginUser = (LoginUser) StpUtil.getTokenSession().get(LOGIN_USER_KEY);
                if (loginUser != null) {
                    // 存入ThreadLocal缓存
                    SaHolder.getStorage().set(LOGIN_USER_KEY, loginUser);
                }
            }
        } catch (Exception e) {
            // 忽略异常
        }

        return loginUser;
    }

    /**
     * 获取用户基于token
     */
    public static LoginUser getLoginUser(String token) {
        SaSession session = StpUtil.getTokenSessionByToken(token);
        if (ObjectUtil.isNull(session)) {
            return null;
        }
        return (LoginUser) session.get(LOGIN_USER_KEY);
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 用户ID
     */
    public static Long getUserId() {
        Long userId;
        try {
            // 1. 先从ThreadLocal获取
            userId = Convert.toLong(SaHolder.getStorage().get(USER_KEY));
            if (ObjectUtil.isNull(userId)) {
                // 2. 从Token extra中获取
                Object extra = StpUtil.getExtra(USER_KEY);
                if (extra != null) {
                    userId = Convert.toLong(extra);
                }
            }
            if (ObjectUtil.isNotNull(userId)) {
                SaHolder.getStorage().set(USER_KEY, userId);
            }
        } catch (Exception e) {
            return null;
        }
        return userId;
    }

    /**
     * 获取部门ID
     *
     * @return 部门ID
     */
    public static Long getDeptId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getDeptId() : null;
    }

    /**
     * 获取用户账户（用户名）
     *
     * @return 用户名
     */
    public static String getUsername() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserName() : null;
    }

    /**
     * 获取用户编码
     *
     * @return 用户编码
     */
    public static String getUserCode() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserCode() : null;
    }

    /**
     * 获取用户类型
     *
     * @return 用户类型
     */
    public static String getUserType() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getUserType() : null;
    }

    /**
     * 判断是否为管理员
     *
     * @return 是否为管理员
     */
    public static boolean isAdmin() {
        return isAdmin(getUserId());
    }

    /**
     * 判断是否为管理员
     *
     * @param userId 用户ID
     * @return 是否为管理员
     */
    public static boolean isAdmin(Long userId) {
        // 管理员判断由 Sa-Token 注解通过 StpInterface 动态处理
        // 此方法保留用于代码中快速判断
        return userId != null && userId == 1L;
    }

    /**
     * 检查是否已登录
     *
     * @return 是否已登录
     */
    public static boolean isLogin() {
        try {
            return StpUtil.isLogin();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 踢出登录
     *
     * @param loginId 登录ID
     */
    public static void logout(Object loginId) {
        StpUtil.logout(loginId);
    }

    /**
     * 当前会话退出登录
     */
    public static void logout() {
        try {
            StpUtil.logout();
        } catch (Exception e) {
            // 忽略未登录异常
        }
    }

    /**
     * 获取Token值
     *
     * @return Token值
     */
    public static String getTokenValue() {
        return StpUtil.getTokenValue();
    }

    /**
     * 获取Token名称
     *
     * @return Token名称
     */
    public static String getTokenName() {
        return StpUtil.getTokenName();
    }
}
