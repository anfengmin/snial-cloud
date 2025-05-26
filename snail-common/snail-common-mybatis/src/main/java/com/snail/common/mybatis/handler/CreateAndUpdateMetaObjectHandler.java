package com.snail.common.mybatis.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.satoken.vo.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        String userCode = getLoginUserCode(metaObject);

        // 使用统一的方法进行填充，减少重复代码
        fillField(metaObject, CREATE_TIME, now, LocalDateTime.class);
        fillField(metaObject, UPDATE_TIME, now, LocalDateTime.class);
        fillField(metaObject, CREATE_BY, userCode, String.class);
        fillField(metaObject, UPDATE_BY, userCode, String.class);

    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        fillField(metaObject, UPDATE_TIME, LocalDateTime.now(), LocalDateTime.class);
        LoginUser loginUser = getLoginUser();
        String userCode = Optional.ofNullable(loginUser)
                .map(LoginUser::getUserCode)
                .orElse(null);
        fillField(metaObject, UPDATE_BY, userCode, String.class);

    }


    /**
     * 通用填充
     *
     * @param metaObject metaObject
     * @param fieldName  fieldName
     * @param value      value
     * @param fieldType  fieldType
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private <T> void fillField(MetaObject metaObject, String fieldName, T value, Class<T> fieldType) {
        if (metaObject.hasSetter(fieldName)) {
            // 只有在值不为空的时候才填充
            if (value != null) {
                // 这里实际上 insert 和 update 都调用的是 insertFill，只是名称不同而已
                this.strictInsertFill(metaObject, fieldName, fieldType, value);
            }
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
     * @return com.snail.sys.api.domain.SysUser
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