package com.snail.sys.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.redis.utils.CacheUtils;
import com.snail.sys.dao.SysDictTypeDao;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.domain.SysDictType;
import com.snail.sys.dto.SysDictTypePageDTO;
import com.snail.sys.service.SysDictDataService;
import com.snail.sys.service.SysDictTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 字典类型(SysDictType)表服务实现类
 *
 * @author makejava
 * @since 2025-05-21 21:48:36
 */
@Service("sysDictTypeService")
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeDao, SysDictType> implements SysDictTypeService {

    @Resource
    private SysDictDataService sysDictDataService;


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysDictType> queryByPage(SysDictTypePageDTO dto) {

        // 创建分页对象
        Page<SysDictType> page = new Page<>(dto.getCurrent(), dto.getSize());
        // 构建查询条件并执行分页查询

        return this.lambdaQuery()
                .like(StrUtil.isNotBlank(dto.getDictName()), SysDictType::getDictName, dto.getDictName())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysDictType::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getDictType()), SysDictType::getDictType, dto.getDictType())
                .between(StrUtil.isNotBlank(dto.getBeginTime()) && StrUtil.isNotBlank(dto.getEndTime()),
                        SysDictType::getCreateTime,
                        StrUtil.isNotBlank(dto.getBeginTime()) ? DateUtil.parseDateTime(dto.getBeginTime()) : null,
                        StrUtil.isNotBlank(dto.getEndTime()) ? DateUtil.parseDateTime(dto.getEndTime()) : null)
                .page(page);
    }
    /**
     * checkDictTypeUnique
     *
     * @param sysDictType sysDictType
     * @return boolean
     * @since 1.0
     */
    @Override
    public boolean checkDictTypeUnique(SysDictType sysDictType) {
        return this.lambdaQuery().eq(SysDictType::getDictType, sysDictType.getDictType())
                .ne(ObjectUtil.isNotNull(sysDictType.getId()), SysDictType::getId, sysDictType.getId())
                .exists();
    }

    /**
     * 重置字典缓存数据
     *
     * @since 1.0
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 清空字典缓存数据
     *
     * @since 1.0
     */
    @Override
    public void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }

    /**
     * 加载字典缓存数据
     *
     * @since 1.0
     */
    @Override
    public void loadingDictCache() {
        List<SysDictData> list = sysDictDataService.lambdaQuery()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .list();
        Map<String, List<SysDictData>> dictDataMap = list.stream()
                .collect(Collectors.groupingBy(SysDictData::getDictType));

        dictDataMap.forEach((k, v) -> {
            List<SysDictData> dictList = ListUtil.sort(v, Comparator.comparing(SysDictData::getDictSort));
            CacheUtils.put(CacheNames.SYS_DICT, k, dictList);
        });

    }
}
