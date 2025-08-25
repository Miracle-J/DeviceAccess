package com.wx.websocket;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/**
 * 注册两个 WebSocket 端点，分别用于8677(运动控制)和8671(设备数据)。
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final DataWebSocketHandler motionHandler;
    private final DataWebSocketHandler deviceHandler;

    public WebSocketConfig(@Qualifier("motionHandler") DataWebSocketHandler motionHandler,
                           @Qualifier("deviceHandler") DataWebSocketHandler deviceHandler) {
        this.motionHandler = motionHandler;
        this.deviceHandler = deviceHandler;
    }

    /** 8677端口对应的 WebSocket 处理器 */
    @Bean("motionHandler")
    public DataWebSocketHandler motionHandler() {
        return new DataWebSocketHandler();
    }

    /** 8671端口对应的 WebSocket 处理器 */
    @Bean("deviceHandler")
    public DataWebSocketHandler deviceHandler() {
        return new DataWebSocketHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(motionHandler, "/ws/motion").setAllowedOrigins("*");
        registry.addHandler(deviceHandler, "/ws/device").setAllowedOrigins("*");
    }
}

