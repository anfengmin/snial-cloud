-- 请假申请表
CREATE TABLE IF NOT EXISTS `leave_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `process_instance_id` varchar(64) NOT NULL COMMENT '流程实例ID',
  `business_key` varchar(64) NOT NULL COMMENT '业务标识',
  `applicant` varchar(64) NOT NULL COMMENT '申请人',
  `name` varchar(50) NOT NULL COMMENT '申请人姓名',
  `leave_type` varchar(20) NOT NULL COMMENT '请假类型（sick-病假，annual-年假，personal-事假）',
  `start_date` datetime NOT NULL COMMENT '开始日期',
  `end_date` datetime NOT NULL COMMENT '结束日期',
  `reason` varchar(500) DEFAULT NULL COMMENT '请假原因',
  `actual_start_date` datetime DEFAULT NULL COMMENT '实际开始日期',
  `actual_end_date` datetime DEFAULT NULL COMMENT '实际结束日期',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '流程状态（0-进行中，1-已完成，2-已取消）',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_process_instance_id` (`process_instance_id`),
  KEY `idx_applicant` (`applicant`),
  KEY `idx_business_key` (`business_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='请假申请表';