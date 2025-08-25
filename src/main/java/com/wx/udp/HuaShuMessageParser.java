package com.wx.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility class for parsing binary payloads into {@link HuaShuMessage} objects.
 */
public final class HuaShuMessageParser {

    private HuaShuMessageParser() {
    }

    /**
     * Parse a payload without framing (i.e. without the F0F0 length header and
     * trailing zeros) into a {@link HuaShuMessage}.
     *
     * @param payload binary payload
     * @return parsed message
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

