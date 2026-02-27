package com.snail.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.exception.user.UserException;
import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.api.domain.SysDept;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.*;
import com.snail.sys.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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


    private final SysUserRoleService sysUserRoleService;
    private final SysUserPostService sysUserPostService;

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysUser> queryByPage(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        return this.lambdaQuery()
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(StrUtil.isNotEmpty(dto.getUserCode()), SysUser::getUserCode, dto.getUserCode())
                .like(StrUtil.isNotEmpty(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(ObjectUtil.isNotEmpty(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotEmpty(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo())
                .between(StrUtil.isNotEmpty(dto.getBeginTime()) && StrUtil.isNotEmpty(dto.getEndTime()),
                        SysUser::getCreateTime, dto.getBeginTime(), dto.getEndTime())

                .page(page);
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
     * 构建登录用户信息（包含权限）- 权限从数据库动态获取，此方法保留用于兼容
     *
     * @param userCode 用户账号
     * @return 登录用户信息
     */
    @Override
    public LoginUser buildLoginUser(String userCode) {
        // 权限信息现在通过 SysSaPermissionImpl 从数据库动态获取
        // 此方法保留用于兼容，返回基本的用户信息
        return getUserInfo(userCode);
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

    /**
     * checkUserDataScope
     *
     * @param userId userId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkUserDataScope(Long userId) {
        boolean flag = sysUserRoleService.checkAdminRole(userId);
        if (!flag) {
            this.lambdaQuery()
                    .eq(SysUser::getId, userId)
                    .oneOpt()
                    .orElseThrow(() -> new ServiceException("没有权限访问用户数据！"));
        }
    }

    @Override
    public SysUserVo getInfo(Long userId) {
        this.checkUserDataScope(userId);
        // TODO
        return null;
    }

    /**
     * insertUser
     *
     * @param user user
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        // 新增用户信息
        user.setPassWord(BCrypt.hashpw(user.getPassWord()));
        boolean save = this.save(user);
        // 新增用户岗位关联
        sysUserPostService.insertUserPost(user);
        // 新增用户与角色管理
        sysUserRoleService.insertUserRole(user);
        return save;
    }
    /**
     * checkUserCodeUnique
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkUserCodeUnique(SysUser sysUser) {
        return this.lambdaQuery().eq(SysUser::getUserCode, sysUser.getUserCode())
                .ne(ObjectUtil.isNotNull(sysUser.getId()), SysUser::getId, sysUser.getId())
                .exists();
    }

    /**
     * edit
     *
     * @param user user
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public R<Boolean> edit(SysUser user) {
        this.checkUserAllowed(user);
        this.checkUserDataScope(user.getId());

        if (checkUserCodeUnique(user)) {
            return R.fail("修改用户:" + user.getUserCode() + "失败，登录账号已存在");
        }
        return R.ok(this.updateById(user));
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user user
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StrUtil.isNotBlank(user.getUserCode()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }



    /**
     * 查询已分配用户角色列表
     *
     * @param dto dto
     * @return Page<SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Page<SysUser> selectAllocatedList(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());

        MPJLambdaWrapper<SysUser> wrapper = getUserEq()
                .eq(ObjectUtil.isNotNull(dto.getRoleId()), SysRole::getId, dto.getRoleId())
                .like(StrUtil.isNotBlank(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo());

        return baseMapper.selectJoinPage(page, SysUser.class, wrapper);
    }


    /**
     * 查询未分配用户角色列表
     *
     * @param dto dto
     * @return Page<SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Page<SysUser> selectUnallocatedList(SysUserPageDTO dto) {
        List<Long> userIds = sysUserRoleService.selectUserIdsByRoleId(dto.getRoleId());
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        MPJLambdaWrapper<SysUser> wrapper = getUserEq()
                .and(w -> w.ne(SysRole::getId, dto.getRoleId()).or().isNull(SysRole::getId))
                .notIn(CollUtil.isNotEmpty(userIds), SysUser::getId, userIds)
                .like(StrUtil.isNotBlank(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .like(StrUtil.isNotBlank(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo());
        return baseMapper.selectJoinPage(page, SysUser.class, wrapper);

    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkPhoneExists(SysUser user) {
        return this.lambdaQuery().eq(SysUser::getPhoneNo, user.getPhoneNo())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId())
                .exists();
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkEmailExists(SysUser user) {
        return this.lambdaQuery().eq(SysUser::getEmail,user.getEmail())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId())
                .exists();
    }

    /**
     * updateUser
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        Long userId = user.getId();

        // 删除用户与岗位关联
        sysUserPostService.deleteUserPost(userId);
        // 新增用户岗位关联
        sysUserPostService.insertUserPost(user);
        // 删除用户与角色关联
        sysUserRoleService.deleteUserRole(userId);
        // 新增用户与角色管理
        sysUserRoleService.insertUserRole(user);

        return this.updateById(user);
    }

    /**
     * getUserEq
     *
     * @return com.github.yulichang.wrapper.MPJLambdaWrapper<com.snail.sys.api.domain.SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private static MPJLambdaWrapper<SysUser> getUserEq() {
        return new MPJLambdaWrapper<SysUser>()
                .select(SysUser::getId,
                        SysUser::getDeptId,
                        SysUser::getUserName,
                        SysUser::getNickName,
                        SysUser::getEmail,
                        SysUser::getPhoneNo,
                        SysUser::getStatus,
                        SysUser::getCreateTime)
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)

                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL);
    }
}
