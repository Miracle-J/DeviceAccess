package com.wx.udp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.wx.storage.LastDataService;
import com.wx.websocket.DataWebSocketHandler;

/**
 * 监听8671端口的UDP服务器，用于接收设备数据。
 *
 * <p>每个接收到的数据报都被解析为 {@link MsgItem} 实例并打印到控制台。</p>
 */
@Component
public class DeviceDataUdpServer {

    private static final int PORT = 8671;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    /** 用于广播消息的 WebSocket 处理器 */
    private final DataWebSocketHandler webSocketHandler;
    /** 数据存储服务 */
    private final LastDataService lastDataService;
    private volatile boolean running = true;

    public DeviceDataUdpServer(@Qualifier("deviceHandler") DataWebSocketHandler webSocketHandler,
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
//    // 添加调试信息
//    System.out.println("接收到的数据长度: " + data.length);
//    if (data.length >= 4) {
//        System.out.println("前4个字节: " + String.format("%02X %02X %02X %02X", data[0], data[1], data[2], data[3]));
//        int rawWorkshopId = (data[0] & 0xFF) | ((data[1] & 0xFF) << 8) | ((data[2] & 0xFF) << 16) | ((data[3] & 0xFF) << 24);
//        System.out.println("直接解析的workshopId值: " + rawWorkshopId);
//    }

    if (data.length < MsgItemParser.LENGTH) {
        System.out.println("数据长度不足，期望: " + MsgItemParser.LENGTH + ", 实际: " + data.length);
        return;
    }
    byte[] payload = Arrays.copyOf(data, MsgItemParser.LENGTH);
    MsgItem item = MsgItemParser.parse(payload);
    System.out.println("解析后的消息: " + item);
    // 保存最新数据
    lastDataService.save(PORT, item.getLineId(), item.getDeviceId(), item);
    // 将消息广播给所有 WebSocket 客户端
    webSocketHandler.broadcast(item, PORT);
}

}

