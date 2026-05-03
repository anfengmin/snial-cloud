package com.snail.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snail.sys.domain.SysAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 区域字典(SysAddress)表数据库访问层
 *
 * @author makejava
 * @since 2026-05-03
 */
@Mapper
public interface SysAddressDao extends BaseMapper<SysAddress> {
}
