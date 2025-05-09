package com.snail.camunda.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.impl.variable.serializer.JavaObjectSerializer;
import org.camunda.bpm.engine.impl.variable.serializer.TypedValueSerializer;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.engine.variable.impl.value.ObjectValueImpl;
import org.camunda.bpm.engine.variable.type.ValueType;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.bpm.engine.variable.value.TypedValue;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义Jackson序列化器
 * 用于将复杂对象序列化为JSON格式存储在流程变量中
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CustomJacksonSerializer extends JavaObjectSerializer {

    private final ObjectMapper objectMapper;
    
    @Override
    public String getSerializationDataformat() {
        return Variables.SerializationDataFormats.JSON.getName();
    }

    @Override
    public ObjectValue readValue(ValueType valueType, InputStream inputStream, boolean deserializeValue) {
        try {
            // 读取输入流中的字节
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            byte[] bytes = outputStream.toByteArray();
            
            // 创建对象值
            ObjectValueImpl objectValue = new ObjectValueImpl(null, bytes, getSerializationDataformat(), valueType);
            
            // 如果需要反序列化
            if (deserializeValue && bytes.length > 0) {
                Object deserializedObject = objectMapper.readValue(bytes, Object.class);
                objectValue.setValue(deserializedObject);
            }
            
            return objectValue;
        } catch (Exception e) {
            log.error("使用Jackson反序列化对象失败", e);
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    protected byte[] serializeToByteArray(Object value) throws Exception {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (IOException e) {
            log.error("使用Jackson序列化对象失败", e);
            throw new RuntimeException("序列化失败: " + e.getMessage(), e);
        }
    }

    @Override
    protected Object deserializeFromByteArray(byte[] bytes, ValueType valueType) throws Exception {
        try {
            return objectMapper.readValue(bytes, Object.class);
        } catch (IOException e) {
            log.error("使用Jackson反序列化对象失败", e);
            throw new RuntimeException("反序列化失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 创建可序列化的流程变量
     * 
     * @param value 要序列化的对象
     * @return 序列化后的流程变量值
     */
    public ObjectValue createSerializableVariable(Object value) {
        return Variables.objectValue(value)
                .serializationDataFormat(getSerializationDataformat())
                .create();
    }
}