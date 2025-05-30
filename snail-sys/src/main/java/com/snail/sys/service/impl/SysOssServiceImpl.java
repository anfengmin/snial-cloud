package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOss;
import com.snail.sys.dao.SysOssDao;
import com.snail.sys.dto.SysOssPageDTO;
import com.snail.sys.service.SysOssService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:33
 */
@Service("sysOssService")
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOss> implements SysOssService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysOss>> queryByPage(SysOssPageDTO dto) {
        Page<SysOss> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysOss> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
