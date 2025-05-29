package com.snail.sys.service;

import com.snail.sys.domain.SysNotice;
import com.snail.sys.dto.SysNoticePageDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;

/**
 * 通知公告
 *
 * @author makejava
 * @since 2025-05-29 21:50:40
 */
public interface SysNoticeService extends IService<SysNotice> {

 /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
     R<Page<SysNotice>> queryByPage(SysNoticePageDTO dto);


}
