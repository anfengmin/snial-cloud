package com.snail.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.exception.user.UserException;
import com.snail.common.satoken.vo.LoginUser;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.domain.SysDept;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.SysDeptService;
import com.snail.sys.service.SysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;
import java.util.Map;


/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:07:00
 */
@Slf4j
@RequiredArgsConstructor
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {


    private final SysDeptService sysDeptService;
    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysUser>> queryByPage(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUser> result = this.lambdaQuery()
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(StrUtil.isNotEmpty(dto.getUserCode()), SysUser::getUserCode, dto.getUserCode())
                .like(StrUtil.isNotEmpty(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(StrUtil.isNotEmpty(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotEmpty(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo())
                .between(StrUtil.isNotEmpty(dto.getBeginTime()) && StrUtil.isNotEmpty(dto.getEndTime()),
                        SysUser::getCreateTime, dto.getBeginTime(), dto.getEndTime())

                .page(page);
        return R.ok(result);
    }

    /**
     * 根据用户账号获取用户信息
     *
     * @param userCode 用户账号
     * @return 用户信息
     */
    @Override
    public LoginUser getUserInfo(String userCode) {
        SysUser sysUserInfo = this.lambdaQuery().eq(SysUser::getUserCode, userCode).one();
        if (ObjectUtil.isEmpty(sysUserInfo)) {
            throw new UserException("user.not.exists", userCode);
        }
        if (UserConstants.USER_DISABLE.equals(sysUserInfo.getStatus())) {
            throw new UserException("user.blocked", userCode);
        }
        LoginUser loginUser = BeanUtil.copyProperties(sysUserInfo, LoginUser.class);

        log.info("userInfo:{}", sysUserInfo);
        return loginUser;
    }

    /**
     * 注册用户信息
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean registerUserInfo(SysUser sysUser) {
        boolean exists = this.lambdaQuery()
                .eq(SysUser::getUserCode, sysUser.getUserCode())
                .ne(ObjectUtil.isNotNull(sysUser.getId()), SysUser::getId, sysUser.getId())
                .exists();
        if (exists) {
            throw new UserException("user.register.save.error", sysUser.getUserCode());
        }
        return this.save(sysUser);
    }
}
