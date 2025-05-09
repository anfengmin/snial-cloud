package com.snail.camunda.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.camunda.domain.LeaveRequest;
import com.snail.camunda.mapper.LeaveRequestMapper;
import com.snail.camunda.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 请假申请服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeaveRequestServiceImpl extends ServiceImpl<LeaveRequestMapper, LeaveRequest> implements LeaveRequestService {

    private final LeaveRequestMapper leaveRequestMapper;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public LeaveRequest createLeaveRequest(LeaveRequest leaveRequest) {
        // 设置创建时间和更新时间
        Date now = new Date();
        leaveRequest.setCreateTime(now);
        leaveRequest.setUpdateTime(now);
        
        // 设置初始状态为进行中
        leaveRequest.setStatus(0);
        
        // 保存到数据库
        leaveRequestMapper.insert(leaveRequest);
        
        return leaveRequest;
    }

    @Override
    public LeaveRequest updateLeaveRequest(LeaveRequest leaveRequest) {
        // 更新时间
        leaveRequest.setUpdateTime(new Date());
        
        // 更新数据库
        leaveRequestMapper.updateById(leaveRequest);
        
        return leaveRequest;
    }

    @Override
    public LeaveRequest getByProcessInstanceId(String processInstanceId) {
        // 根据流程实例ID查询
        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveRequest::getProcessInstanceId, processInstanceId);
        
        return leaveRequestMapper.selectOne(wrapper);
    }

    @Override
    public List<LeaveRequest> getByApplicant(String applicant) {
        // 根据申请人查询
        LambdaQueryWrapper<LeaveRequest> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LeaveRequest::getApplicant, applicant);
        wrapper.orderByDesc(LeaveRequest::getCreateTime);
        
        return leaveRequestMapper.selectList(wrapper);
    }

    @Override
    public boolean updateStatus(String processInstanceId, Integer status) {
        // 查询请假申请
        LeaveRequest leaveRequest = getByProcessInstanceId(processInstanceId);
        if (leaveRequest == null) {
            log.error("未找到流程实例对应的请假申请: {}", processInstanceId);
            return false;
        }
        
        // 更新状态
        leaveRequest.setStatus(status);
        leaveRequest.setUpdateTime(new Date());
        
        return leaveRequestMapper.updateById(leaveRequest) > 0;
    }

    @Override
    public boolean updateActualDates(String processInstanceId, String actualStartDate, String actualEndDate) {
        // 查询请假申请
        LeaveRequest leaveRequest = getByProcessInstanceId(processInstanceId);
        if (leaveRequest == null) {
            log.error("未找到流程实例对应的请假申请: {}", processInstanceId);
            return false;
        }
        
        try {
            // 解析日期字符串
            Date startDate = DATE_FORMAT.parse(actualStartDate);
            Date endDate = DATE_FORMAT.parse(actualEndDate);
            
            // 更新实际日期
            leaveRequest.setActualStartDate(startDate);
            leaveRequest.setActualEndDate(endDate);
            leaveRequest.setUpdateTime(new Date());
            
            return leaveRequestMapper.updateById(leaveRequest) > 0;
        } catch (ParseException e) {
            log.error("日期格式解析错误", e);
            return false;
        }
    }
}