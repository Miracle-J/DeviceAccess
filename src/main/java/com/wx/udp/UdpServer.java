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
        int offset = 0;
        while (offset + 8 <= data.length) {
            // 查找帧头
            if (data[offset] != (byte) 0xF0 || data[offset + 1] != (byte) 0xF0) {
                offset++;
                continue;
            }

            // 读取长度（小端）
            int payloadLen = ByteBuffer.wrap(data, offset + 2, 2)
                                       .order(ByteOrder.LITTLE_ENDIAN)
                                       .getShort() & 0xFFFF;
            int end = offset + 4 + payloadLen;
            if (end + 4 > data.length) {
                // 剩余数据不足以组成完整帧，跳出循环
                break;
            }

            // 校验尾部4个0x00
            boolean validTail = true;
            for (int i = end; i < end + 4; i++) {
                if (data[i] != 0) {
                    validTail = false;
                    break;
                }
            }
            if (!validTail) {
                offset++;
                continue;
            }

            byte[] payload = Arrays.copyOfRange(data, offset + 4, end);
            HuaShuMessage msg = HuaShuMessageParser.parse(payload);
            System.out.println("Received message: " + msg);
            // 保存最新数据
            lastDataService.save(PORT, msg.getProductionLineId(), msg.getMacId(), msg);
            // 将消息广播给所有 WebSocket 客户端
            webSocketHandler.broadcast(msg, PORT);

            offset = end + 4; // 处理下一帧
        }
    }
}

