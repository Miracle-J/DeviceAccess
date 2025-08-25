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
 * Simple UDP server that listens on port 8671 and parses incoming datagrams
 * as {@link MsgItem} structures. Parsed messages are printed to the console.
 */
@Component
public class MsgItemUdpServer {

    private static final int PORT = 8671;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = true;

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
        if (data.length < MsgItemParser.LENGTH) {
            return; // 数据长度不足
        }
        byte[] payload = Arrays.copyOf(data, MsgItemParser.LENGTH);
        MsgItem item = MsgItemParser.parse(payload);
        System.out.println("Received message: " + item);
    }
}

