package com.snail.common.redis.utils;

import com.snial.common.core.constant.CacheConstants;

/**
 * 字典工具类
 *
 * @author Anfm
 * Created time 2025/5/12
 * @since 1.0
 */
public class DictUtils {

//    /**
//     * 设置字典缓存
//     *
//     * @param key       参数键
//     * @param dictDatas 字典数据列表
//     */
//    public static void setDictCache(String key, List<SysDictData> dictDatas) {
//        CacheUtils.put(CacheConstants.SYS_DICT_KEY, key, dictDatas);
//    }
//
//    /**
//     * 获取字典缓存
//     *
//     * @param key 参数键
//     * @return dictDatas 字典数据列表
//     */
//    public static List<SysDictData> getDictCache(String key) {
//        return CacheUtils.get(CacheConstants.SYS_DICT_KEY, key);
//    }

    /**
     * 删除指定字典缓存
     *
     * @param key 字典键
     */
    public static void removeDictCache(String key) {
        CacheUtils.evict(CacheConstants.SYS_DICT_KEY, key);
    }

    /**
     * 清空字典缓存
     */
    public static void clearDictCache() {
        CacheUtils.clear(CacheConstants.SYS_DICT_KEY);
    }
}
