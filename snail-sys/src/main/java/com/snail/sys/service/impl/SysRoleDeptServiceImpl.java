package com.snail.sys.service.impl;

import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.dao.SysRoleDeptDao;
import com.snail.sys.dto.SysRoleDeptPageDTO;
import com.snail.sys.service.SysRoleDeptService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


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
}
