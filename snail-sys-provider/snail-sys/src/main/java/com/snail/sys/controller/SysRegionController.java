package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.utils.R;
import com.snail.sys.service.SysRegionService;
import com.snail.sys.vo.RegionInfoVO;
import com.snail.sys.vo.RegionTreeVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 行政区划
 *
 * @author Levi.
 * @since 2026-05-03
 */
@Tag(name = "行政区划")
@RestController
@RequiredArgsConstructor
@RequestMapping("/region")
public class SysRegionController {

    private final SysRegionService sysRegionService;

    @SaCheckLogin
    @GetMapping("/tree")
    @Operation(summary = "查询省市区级联树")
    public R<List<RegionTreeVO>> tree() {
        return R.ok(sysRegionService.queryRegionTree());
    }

    @SaCheckLogin
    @GetMapping("/map")
    @Operation(summary = "查询行政区划编码映射")
    public R<Map<String, RegionInfoVO>> regionMap() {
        return R.ok(sysRegionService.queryRegionMap());
    }

    @SaCheckLogin
    @GetMapping("/version")
    @Operation(summary = "查询行政区划缓存版本")
    public R<Long> version() {
        return R.ok(sysRegionService.queryRegionVersion());
    }

    @SaCheckPermission("system:region:refreshCache")
    @DeleteMapping("/refreshCache")
    @Operation(summary = "刷新行政区划缓存")
    public R<Void> refreshCache() {
        sysRegionService.refreshRegionCache();
        return R.ok();
    }
}
