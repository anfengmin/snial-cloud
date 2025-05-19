package com.snail.common.web.config;

import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

/**
 * Undertow 自定义配置
 *
 * @author Levi.
 * Created time 2025/5/18
 * @since 1.0
 */
public class UndertowConfig implements WebServerFactoryCustomizer<UndertowServletWebServerFactory> {

    /**
     * 设置 Undertow 的 websocket 缓冲池
     *
     * @param factory factory
     * @since 1.0
     */
    @Override
    public void customize(UndertowServletWebServerFactory factory) {
        // 默认不直接分配内存 如果项目中使用了 websocket 建议直接分配
        factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(false, 512));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }

}
