package com.snail.camunda.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 序列化器配置类
 * 用于注册自定义的Jackson序列化器
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class SerializerConfiguration {

    private final CustomJacksonSerializer customJacksonSerializer;
    
    /**
     * 注册自定义序列化器的插件
     */
    @Bean
    public ProcessEnginePlugin serializerPlugin() {
        return new ProcessEnginePlugin() {
            @Override
            public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                // 设置默认序列化格式为JSON
                processEngineConfiguration.setDefaultSerializationFormat(Variables.SerializationDataFormats.JSON.getName());
                
                // 注册自定义序列化器
                processEngineConfiguration.getVariableSerializers().addSerializer(customJacksonSerializer);
                
                log.info("自定义Jackson序列化器注册成功");
            }

            @Override
            public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
                // 后初始化配置
            }

            @Override
            public void postProcessEngineBuild(ProcessEngine processEngine) {
                // 流程引擎构建后的处理
            }
        };
    }
}