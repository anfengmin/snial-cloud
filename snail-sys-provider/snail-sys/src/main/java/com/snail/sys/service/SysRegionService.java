package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysAddress;
import com.snail.sys.vo.RegionInfoVO;
import com.snail.sys.vo.RegionTreeVO;

import java.util.List;
import java.util.Map;

/**
 * 行政区划服务接口
 *
 * @author Levi.
 * @since 2026-05-03
 */
public interface SysRegionService extends IService<SysAddress> {

    /**
     * 查询省市区级联树
     *
     * @return 省市区级联树
     */
    List<RegionTreeVO> queryRegionTree();

    /**
     * 查询行政区划编码映射
     *
     * @return key为区域编码的行政区划映射
     */
    Map<String, RegionInfoVO> queryRegionMap();

    /**
     * 查询行政区划缓存版本
     *
     * @return 缓存版本
     */
    Long queryRegionVersion();

    /**
     * 刷新行政区划缓存
     */
    void refreshRegionCache();
}
