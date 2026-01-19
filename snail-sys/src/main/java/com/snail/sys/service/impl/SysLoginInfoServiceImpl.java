package com.snail.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysLoginInfo;
import com.snail.sys.dao.SysLoginInfoDao;
import com.snail.sys.dto.SysLogPageDTO;
import com.snail.sys.service.SysLoginInfoService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;



/**
 * 系统访问记录(SysLoginInfo)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:50:06
 */
@Service("sysLoginInfoService")
public class SysLoginInfoServiceImpl extends ServiceImpl<SysLoginInfoDao, SysLoginInfo> implements SysLoginInfoService {

    /**
     * 清空登录日志
     *
     * @since 1.0
     */
    @Override
    public void cleanLogInfo() {
        this.remove(new LambdaQueryWrapper<>());
    }

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysLoginInfo>> queryByPage(SysLogPageDTO dto) {

        Page<SysLoginInfo> page = this.lambdaQuery()
                .eq(StrUtil.isNotBlank(dto.getStatus()), SysLoginInfo::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getIpaddr()), SysLoginInfo::getIpaddr, dto.getIpaddr())
                .like(StrUtil.isNotBlank(dto.getUserName()), SysLoginInfo::getUserName, dto.getUserName())
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime())
                        , SysLoginInfo::getLoginTime, dto.getBeginTime(), dto.getEndTime())
                .orderByDesc(SysLoginInfo::getId)
                .page(new Page<>(dto.getCurrent(), dto.getSize()));
        return R.ok( page);
    }
}
