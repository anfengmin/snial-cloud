package com.snail.sys.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.utils.R;
import com.snail.common.mybatis.helper.DataBaseHelper;
import com.snail.common.redis.utils.CacheUtils;
import com.snail.sys.api.domain.SysDept;
import com.snail.sys.api.domain.SysUser;
import com.snail.sys.dao.SysDeptDao;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.service.SysDeptService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门表(SysDept)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:30:47
 */
@RequiredArgsConstructor
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDept> implements SysDeptService {

    /**
     * 这里仅在校验部门下是否存在用户时需要访问用户表，
     * 为了避免与 SysUserServiceImpl 形成循环依赖，直接依赖 SysUserDao 即可。
     */
    private final SysUserDao sysUserDao;

    /**
     * 获取部门列表
     *
     * @param dept dept
     * @return java.util.List<com.snail.sys.api.domain.SysDept>
     * @since 1.0
     */
    @Override
    public List<SysDept> queryDeptList(SysDept dept) {

        return this.lambdaQuery()
                .eq(ObjectUtil.isNotNull(dept.getId()), SysDept::getId, dept.getId())
                .eq(ObjectUtil.isNotNull(dept.getParentId()), SysDept::getParentId, dept.getParentId())
                .like(ObjectUtil.isNotNull(dept.getDeptName()), SysDept::getDeptName, dept.getDeptName())
                .eq(ObjectUtil.isNotNull(dept.getStatus()), SysDept::getStatus, dept.getStatus())
                .orderByAsc(SysDept::getParentId)
                .orderByAsc(SysDept::getOrderNo)
                .list();
    }

    /**
     * 校验部门名称是否存在
     *
     * @param dept dept
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean checkDeptNameExists(SysDept dept) {
        return this.lambdaQuery()
                .eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .ne(ObjectUtil.isNotNull(dept.getId()), SysDept::getId, dept.getId())
                .exists();
    }

    /**
     * 校验部门是否有子部门
     *
     * @param deptId deptId
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean hasChildByDeptId(Long deptId) {
        return this.lambdaQuery()
                .eq(SysDept::getParentId, deptId)
                .exists();
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId deptId
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean checkDeptExistUser(Long deptId) {
        // 直接通过 SysUserDao 查询，避免注入 SysUserService 造成循环依赖
        return sysUserDao.selectCount(
                new LambdaQueryWrapper<SysUser>().eq(SysUser::getDeptId, deptId)
        ) > 0;
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId deptId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkDeptDataScope(Long deptId) {
        SysDept sysDept = baseMapper.selectById(deptId);
        if (ObjectUtil.isEmpty(sysDept)) {
            throw new ServiceException("没有权限访问部门数据！");

        }
    }

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId deptId
     * @return Long
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Long selectNormalChildrenDeptById(Long deptId) {
        return this.lambdaQuery()
                .eq(SysDept::getId, deptId)
                .count();
    }

    /**
     * 修改保存部门信息
     *
     * @param dept dept
     * @return com.snail.common.core.utils.R<java.lang.Void>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @CacheEvict(cacheNames = CacheNames.SYS_DEPT, key = "#dept.deptId")
    @Override
    public R<Boolean> updateDept(SysDept dept) {
        SysDept newParentDept = baseMapper.selectById(dept.getParentId());
        SysDept oldDept = baseMapper.selectById(dept.getId());
        if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
            String newAncestors = newParentDept.getAncestors() + StrUtil.COMMA + newParentDept.getId();
            String oldAncestors = oldDept.getAncestors();
            dept.setAncestors(newAncestors);
            updateDeptChildren(dept.getId(), newAncestors, oldAncestors);
        }
        boolean result = this.updateById(dept);

        if (ObjectUtil.equals(UserConstants.DEPT_NORMAL, dept.getStatus()) && StrUtil.isNotEmpty(dept.getAncestors())
                && !StrUtil.equals(UserConstants.DEPT_NORMAL, dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return R.ok(result);
    }

    /**
     * 根据ID查询部门
     *
     * @param deptId deptId
     * @return com.snail.sys.api.domain.SysDept
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public SysDept queryDeptByDeptId(Long deptId) {
        return this.lambdaQuery().eq(SysDept::getId, deptId).one();
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private void updateParentDeptStatusNormal(SysDept dept) {
        String ancestors = dept.getAncestors();
        List<Long> deptIds = Convert.toList(Long.class, ancestors);
        this.lambdaUpdate().set(SysDept::getStatus, UserConstants.DEPT_NORMAL)
                .in(SysDept::getId, deptIds).update();

    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    public void updateDeptChildren(Long deptId, String newAncestors, String oldAncestors) {

        List<SysDept> children = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
                .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<SysDept> list = new ArrayList<>();
        for (SysDept child : children) {
            SysDept dept = new SysDept();
            dept.setId(child.getId());
            dept.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(dept);
        }
        if (!list.isEmpty()) {
            if (this.updateBatchById(list)) {
                list.forEach(dept -> CacheUtils.evict(CacheNames.SYS_DEPT, dept.getId()));
            }
        }
    }
}
