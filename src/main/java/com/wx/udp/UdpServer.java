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

import org.springframework.stereotype.Component;

/**
 * Simple UDP server that listens on port 8671 for HuaShu RuiXun messages and
 * logs them to the console.
 */
@Component
public class UdpServer {

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
        if (data.length < 8) {
            return; // invalid
        }
        if (data[0] != (byte) 0xF0 || data[1] != (byte) 0xF0) {
            return; // wrong header
        }
        ByteBuffer header = ByteBuffer.wrap(data, 2, 2).order(ByteOrder.LITTLE_ENDIAN);
        int payloadLen = header.getShort() & 0xFFFF;
        if (payloadLen + 8 != data.length) {
            return; // length mismatch
        }
        for (int i = data.length - 4; i < data.length; i++) {
            if (data[i] != 0) {
                return; // invalid trailer
            }
        }
        byte[] payload = Arrays.copyOfRange(data, 4, 4 + payloadLen);
        HuaShuMessage msg = HuaShuMessageParser.parse(payload);
        System.out.println("Received message: " + msg);
    }
}

