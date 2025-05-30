package com.snail.sys.service.impl;

import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dao.SysOssConfigDao;
import com.snail.sys.dto.SysOssConfigPageDTO;
import com.snail.sys.service.SysOssConfigService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 对象存储配置表
 *
 * @author makejava
 * @since 2025-05-30 23:04:59
 */
@Service("sysOssConfigService")
public class SysOssConfigServiceImpl extends ServiceImpl<SysOssConfigDao, SysOssConfig> implements SysOssConfigService {


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysOssConfig>> queryByPage(SysOssConfigPageDTO dto) {
        Page<SysOssConfig> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysOssConfig> result = this.lambdaQuery().page(page);
        return R.ok(result);
    }
}
