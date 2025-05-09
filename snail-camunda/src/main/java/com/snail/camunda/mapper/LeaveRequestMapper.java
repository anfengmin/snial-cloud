package com.snail.camunda.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snail.camunda.domain.LeaveRequest;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请假申请数据访问层
 */
@Mapper
public interface LeaveRequestMapper extends BaseMapper<LeaveRequest> {
}