package com.snail.sys.service;

import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 部门表(SysDept)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:30:46
 */
public interface SysDeptService extends IService<SysDept> {

    /**
     * 获取部门列表
     *
     * @param dept dept
     * @return java.util.List<com.snail.sys.api.domain.SysDept>
     * @since 1.0
     */
    List<SysDept> queryDeptList(SysDept dept);

    /**
     * 校验部门名称是否唯一
     *
     * @param dept dept
     * @return boolean
     * @since 1.0
     */
    boolean checkDeptNameExists(SysDept dept);

    /**
     * 校验部门是否有子部门
     *
     * @param deptId deptId
     * @return boolean
     * @since 1.0
     */
    boolean hasChildByDeptId(Long deptId);

    /**
     * 查询部门是否存在用户
     *
     * @param deptId deptId
     * @return boolean
     * @since 1.0
     */
    boolean checkDeptExistUser(Long deptId);

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId deptId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    void checkDeptDataScope(Long deptId);

    /**
     * 根据ID查询所有子部门（正常状态）
     *
     * @param deptId deptId
     * @return int
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    Long selectNormalChildrenDeptById(Long deptId);

    /**
     * 修改保存部门信息
     *
     * @param dept dept
     * @return com.snail.common.core.utils.R<java.lang.Void>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    R<Boolean> updateDept(SysDept dept);
}
