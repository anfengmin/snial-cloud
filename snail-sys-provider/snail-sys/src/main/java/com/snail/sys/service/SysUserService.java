package com.snail.sys.service;


import com.snail.common.core.excel.model.ExcelImportResult;
import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysUser;
import com.snail.sys.dto.UserPasswordUpdateDTO;
import com.snail.sys.dto.UserProfileUpdateDTO;
import com.snail.sys.vo.UserProfileVo;
import com.snail.sys.vo.SysUserVo;
import com.snail.sys.dto.SysUserPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:06:59
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    Page<SysUser> queryByPage(SysUserPageDTO dto);

    /**
     * getUserInfo
     *
     * @param userCode userCode
     * @return com.snail.common.satoken.vo.LoginUser
     * @since 1.0
     */
    LoginUser getUserInfo(String userCode);

    /**
     * registerUserInfo
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     */
    boolean registerUserInfo(SysUser sysUser);

    /**
     * checkUserDataScope
     *
     * @param userId userId
     * @since 1.0
     */
    void checkUserDataScope(Long userId);

    /**
     * 构建登录用户信息（包含权限）
     *
     * @param user 用户账号
     * @return 登录用户信息
     */
    LoginUser buildLoginUser(SysUser user);

    /**
     * getInfo
     *
     * @param userId userId
     * @return com.snail.sys.domain.vo.SysUserVo
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    SysUserVo getInfo(Long userId);

    /**
     * insertUser
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean insertUser(SysUser user);

    /**
     * checkUserCodeUnique
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkUserCodeUnique(SysUser sysUser);

    /**
     * edit
     *
     * @param user user
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    R<Boolean> edit(SysUser user);
    /**
     * 校验用户是否允许操作
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkUserAllowed(SysUser user);


    /**
     * 获取已分配用户角色列表
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    Page<SysUser> selectAllocatedList(SysUserPageDTO dto);



    /**
     * 获取未分配用户角色列表
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    Page<SysUser> selectUnallocatedList(SysUserPageDTO dto);

    /**
     * 校验手机号码是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkPhoneExists(SysUser user);

    /**
     * 校验邮箱是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean checkEmailExists(SysUser user);

    /**
     * 更新用户信息
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    boolean updateUser(SysUser user);

    /**
     * 获取当前登录用户个人资料
     *
     * @param userId 当前登录用户ID
     * @return 个人资料
     */
    UserProfileVo getProfile(Long userId);

    /**
     * 更新当前登录用户个人资料
     *
     * @param userId 当前登录用户ID
     * @param dto 更新参数
     * @return 是否成功
     */
    boolean updateProfile(Long userId, UserProfileUpdateDTO dto);

    /**
     * 更新当前登录用户密码
     *
     * @param userId 当前登录用户ID
     * @param dto 密码更新参数
     * @return 是否成功
     */
    boolean updateProfilePassword(Long userId, UserPasswordUpdateDTO dto);

    /**
     * 导出用户列表
     *
     * @param dto      查询条件
     * @param response 响应
     */
    void exportUsers(SysUserPageDTO dto, HttpServletResponse response);

    /**
     * 下载用户导入模板
     *
     * @param response 响应
     */
    void downloadImportTemplate(HttpServletResponse response);

    /**
     * 导入用户
     *
     * @param file          Excel文件
     * @param updateSupport 是否更新已存在用户
     * @return 导入结果
     */
    ExcelImportResult importUsers(MultipartFile file, boolean updateSupport);

    /**
     * 重置用户密码
     *
     * @param userId 用户ID
     * @return 重置后的初始密码
     */
    String resetUserPassword(Long userId);

}
