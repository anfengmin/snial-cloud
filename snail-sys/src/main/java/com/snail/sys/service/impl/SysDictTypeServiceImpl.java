package com.snail.sys.service.impl;

import com.snail.sys.domain.SysDictType;
import com.snail.sys.dao.SysDictTypeDao;
import com.snail.sys.service.SysDictTypeService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

/**
 * 字典类型(SysDictType)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictType> implements SysDictTypeService {

    @Resource
    private SysDictTypeDao sysDictTypeDao;


}
