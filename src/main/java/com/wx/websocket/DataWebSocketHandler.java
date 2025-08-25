package com.wx.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的 WebSocket 处理器，维护当前连接的会话，并广播包含 UDP 来源端口
 * 与解析数据的 JSON 消息。
 */
@Component
public class DataWebSocketHandler extends TextWebSocketHandler {

    /** 保存所有活跃的 WebSocket 会话 */
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    /** JSON 序列化工具 */
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    /**
     * 向所有已连接的客户端广播消息。
     *
     * @param data 解析后的 UDP 数据
     * @param port 数据来源的 UDP 端口
     */
    public void broadcast(Object data, int port) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("port", port);
        payload.put("data", data);
        try {
            String json = mapper.writeValueAsString(payload);
            TextMessage message = new TextMessage(json);
            sessions.forEach(session -> {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    // 忽略发送失败
                }
            });
        } catch (JsonProcessingException e) {
            // 忽略序列化异常
        }
    }
}

