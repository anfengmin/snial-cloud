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
import com.snail.common.core.utils.R;
import com.snail.common.core.exception.user.UserException;
import com.snail.sys.api.domain.*;
import com.snail.sys.api.vo.UserVO;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.domain.SysUserPost;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.service.*;
import com.snail.sys.vo.SysUserVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;


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

    private static final String DATA_IMAGE_PREFIX = "data:image/svg+xml;charset=UTF-8,";
    private static final String[][] AVATAR_COLOR_PALETTE = {
            {"#2563EB", "#FFFFFF"},
            {"#0F766E", "#FFFFFF"},
            {"#16A34A", "#FFFFFF"},
            {"#D97706", "#FFFFFF"},
            {"#DC2626", "#FFFFFF"},
            {"#7C3AED", "#FFFFFF"},
            {"#BE185D", "#FFFFFF"},
            {"#0891B2", "#FFFFFF"},
            {"#E2E8F0", "#1E293B"}
    };


    private final SysUserRoleService sysUserRoleService;
    private final SysUserPostService sysUserPostService;
    private final SysPermissionService sysPermissionService;
    private final SysDeptService sysDeptService;

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysUser> queryByPage(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUser> result = this.lambdaQuery()
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(StrUtil.isNotEmpty(dto.getUserCode()), SysUser::getUserCode, dto.getUserCode())
                .like(StrUtil.isNotEmpty(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(ObjectUtil.isNotEmpty(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotEmpty(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo())
                .between(StrUtil.isNotEmpty(dto.getBeginTime()) && StrUtil.isNotEmpty(dto.getEndTime()),
                        SysUser::getCreateTime, dto.getBeginTime(), dto.getEndTime())

                .page(page);
        result.getRecords().forEach(user -> ensureAvatarIfAbsent(user, true));
        return result;
    }

    @Override
    public SysUser getById(Serializable id) {
        SysUser user = super.getById(id);
        return ensureAvatarIfAbsent(user, true);
    }

    /**
     * 根据用户账号获取用户信息
     *
     * @param userCode 用户账号
     * @return 用户信息
     */
    @Override
    public LoginUser getUserInfo(String userCode) {
        // 使用 MPJ 关联查询用户、部门、角色信息
//        SysUser sysUserInfo = this.lambdaQuery().eq(SysUser::getUserCode, userCode).one();
        SysUser sysUserInfo = selectUserByUserName(userCode);
        if (ObjectUtil.isEmpty(sysUserInfo)) {
            throw new UserException("user.not.exists", userCode);
        }
        if (UserConstants.USER_DISABLE.equals(sysUserInfo.getStatus())) {
            throw new UserException("user.blocked", userCode);
        }
//        LoginUser loginUser = BeanUtil.copyProperties(sysUserInfo, LoginUser.class);

        log.info("userInfo:{}", sysUserInfo);
        return buildLoginUser(sysUserInfo);
    }

    private SysUser selectUserByUserName(String userCode) {
        MPJLambdaWrapper<SysUser> wrapper = new MPJLambdaWrapper<SysUser>()
//                .distinct()
                .selectAll(SysUser.class) // 查询用户表所有字段
                // 1. 映射一对一：部门 (SysUser.dept)
                .selectAssociation(SysDept.class, SysUser::getDept)
                // 2. 映射一对多：角色 (SysUser.roles)
                .selectCollection(SysRole.class, SysUser::getRoles)
                // 连表逻辑保持不变
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysUser::getUserCode, userCode);


        // 一个用户可能有多个角色，MPJ 会自动将多条记录折叠到 SysUser.roles 集合中
        List<SysUser> list = baseMapper.selectJoinList(SysUser.class, wrapper);

//        List<SysUserRole> sysUserRoles = sysUserRoleService.queryRoleListByUserId(user.getId());
//        SysDept sysDept = sysDeptService.queryDeptByDeptId(user.getDeptId());
        return CollUtil.isNotEmpty(list) ? ensureAvatarIfAbsent(list.get(0), true) : null;
    }

    /**
     * 构建登录用户信息（包含权限）- 权限从数据库动态获取，此方法保留用于兼容
     *
     * @param userCode 用户账号
     * @return 登录用户信息
     */
    @Override
    public LoginUser buildLoginUser(SysUser user) {
        // 权限信息现在通过 SysSaPermissionImpl 从数据库动态获取
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUserCode(user.getUserCode());
        loginUser.setUserName(user.getUserName());
        loginUser.setPassWord(user.getPassWord());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(sysPermissionService.getMenuPermission(user));
        loginUser.setRolePermission(sysPermissionService.getRolePermission(user));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? StrUtil.EMPTY : user.getDept().getDeptName());
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
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
        fillAvatarForPersist(sysUser, null);
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
        SysUser user = this.getById(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new ServiceException("用户不存在");
        }

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        Long[] roleIds = sysUserRoleService.queryRoleListByUserId(userId)
                .stream()
                .map(SysUserRole::getRoleId)
                .toArray(Long[]::new);
        Long[] postIds = sysUserPostService.lambdaQuery()
                .eq(SysUserPost::getUserId, userId)
                .list()
                .stream()
                .map(SysUserPost::getPostId)
                .toArray(Long[]::new);

        userVO.setRoleIds(roleIds);
        userVO.setPostIds(postIds);

        return SysUserVo.builder()
                .user(userVO)
                .build();
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
        fillAvatarForPersist(user, null);
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
        SysUser dbUser = super.getById(user.getId());
        fillAvatarForPersist(user, ObjectUtil.isNull(dbUser) ? null : dbUser.getAvatar());
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
        SysUser dbUser = super.getById(userId);
        fillAvatarForPersist(user, ObjectUtil.isNull(dbUser) ? null : dbUser.getAvatar());

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

    private SysUser ensureAvatarIfAbsent(SysUser user, boolean persist) {
        if (ObjectUtil.isEmpty(user) || StrUtil.isNotBlank(user.getAvatar())) {
            return user;
        }

        String avatar = generateAvatarDataUri(user.getUserName());
        user.setAvatar(avatar);

        if (persist && ObjectUtil.isNotNull(user.getId())) {
            this.lambdaUpdate()
                    .eq(SysUser::getId, user.getId())
                    .set(SysUser::getAvatar, avatar)
                    .update();
        }

        return user;
    }

    private void fillAvatarForPersist(SysUser user, String currentAvatar) {
        if (StrUtil.isNotBlank(user.getAvatar())) {
            return;
        }

        if (StrUtil.isNotBlank(currentAvatar)) {
            user.setAvatar(currentAvatar);
            return;
        }

        user.setAvatar(generateAvatarDataUri(user.getUserName()));
    }

    private String generateAvatarDataUri(String userName) {
        String avatarText = extractAvatarText(userName);
        int hash = Math.abs(StrUtil.emptyToDefault(userName, "USER").hashCode());
        String[] palette = AVATAR_COLOR_PALETTE[hash % AVATAR_COLOR_PALETTE.length];
        String backgroundColor = palette[0];
        String textColor = palette[1];
        int fontSize = avatarText.length() <= 1 ? 72 : avatarText.length() == 2 ? 56 : 44;

        String safeText = escapeSvgText(avatarText);
        String svg =
                "<svg xmlns='http://www.w3.org/2000/svg' width='160' height='160' viewBox='0 0 160 160'>"
                        + "<rect width='160' height='160' rx='20' fill='" + backgroundColor + "'/>"
                        + "<text x='50%' y='50%' text-anchor='middle' dominant-baseline='central' "
                        + "font-family='PingFang SC, Microsoft YaHei, Helvetica Neue, Arial, sans-serif' "
                        + "font-size='" + fontSize + "' font-weight='700' fill='" + textColor + "'>"
                        + safeText
                        + "</text></svg>";

        return DATA_IMAGE_PREFIX + encodeSvg(svg);
    }

    private String extractAvatarText(String userName) {
        String source = StrUtil.blankToDefault(StrUtil.trim(userName), "User");

        String chinese = source.replaceAll("[^\\u4e00-\\u9fa5]", "");
        if (StrUtil.isNotBlank(chinese)) {
            if (chinese.length() <= 2) {
                return chinese;
            }
            return chinese.substring(chinese.length() - 2);
        }

        String[] words = source.split("[^A-Za-z0-9]+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (StrUtil.isBlank(word)) {
                continue;
            }
            initials.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
            if (initials.length() >= 3) {
                break;
            }
        }
        if (initials.length() >= 2) {
            return initials.toString();
        }

        String normalized = source.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        if (StrUtil.isBlank(normalized)) {
            return "U";
        }
        return normalized.substring(0, Math.min(normalized.length(), 3));
    }

    private String encodeSvg(String svg) {
        try {
            return URLEncoder.encode(svg, StandardCharsets.UTF_8.name())
                    .replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new ServiceException("编码用户头像失败");
        }
    }

    private String escapeSvgText(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
