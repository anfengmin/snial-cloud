package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.sys.domain.SysDictType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.dto.SysDictTypePageDTO;


/**
 * 字典类型(SysDictType)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
public interface SysDictTypeService extends IService<SysDictType> {

    /**
     * queryByPage
     *
     * @param dto dto
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.snail.sys.domain.SysDictType>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    Page<SysDictType> queryByPage(SysDictTypePageDTO dto);
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
