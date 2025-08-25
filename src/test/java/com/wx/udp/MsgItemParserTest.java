package com.wx.udp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link MsgItemParser} 的单元测试，用于验证解析逻辑是否正确。
 */
public class MsgItemParserTest {
    /**
     * 将十六进制字符串转换为字节数组，便于构造测试数据。
     */
    private static byte[] hexToBytes(String hex) {
        String[] parts = hex.trim().split("\\s+");
        byte[] b = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            b[i] = (byte) Integer.parseInt(parts[i], 16);
        }
        return b;
    }

    /**
     * 解析给定的样例报文，并断言关键字段值。
     */
    @Test
    void parsesSampleData() {
        String hex = "CB 00 00 00 00 00 00 00 09 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 00 00 00 C0 17 00 00 C0 17 00 00 3F 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 35 39 35 30 38 32 31 49 30 32 31 34 30 00 00 00 00 00 00 00 35 39 35 30 38 32 31 49 30 32 31 34 30 00 00 00 00 00 00 00 35 39 35 30 38 32 31 49 30 32 31 34 30 00 00 00 00 00 00 00 31 31 36 36 32 32 32 32 33 33 33 33 34 00 00 00 00 00 00 00";
        byte[] data = hexToBytes(hex);
        MsgItem item = MsgItemParser.parse(data);
        assertEquals(203, item.getWorkshopId());
        assertEquals(0, item.getSpotChecks());
        assertEquals(2, item.getSurLazhasiTM());
        assertEquals("5950821I02140", item.getDetoSN());
        assertEquals("1166222233334", item.getBoxSN());
    }
}

