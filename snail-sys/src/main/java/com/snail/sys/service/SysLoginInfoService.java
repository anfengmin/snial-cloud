package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysLoginInfo;
import com.snail.sys.dto.SysLogPageDTO;


/**
 * 系统访问记录(SysLoginInfo)表服务接口
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
public interface SysLoginInfoService extends IService<SysLoginInfo> {


    /**
     * 清空登录日志
     */
    void cleanLogInfo();

    /**
     * 分页查询
     *
     * @param dto dto
     * @return R
     */
    Page<SysLoginInfo> queryByPage(SysLogPageDTO dto);
}
