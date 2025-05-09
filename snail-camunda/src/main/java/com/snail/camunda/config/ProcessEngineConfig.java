//package com.snail.camunda.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.snail.camunda.listener.LeaveProcessEventListener;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.camunda.bpm.engine.ProcessEngine;
//import org.camunda.bpm.engine.RepositoryService;
//import org.camunda.bpm.engine.RuntimeService;
//import org.camunda.bpm.engine.delegate.ExecutionListener;
//import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
//import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
//import org.camunda.bpm.engine.impl.jobexecutor.JobExecutor;
//import org.camunda.bpm.engine.repository.Deployment;
//import org.camunda.bpm.engine.repository.ProcessDefinition;
//import org.camunda.bpm.engine.variable.Variables;
//import org.camunda.bpm.engine.variable.value.ObjectValue;
//import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultJobConfiguration;
//import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.event.EventListener;
//import org.springframework.core.io.ClassPathResource;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * 流程引擎配置类
// * 用于配置流程引擎、注册监听器和自动部署流程
// */
///**
// * 流程引擎配置类
// * 用于配置流程引擎、注册监听器和自动部署流程
// */
//@Slf4j
//@Configuration
//@RequiredArgsConstructor
//public class ProcessEngineConfig {
//
//    private final RepositoryService repositoryService;
//    private final RuntimeService runtimeService;
//    private final LeaveProcessEventListener leaveProcessEventListener;
//    private final ProcessEngine processEngine;
//    private final ObjectMapper objectMapper;
//
//    private static final String LEAVE_PROCESS_KEY = "leave-approval";
//    private static final String LEAVE_PROCESS_BPMN = "bpmn/leave-approval.bpmn";
//
//    /**
//     * Camunda引擎启动后自动部署请假流程并注册监听器
//     */
//    @EventListener
//    public void onPostDeploy(PostDeployEvent event) {
//        log.info("Camunda引擎启动完成，开始部署请假流程并注册监听器");
//
//        // 部署请假流程
//        deployLeaveProcess();
//
//        // 注册流程监听器
//        registerProcessListeners();
//    }
//
//    /**
//     * 部署请假流程
//     */
//    private void deployLeaveProcess() {
//        try {
//            // 检查流程是否已部署
//            ProcessDefinition existingProcess = repositoryService.createProcessDefinitionQuery()
//                    .processDefinitionKey(LEAVE_PROCESS_KEY)
//                    .latestVersion()
//                    .singleResult();
//
//            if (existingProcess != null) {
//                log.info("请假流程已部署，版本: {}", existingProcess.getVersion());
//                return;
//            }
//
//            // 从classpath加载BPMN文件
//            ClassPathResource resource = new ClassPathResource(LEAVE_PROCESS_BPMN);
//            InputStream inputStream = resource.getInputStream();
//
//            // 部署流程
//            Deployment deployment = repositoryService.createDeployment()
//                    .addInputStream(LEAVE_PROCESS_BPMN, inputStream)
//                    .name("请假审批流程")
//                    .deploy();
//
//            // 获取流程定义
//            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                    .deploymentId(deployment.getId())
//                    .singleResult();
//
//            log.info("请假流程部署成功，流程定义ID: {}, 版本: {}",
//                    processDefinition.getId(), processDefinition.getVersion());
//
//        } catch (IOException e) {
//            log.error("部署请假流程失败", e);
//        }
//    }
//
//    /**
//     * 注册流程监听器
//     * 为流程定义注册全局事件监听器，而不是创建新的流程实例
//     */
//    private void registerProcessListeners() {
//        try {
//            // 获取流程定义
//            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
//                    .processDefinitionKey(LEAVE_PROCESS_KEY)
//                    .latestVersion()
//                    .singleResult();
//
//            if (processDefinition == null) {
//                log.warn("未找到请假流程定义，无法注册监听器");
//                return;
//            }
//
//            // 使用流程引擎配置注册全局监听器
//            ProcessEngineConfigurationImpl configuration = (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
//
//            // 注册全局流程实例启动监听器
//            if (!configuration.getCustomPreProcessEngineEventListeners().contains(leaveProcessEventListener)) {
//                configuration.getCustomPreProcessEngineEventListeners().add(leaveProcessEventListener);
//                log.info("注册全局流程实例启动监听器成功");
//            }
//
//            // 注册全局流程实例结束监听器
//            if (!configuration.getCustomPostProcessEngineEventListeners().contains(leaveProcessEventListener)) {
//                configuration.getCustomPostProcessEngineEventListeners().add(leaveProcessEventListener);
//                log.info("注册全局流程实例结束监听器成功");
//            }
//
//            // 注册流程定义级别的监听器（通过RuntimeService API）
//            try {
//                // 为特定流程定义注册监听器
//                runtimeService.addEventListener(leaveProcessEventListener, ExecutionListener.EVENTNAME_START);
//                runtimeService.addEventListener(leaveProcessEventListener, ExecutionListener.EVENTNAME_END);
//                log.info("为流程定义[{}]注册事件监听器成功", LEAVE_PROCESS_KEY);
//            } catch (Exception e) {
//                log.warn("通过RuntimeService注册监听器失败，将使用全局监听器: {}", e.getMessage());
//            }
//
//            log.info("请假流程监听器注册完成");
//
//        } catch (Exception e) {
//            log.error("注册流程监听器失败", e);
//        }
//    }
//
//    /**
//     * 获取流程引擎配置
//     * 用于访问和修改流程引擎的配置参数
//     *
//     * @return 流程引擎配置实现类
//     */
//    public ProcessEngineConfigurationImpl getProcessEngineConfiguration() {
//        return (ProcessEngineConfigurationImpl) processEngine.getProcessEngineConfiguration();
//    }
//
//    /**
//     * 创建可序列化的流程变量
//     * 用于在流程中传递复杂对象
//     *
//     * @param value 要序列化的对象
//     * @return 序列化后的流程变量值
//     */
//    public ObjectValue createSerializableVariable(Object value) {
//        return Variables.objectValue(value)
//                .serializationDataFormat(Variables.SerializationDataFormats.JSON)
//                .create();
//    }
//}