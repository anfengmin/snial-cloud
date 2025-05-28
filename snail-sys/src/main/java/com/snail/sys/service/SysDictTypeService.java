package com.snail.sys.service;

import com.snail.sys.domain.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * 字典类型(SysDictType)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
public interface SysDictTypeService extends IService<SysDictType> {


    /**
     * checkDictTypeUnique
     *
     * @param sysDictType sysDictType
     * @return boolean
     * @since 1.0
     */
    boolean checkDictTypeUnique(SysDictType sysDictType);

    /**
     * 重置字典缓存数据
     *
     * @since 1.0
     */
    void resetDictCache();

    /**
     * 清空字典缓存数据
     *
     * @since 1.0
     */
    void clearDictCache();

    /**
     * 加载字典缓存数据
     *
     * @since 1.0
     */
    void loadingDictCache();

}
