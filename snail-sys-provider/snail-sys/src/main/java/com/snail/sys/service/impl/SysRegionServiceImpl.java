package com.snail.sys.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.constant.CacheNames;
import com.snail.common.redis.utils.CacheUtils;
import com.snail.sys.dao.SysAddressDao;
import com.snail.sys.domain.SysAddress;
import com.snail.sys.service.SysRegionService;
import com.snail.sys.vo.RegionInfoVO;
import com.snail.sys.vo.RegionTreeVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 行政区划服务实现类
 *
 * @author Levi.
 * @since 2026-05-03
 */
@Service("sysRegionService")
public class SysRegionServiceImpl extends ServiceImpl<SysAddressDao, SysAddress> implements SysRegionService {

    private static final String REGION_TREE_CACHE_KEY = "tree";
    private static final String REGION_MAP_CACHE_KEY = "map";
    private static final String REGION_VERSION_CACHE_KEY = "version";

    /**
     * 省级节点在初始化脚本中挂在“中国”节点下，这里只对外返回省-市-区三级。
     */
    private static final Integer PROVINCE_LEVEL = 1;
    private static final Integer DISTRICT_LEVEL = 3;
    private static final Integer NORMAL_STATUS = 0;
    private static final Integer LEAF_TRUE = 1;

    @Override
    public List<RegionTreeVO> queryRegionTree() {
        List<RegionTreeVO> tree = CacheUtils.get(CacheNames.SYS_REGION, REGION_TREE_CACHE_KEY);
        if (ObjectUtil.isNotNull(tree)) {
            return tree;
        }
        return reloadRegionCache().tree;
    }

    @Override
    public Map<String, RegionInfoVO> queryRegionMap() {
        Map<String, RegionInfoVO> regionMap = CacheUtils.get(CacheNames.SYS_REGION, REGION_MAP_CACHE_KEY);
        if (ObjectUtil.isNotNull(regionMap)) {
            return regionMap;
        }
        return reloadRegionCache().regionMap;
    }

    @Override
    public Long queryRegionVersion() {
        Long version = CacheUtils.get(CacheNames.SYS_REGION, REGION_VERSION_CACHE_KEY);
        if (ObjectUtil.isNotNull(version)) {
            return version;
        }
        queryRegionTree();
        return CacheUtils.get(CacheNames.SYS_REGION, REGION_VERSION_CACHE_KEY);
    }

    @Override
    public void refreshRegionCache() {
        CacheUtils.clear(CacheNames.SYS_REGION);
        reloadRegionCache();
    }

    private RegionCache reloadRegionCache() {
        List<SysAddress> regionList = this.lambdaQuery()
                .eq(SysAddress::getStatus, NORMAL_STATUS)
                .between(SysAddress::getLevelType, PROVINCE_LEVEL, DISTRICT_LEVEL)
                .orderByAsc(SysAddress::getLevelType)
                .orderByAsc(SysAddress::getParentId)
                .orderByAsc(SysAddress::getSort)
                .orderByAsc(SysAddress::getId)
                .list();

        RegionCache regionCache = buildRegionCache(regionList);
        CacheUtils.put(CacheNames.SYS_REGION, REGION_TREE_CACHE_KEY, regionCache.tree);
        CacheUtils.put(CacheNames.SYS_REGION, REGION_MAP_CACHE_KEY, regionCache.regionMap);
        CacheUtils.put(CacheNames.SYS_REGION, REGION_VERSION_CACHE_KEY, System.currentTimeMillis());
        return regionCache;
    }

    private RegionCache buildRegionCache(List<SysAddress> regionList) {
        Map<String, RegionTreeVO> nodeMap = new LinkedHashMap<>();
        Map<String, RegionInfoVO> regionMap = new LinkedHashMap<>();

        for (SysAddress region : regionList) {
            String regionCode = String.valueOf(region.getId());
            nodeMap.put(regionCode, toRegionTree(region));
            regionMap.put(regionCode, toRegionInfo(region));
        }

        List<RegionTreeVO> tree = new ArrayList<>();
        for (RegionTreeVO node : nodeMap.values()) {
            RegionTreeVO parent = nodeMap.get(node.getParentId());
            if (ObjectUtil.equals(node.getLevel(), PROVINCE_LEVEL) || ObjectUtil.isNull(parent)) {
                tree.add(node);
                continue;
            }
            parent.getChildren().add(node);
        }

        sortRegionTree(tree);
        return new RegionCache(tree, regionMap);
    }

    private RegionTreeVO toRegionTree(SysAddress region) {
        RegionTreeVO vo = new RegionTreeVO();
        fillRegionInfo(vo, region);
        return vo;
    }

    private RegionInfoVO toRegionInfo(SysAddress region) {
        RegionInfoVO vo = new RegionInfoVO();
        fillRegionInfo(vo, region);
        return vo;
    }

    private void fillRegionInfo(RegionInfoVO vo, SysAddress region) {
        String regionCode = String.valueOf(region.getId());
        vo.setId(region.getId());
        vo.setParentId(region.getParentId());
        vo.setName(region.getName());
        vo.setLabel(region.getName());
        vo.setValue(regionCode);
        vo.setShortName(region.getShortName());
        vo.setFullName(region.getFullName());
        vo.setMergerName(region.getMergerName());
        vo.setLevel(region.getLevelType());
        vo.setLevelName(region.getLevelName());
        vo.setCityCode(region.getCityCode());
        vo.setZipCode(region.getZipCode());
        vo.setLng(region.getLng());
        vo.setLat(region.getLat());
        vo.setPinyin(region.getPinyin());
        vo.setInitial(region.getInitial());
        vo.setSort(region.getSort());
        vo.setIsLeaf(ObjectUtil.equals(region.getIsLeaf(), LEAF_TRUE)
                || ObjectUtil.equals(region.getLevelType(), DISTRICT_LEVEL));
    }

    private void sortRegionTree(List<RegionTreeVO> nodes) {
        nodes.sort((left, right) -> {
            int sortResult = compareLong(left.getSort(), right.getSort());
            if (sortResult != 0) {
                return sortResult;
            }
            return compareLong(left.getId(), right.getId());
        });

        for (RegionTreeVO node : nodes) {
            if (ObjectUtil.isNotEmpty(node.getChildren())) {
                sortRegionTree(node.getChildren());
            }
        }
    }

    private int compareLong(Long left, Long right) {
        if (ObjectUtil.equals(left, right)) {
            return 0;
        }
        if (ObjectUtil.isNull(left)) {
            return 1;
        }
        if (ObjectUtil.isNull(right)) {
            return -1;
        }
        return left.compareTo(right);
    }

    private static class RegionCache {
        private final List<RegionTreeVO> tree;
        private final Map<String, RegionInfoVO> regionMap;

        private RegionCache(List<RegionTreeVO> tree, Map<String, RegionInfoVO> regionMap) {
            this.tree = tree;
            this.regionMap = regionMap;
        }
    }
}
