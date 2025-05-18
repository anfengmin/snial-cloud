package com.snail.common.satoken.core.service;

import cn.dev33.satoken.stp.StpInterface;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.vo.SysUserVo;

import java.util.ArrayList;
import java.util.List;

/**
 * sa-token 权限管理实现类
 *
 * @author Anfm
 * Created time 2025/5/14
 * @since 1.0
 */
public class SaPermissionImpl implements StpInterface {

    /**
     * 获取菜单权限列表
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        SysUserVo sysUserVo = LoginUtils.getLoginUser();
        assert sysUserVo != null;
        return new ArrayList<>(sysUserVo.getMenuPermission());
    }

    /**
     * 获取角色权限列表
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SysUserVo sysUserVo = LoginUtils.getLoginUser();
        assert sysUserVo != null;
        return new ArrayList<>(sysUserVo.getRolePermission());
    }
}