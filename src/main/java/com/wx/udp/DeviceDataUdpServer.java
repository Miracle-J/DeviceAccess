package com.wx.udp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

/**
 * 监听 8671 端口的 UDP 服务器，用于接收设备上报的数据。
 *
 * <p>每个接收到的数据报都会被解析为 {@link MsgItem} 对象并输出到控制台，
 * 后续可根据业务需要进行持久化或其他处理。</p>
 */
@Component
public class DeviceDataUdpServer {

    /** 设备数据监听端口 */
    private static final int PORT = 8671;
    /** 单线程执行器用于后台监听 */
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    /** 标记服务器运行状态 */
    private volatile boolean running = true;

    /** 容器启动后自动调用，开启监听线程 */
    @PostConstruct
    public void start() {
        executor.execute(this::listen);
    }

    /** 容器销毁前调用，停止监听 */
    @PreDestroy
    public void stop() {
        running = false;
        executor.shutdownNow();
    }

    /** 循环接收 UDP 数据包 */
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

    /**
     * 处理单个数据报，将其解析为 {@link MsgItem}。
     *
     * @param data 原始字节数组
     */
    private void process(byte[] data) {
        if (data.length < MsgItemParser.LENGTH) {
            return; // 数据长度不足
        }
        byte[] payload = Arrays.copyOf(data, MsgItemParser.LENGTH);
        MsgItem item = MsgItemParser.parse(payload);
        System.out.println("Received message: " + item);
    }
}

