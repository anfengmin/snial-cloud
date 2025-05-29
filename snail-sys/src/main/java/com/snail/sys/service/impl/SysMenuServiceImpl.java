package com.snail.sys.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.sys.domain.SysMenu;
import com.snail.sys.dao.SysMenuDao;
import com.snail.sys.dto.SysMenuPageDTO;
import com.snail.sys.service.SysMenuService;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * 菜单权限
 *
 * @author makejava
 * @since 2025-05-29 21:45:02
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenu> implements SysMenuService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysMenu>> queryByPage(SysMenuPageDTO dto) {
        Page<SysMenu> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysMenu> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
