package com.snail.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.snail.sys.domain.SysDept;
import com.snail.sys.dao.SysDeptDao;
import com.snail.sys.service.SysDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;
import java.util.List;

/**
 * 部门表(SysDept)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:30:47
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDept> implements SysDeptService {

    @Resource
    private SysDeptDao sysDeptDao;


    /**
     * 获取部门列表
     *
     * @param dept dept
     * @return java.util.List<com.snail.sys.domain.SysDept>
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
     * 校验部门名称是否唯一
     *
     * @param dept dept
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean checkDeptNameUnique(SysDept dept) {
        return this.lambdaQuery()
                .eq(SysDept::getDeptName, dept.getDeptName())
                .eq(SysDept::getParentId, dept.getParentId())
                .ne(ObjectUtil.isNotNull(dept.getId()), SysDept::getId, dept.getId())
                .exists();
    }


}
