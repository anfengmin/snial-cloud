package com.snail.sys.service.impl;

import com.snail.sys.domain.SysDictData;
import com.snail.sys.dao.SysDictDataDao;
import com.snail.sys.service.SysDictDataService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

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


}
