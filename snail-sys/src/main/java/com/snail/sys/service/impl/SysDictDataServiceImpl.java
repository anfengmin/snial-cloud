package com.snail.sys.service.impl;

import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.constant.UserConstants;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.dao.SysDictDataDao;
import com.snail.sys.service.SysDictDataService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;
import java.util.List;

/**
 * 字典数据表(SysDictData)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
@Service("sysDictDataService")
public class SysDictDataServiceImpl extends ServiceImpl<SysDictDataDao, SysDictData> implements SysDictDataService {

    @Resource
    private SysDictDataDao sysDictDataDao;


    /**
     * queryDictDataByType
     *
     * @param dictType dictType
     * @return java.util.List<com.snail.sys.domain.SysDictData>
     * @since 1.0
     */
    @Override
    public List<SysDictData> queryDictDataByType(String dictType) {
        return this.lambdaQuery()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, UserConstants.NORMAL)
                .orderByAsc(SysDictData::getDictSort)
                .list();
    }
}
