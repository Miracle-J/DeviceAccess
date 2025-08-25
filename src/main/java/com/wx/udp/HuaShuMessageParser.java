package com.wx.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 *  用于将二进制载荷解析为 {@link HuaShuMessage} 对象的工具类
 */
public final class HuaShuMessageParser {

    private HuaShuMessageParser() {
    }

    /**
     * 将不带帧格式的载荷（即不包含 F0F0 长度头和尾随零）解析为 {@link HuaShuMessage}
     *
     * @param payload 二进制载荷
     * @return 解析后的消息
     */
    public static HuaShuMessage parse(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload).order(ByteOrder.LITTLE_ENDIAN);
        HuaShuMessage msg = new HuaShuMessage();
        msg.setProductionLineId(buffer.get());
        msg.setMacId(buffer.get());
        msg.setModuleId(buffer.get());
        msg.setCardId(buffer.get());
        msg.setType(buffer.getInt());
        msg.setIndex(buffer.getInt());
        msg.setValue(buffer.getInt());
        msg.getData()[0] = buffer.getInt();
        msg.getData()[1] = buffer.getInt();
        if (msg.getType() == 0) {
            int[] in = new int[96];
            for (int i = 0; i < in.length; i++) {
                in[i] = buffer.getInt();
            }
            msg.setIn(in);
            int[] pos = new int[16];
            for (int i = 0; i < pos.length; i++) {
                pos[i] = buffer.getInt();
            }
            msg.setPos(pos);
        }
        return msg;
    }
}

