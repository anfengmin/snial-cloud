package com.snail.sys.service.impl;

import com.snail.common.core.utils.R;
import com.snail.sys.api.domain.SysRole;
import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.dao.SysRoleDeptDao;
import com.snail.sys.dto.SysRoleDeptPageDTO;
import com.snail.sys.service.SysRoleDeptService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 角色和部门关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:26
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDept> implements SysRoleDeptService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysRoleDept>> queryByPage(SysRoleDeptPageDTO dto) {
        Page<SysRoleDept> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysRoleDept> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }

    /**
     * 删除角色部门关联
     *
     * @param roleId 角色ID
     */
    @Override
    public void deleteRoleDeptByRoleId(Long roleId) {
        this.lambdaUpdate().eq(SysRoleDept::getRoleId, roleId).remove();
    }

    /**
     * 新增角色部门关联
     *
     * @param role 角色信息
     * @return 新增结果
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean insertRoleDept(SysRole role) {
        // 新增角色与部门（数据权限）管理
        List<SysRoleDept> list = Arrays.stream(role.getDeptIds()).map(deptId -> {
            SysRoleDept roleDept = new SysRoleDept();
            roleDept.setRoleId(role.getId());
            roleDept.setDeptId(deptId);
            return roleDept;
        }).collect(Collectors.toList());
        return this.saveBatch(list);
    }
}
