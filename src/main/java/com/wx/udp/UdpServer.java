package com.wx.udp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wx.storage.LastDataService;
import com.wx.websocket.DataWebSocketHandler;

/**
 * 监听8677端口的简单UDP服务器，用于接收华曙睿讯消息并将其记录到控制台。
 */
@Component
public class UdpServer {

    private static final int PORT = 8677;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    /** 用于广播消息的 WebSocket 处理器 */
    private final DataWebSocketHandler webSocketHandler;
    /** 数据存储服务 */
    private final LastDataService lastDataService;
    private volatile boolean running = true;

    public UdpServer(@Qualifier("motionHandler") DataWebSocketHandler webSocketHandler,
                     LastDataService lastDataService) {
        this.webSocketHandler = webSocketHandler;
        this.lastDataService = lastDataService;
    }

    @PostConstruct
    public void start() {
        executor.execute(this::listen);
    }

    @PreDestroy
    public void stop() {
        running = false;
        executor.shutdownNow();
    }

    private void listen() {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            byte[] buf = new byte[1024 * 10];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            while (running) {
                socket.receive(packet);
                int len = packet.getLength();
                byte[] data = Arrays.copyOf(packet.getData(), len);
                process(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void process(byte[] data) {
        if (data.length < 8) {
            return; // 无效数据
        }
        if (data[0] != (byte) 0xF0 || data[1] != (byte) 0xF0) {
            return; // 错误的头部
        }
        ByteBuffer header = ByteBuffer.wrap(data, 2, 2).order(ByteOrder.LITTLE_ENDIAN);
        int payloadLen = header.getShort() & 0xFFFF;
        if (payloadLen + 8 != data.length) {
            return; // 长度不匹配
        }
        for (int i = data.length - 4; i < data.length; i++) {
            if (data[i] != 0) {
                return; // 无效的尾部
            }
        }
        byte[] payload = Arrays.copyOfRange(data, 4, 4 + payloadLen);
        HuaShuMessage msg = HuaShuMessageParser.parse(payload);
        System.out.println("Received message: " + msg);
        // 保存最新数据
        lastDataService.save(PORT, msg.getProductionLineId(), msg.getMacId(), msg);
        // 将消息广播给所有 WebSocket 客户端
        webSocketHandler.broadcast(msg, PORT);
    }
}

