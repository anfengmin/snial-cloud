package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.dto.SysConfigPageDTO;



/**
 * 参数配置表(SysConfig)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
public interface SysConfigService extends IService<SysConfig> {

    R<Page<SysConfig>> queryByPage(SysConfigPageDTO dto);

    String selectConfigByKey(String configKey);
}
