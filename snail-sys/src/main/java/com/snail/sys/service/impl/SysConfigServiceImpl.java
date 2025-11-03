package com.snail.sys.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.snail.common.core.utils.R;
import com.snail.sys.domain.SysConfig;
import com.snail.sys.dao.SysConfigDao;
import com.snail.sys.dto.SysConfigPageDTO;
import com.snail.sys.service.SysConfigService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 参数配置表(SysConfig)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:32:37
 */
@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigDao, SysConfig> implements SysConfigService {

    @Resource
    private SysConfigDao sysConfigDao;

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysConfig>> queryByPage(SysConfigPageDTO dto) {
        // 创建分页对象
        Page<SysConfig> page = new Page<>(dto.getCurrent(), dto.getSize());
        
        // 构建查询条件并执行分页查询
        Page<SysConfig> result = this.lambdaQuery()
                // 参数名称模糊查询
                .like(StrUtil.isNotBlank(dto.getConfigName()), SysConfig::getConfigName, dto.getConfigName())
                // 系统内置类型精确查询
                .eq(StrUtil.isNotBlank(dto.getConfigType()), SysConfig::getConfigType, dto.getConfigType())
                // 参数键名模糊查询
                .like(StrUtil.isNotBlank(dto.getConfigKey()), SysConfig::getConfigKey, dto.getConfigKey())
                // 创建时间范围查询
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime()),
                        SysConfig::getCreateTime, 
                        StrUtil.isNotBlank(dto.getBeginTime()) ? DateUtil.parseDateTime(dto.getBeginTime()) : null,
                        StrUtil.isNotBlank(dto.getEndTime()) ? DateUtil.parseDateTime(dto.getEndTime()) : null)
                // 执行分页查询
                .page(page);

        return R.ok(result);
    }

    /**
     * 根据配置键查询配置值
     *
     * @param configKey 配置键
     * @return 配置值，如果不存在则返回空字符串
     */
    // TODO: 2025-05-22 14:25:00 缓存配置 为使用cache 注解，请自行添加缓存策略
    @Override
    public String selectConfigByKey(String configKey) {
        return Optional.ofNullable(
                this.lambdaQuery()
                        .eq(SysConfig::getConfigKey, configKey)
                        .select(SysConfig::getConfigValue)
                        .one()
        )
                .map(SysConfig::getConfigValue)
                .orElse(StrUtil.EMPTY);
    }
}
