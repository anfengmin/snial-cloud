package com.snail.sys.service;

import com.snail.sys.domain.SysDept;
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
     * @return java.util.List<com.snail.sys.domain.SysDept>
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
    boolean checkDeptNameUnique(SysDept dept);
}
