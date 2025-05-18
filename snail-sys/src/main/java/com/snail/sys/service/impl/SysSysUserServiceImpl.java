package com.snail.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.api.vo.SysUserVo;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.service.SysUserService;
import com.snial.common.core.constant.UserConstants;
import com.snial.common.core.exception.user.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 用户信息表(User)表服务实现类
 *
 * @author makejava
 * @since 2025-05-11 20:30:04
 */
@Slf4j
@Service("userService")
public class SysSysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    /**
     * 根据用户账号获取用户信息
     *
     * @param userCode 用户账号
     * @return 用户信息
     */
    @Override
    public SysUserVo getUserInfo(String userCode) {
        SysUser sysUserInfo = this.lambdaQuery().eq(com.snail.sys.api.domain.SysUser::getUserCode, userCode).one();
        if (ObjectUtil.isEmpty(sysUserInfo)) {
            throw new UserException("用户不存在", userCode);
        }
        if (UserConstants.USER_DISABLE.equals(sysUserInfo.getStatus())) {
            throw new UserException("账号已禁用", userCode);
        }
        SysUserVo sysUserVo = BeanUtil.copyProperties(sysUserInfo, SysUserVo.class);

        log.info("userInfo:{}", sysUserInfo);
        return sysUserVo;
    }
}
