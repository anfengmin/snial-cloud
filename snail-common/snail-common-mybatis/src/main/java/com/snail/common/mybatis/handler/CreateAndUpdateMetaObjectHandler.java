package com.snail.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;
import java.util.Optional;

/**
 * MP注入处理器
 *
 * @author Anfm
 * Created time 2025/5/20
 * @since 1.0
 */
@Slf4j
public class CreateAndUpdateMetaObjectHandler implements MetaObjectHandler {

    private static final String CREATE_TIME = "createTime";
    private static final String UPDATE_TIME = "updateTime";
    private static final String CREATE_BY = "createBy";
    private static final String UPDATE_BY = "updateBy";

    /**
     * 插入元对象字段填充（用于插入时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Date now = new Date();
        String userCode = getLoginUserCode(metaObject);

        strictInsertFill(metaObject, CREATE_TIME, Date.class, now);
        strictInsertFill(metaObject, UPDATE_TIME, Date.class, now);
        if (userCode != null) {
            strictInsertFill(metaObject, CREATE_BY, String.class, userCode);
            strictInsertFill(metaObject, UPDATE_BY, String.class, userCode);
        }

    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        strictUpdateFill(metaObject, UPDATE_TIME, Date.class, new Date());
        LoginUser loginUser = getLoginUser();
        String userCode = Optional.ofNullable(loginUser)
                .map(LoginUser::getUserCode)
                .orElse(null);
        if (userCode != null) {
            strictUpdateFill(metaObject, UPDATE_BY, String.class, userCode);
        }

    }


    /**
     * 获取登录用户的userCode
     *
     * @param metaObject metaObject
     * @return java.lang.String
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private String getLoginUserCode(MetaObject metaObject) {
        // 1. 优先从 metaObject 中获取 createBy 字段的值，避免重复获取登录用户
        return Optional.ofNullable(metaObject.getValue(CREATE_BY))
                .map(Object::toString) // 将值转为字符串
                .orElseGet(() -> {
                    // 2. 只有在 createBy 字段为空时才获取登录用户信息
                    LoginUser loginUser = getLoginUser();
                    return Optional.ofNullable(loginUser)
                            .map(LoginUser::getUserCode)
                            .orElse(null);
                    // 返回 null 如果 loginUser 或 userCode 为 null
                });
    }

    /**
     * 获取登录用户信息
     *
     * @return com.snail.sys.domain.SysUser
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private static LoginUser getLoginUser() {
        try {
            return LoginUtils.getLoginUser();
        } catch (Exception e) {
            log.warn("自动注入警告 => 用户未登录", e);
            return null;
        }
    }
}
