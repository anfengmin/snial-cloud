package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysConfig;
import com.snail.sys.dto.SysConfigPageDTO;



/**
 * 参数配置表(SysConfig)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 筛选结果
     */
    Page<SysConfig> queryByPage(SysConfigPageDTO dto);

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 参数键
     * @return 参数键值
     */
    String selectConfigByKey(String configKey);

    /**
     * 校验参数键名是否唯一
     *
     * @param sysConfig 参数配置信息
     * @return 结果
     */
    boolean checkConfigKeyExists(SysConfig sysConfig);
}
