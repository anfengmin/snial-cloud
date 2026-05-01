package com.snail.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.snail.common.core.service.DictService;
import com.snail.sys.domain.SysDictData;
import com.snail.sys.service.SysDictDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Excel字典服务桥接
 *
 * @author Levi.
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class SysExcelDictService implements DictService {

    private final SysDictDataService sysDictDataService;

    @Override
    public String getDictLabel(String dictType, String dictValue, String separator) {
        if (StrUtil.isBlank(dictValue)) {
            return StrUtil.EMPTY;
        }
        Map<String, String> dictMap = getAllDictByDictType(dictType);
        List<String> labels = StrUtil.splitTrim(dictValue, separator);
        return labels.stream()
                .map(value -> dictMap.getOrDefault(value, value))
                .collect(Collectors.joining(separator));
    }

    @Override
    public String getDictValue(String dictType, String dictLabel, String separator) {
        if (StrUtil.isBlank(dictLabel)) {
            return StrUtil.EMPTY;
        }
        Map<String, String> dictMap = getAllDictByDictType(dictType);
        Map<String, String> reverseMap = new LinkedHashMap<>();
        dictMap.forEach((key, value) -> reverseMap.put(value, key));
        List<String> labels = StrUtil.splitTrim(dictLabel, separator);
        return labels.stream()
                .map(label -> reverseMap.getOrDefault(label, label))
                .collect(Collectors.joining(separator));
    }

    @Override
    public Map<String, String> getAllDictByDictType(String dictType) {
        List<SysDictData> dictDataList = sysDictDataService.queryDictDataByType(dictType);
        if (CollUtil.isEmpty(dictDataList)) {
            return Collections.emptyMap();
        }
        Map<String, String> dictMap = new LinkedHashMap<>();
        dictDataList.forEach(item -> dictMap.put(item.getDictValue(), item.getDictLabel()));
        return dictMap;
    }
}
