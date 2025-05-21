package com.snail.sys.service.impl;

import com.snail.sys.domain.SysDept;
import com.snail.sys.dao.SysDeptDao;
import com.snail.sys.service.SysDeptService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import javax.annotation.Resource;

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


}
