package com.snail.sys.service;

import com.snail.sys.domain.SysRoleDept;
import com.snail.sys.dto.SysRoleDeptPageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 角色和部门关联
 *
 * @author makejava
 * @since 2025-05-30 23:06:26
 */
public interface SysRoleDeptService extends IService<SysRoleDept> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    R<Page<SysRoleDept>> queryByPage(SysRoleDeptPageDTO dto);


}
