package com.snail.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.utils.R;
import com.snail.common.storage.model.UploadResult;
import com.snail.common.storage.service.StorageManager;
import com.snail.sys.dao.SysOssDao;
import com.snail.sys.domain.SysOss;
import com.snail.sys.dto.SysOssPageDTO;
import com.snail.sys.service.SysOssService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:33
 */
@Service("sysOssService")
@RequiredArgsConstructor
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOss> implements SysOssService {

    private final StorageManager storageManager;


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public R<Page<SysOss>> queryByPage(SysOssPageDTO dto) {
        Page<SysOss> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysOss> result = this.lambdaQuery()
                .like(StrUtil.isNotBlank(dto.getOriginalName()), SysOss::getOriginalName, dto.getOriginalName())
                .like(StrUtil.isNotBlank(dto.getFileSuffix()), SysOss::getFileSuffix, dto.getFileSuffix())
                .like(StrUtil.isNotBlank(dto.getService()), SysOss::getService, dto.getService())
                .orderByDesc(SysOss::getCreateTime)
                .page(page);
        return R.ok(result);
    }

    @Override
    public R<SysOss> upload(MultipartFile file, String configKey) {
        UploadResult uploadResult = storageManager.upload(configKey, file);
        return R.ok(saveUploadResult(uploadResult));
    }

    @Override
    public SysOss uploadContent(byte[] content, String originalFilename, String contentType, String configKey) {
        UploadResult uploadResult = storageManager.upload(configKey, originalFilename, contentType, content);
        return saveUploadResult(uploadResult);
    }

    private SysOss saveUploadResult(UploadResult uploadResult) {
        SysOss sysOss = new SysOss();
        sysOss.setFileName(uploadResult.getObjectKey());
        sysOss.setOriginalName(uploadResult.getOriginalFilename());
        sysOss.setFileSuffix(uploadResult.getFileSuffix());
        sysOss.setUrl(uploadResult.getUrl());
        sysOss.setService(uploadResult.getService());
        this.save(sysOss);
        return sysOss;
    }
}
