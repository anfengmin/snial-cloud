package com.snail.camunda.config;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultJobConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 作业执行器配置类
 * 用于优化Camunda作业执行器的性能和稳定性
 */
@Slf4j
@Configuration
public class JobExecutorConfiguration {

    /**
     * 配置作业执行器
     * 优化作业执行器参数，提高系统性能和稳定性
     */
    @Bean
    public DefaultJobConfiguration jobConfiguration() {
        return new DefaultJobConfiguration() {
            @Override
            public void customize(ProcessEngineConfigurationImpl configuration) {
                // 获取作业执行器
                JobExecutor jobExecutor = configuration.getJobExecutor();
                
                // 设置作业获取线程池大小
                jobExecutor.setMaxJobsPerAcquisition(5);
                
                // 设置作业获取等待时间（毫秒）
                jobExecutor.setWaitTimeInMillis(5000);
                
                // 设置作业锁定时间（毫秒）
                jobExecutor.setLockTimeInMillis(300000); // 5分钟
                
                // 设置作业执行线程池大小
                jobExecutor.setMaxPoolSize(5);
                
                // 设置核心线程池大小
                jobExecutor.setCorePoolSize(3);
                
                // 设置队列大小
                jobExecutor.setQueueSize(10);
                
                // 设置作业重试次数
                configuration.setDefaultNumberOfRetries(3);
                
                // 设置作业重试间隔（秒）
                configuration.setDefaultJobRetryTimeCycle("R3/PT10S");
                
                log.info("作业执行器配置完成，优化系统性能和稳定性");
            }
        };
    }
}