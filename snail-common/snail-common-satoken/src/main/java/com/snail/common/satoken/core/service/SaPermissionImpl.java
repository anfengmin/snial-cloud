package com.snail.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.common.satoken.vo.LoginUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Sa-Token 权限管理实现类
 * <p>
 * 默认实现返回空权限列表
 * 各业务服务需要重写此实现，从数据库动态获取权限
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     * <p>
     * 默认返回空列表，由各业务服务重写从数据库获取
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 权限信息由各业务服务通过重写此方法从数据库获取
        // 示例：调用权限服务获取用户权限
        return new ArrayList<>();
    }

    /**
     * 获取角色权限列表
     * <p>
     * 默认返回空列表，由各业务服务重写从数据库获取
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 权限信息由各业务服务通过重写此方法从数据库获取
        // 示例：调用角色服务获取用户角色
        return new ArrayList<>();
    }
}
