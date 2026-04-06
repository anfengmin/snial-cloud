package com.snail.sys.service;

import com.snail.sys.domain.SysDictData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;


/**
 * 字典数据表(SysDictData)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:34:18
 */
public interface SysDictDataService extends IService<SysDictData> {


    /**
     * queryDictDataByType
     *
     * @param dictType dictType
     * @return java.util.List<com.snail.sys.domain.SysDictData>
     * @since 1.0
     */
    List<SysDictData> queryDictDataByType(String dictType);
}
