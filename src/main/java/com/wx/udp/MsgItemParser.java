package com.wx.udp;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * 将二进制报文解析为 {@link MsgItem} 对象的工具类。
 */
public final class MsgItemParser {
    /** 结构体总长度（字节） */
    public static final int LENGTH = 240;

    private MsgItemParser() {
    }

    /**
     * 解析字节数组，按照协议顺序填充 {@link MsgItem} 的各个字段。
     *
     * @param data 长度至少为 {@link #LENGTH} 的字节数组
     * @return 填充后的 {@link MsgItem}
     */
    public static MsgItem parse(byte[] data) {
        // 使用小端模式读取数据
        ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        MsgItem item = new MsgItem();
        int workshopId = buffer.getInt();
        item.setWorkshopId(workshopId);
        item.setLineId(buffer.getInt());
        item.setDeviceId(buffer.getInt());
        item.setPartId(buffer.getInt());
        item.setType(buffer.getInt());
        item.setData(buffer.getInt());
        item.setDeviceState(buffer.getInt());
        item.setYieldCur(buffer.getInt());
        item.setYieldTotal(buffer.getInt());
        item.setUph(buffer.getInt());
        item.setZhuSuNGNum(buffer.getInt());
        item.setXianKaNGNum(buffer.getInt());
        item.setProductTypeNum(buffer.getInt());
        item.setProductCircleNum(buffer.getInt());
        item.setTieWindCircleANum(buffer.getInt());
        item.setTieWindCircleBNum(buffer.getInt());
        item.setSpotChecks(buffer.getInt());
        item.setSurLazhasiTM(buffer.getInt());
        int[] aux = item.getAux();
        for (int i = 0; i < aux.length; i++) {
            aux[i] = buffer.getInt();
        }
        // 解析四个定长字符串字段
        byte[] tmp = new byte[20];
        buffer.get(tmp);
        item.setDetoSN(extractString(tmp));
        buffer.get(tmp);
        item.setSerialNo(extractString(tmp));
        buffer.get(tmp);
        item.setBaqSN(extractString(tmp));
        buffer.get(tmp);
        item.setBoxSN(extractString(tmp));
        return item;
    }

    /**
     * 从定长字节数组中提取以\0结尾的 ASCII 字符串。
     *
     * @param arr 源字节数组
     * @return 去除尾部\0后的字符串
     */
    private static String extractString(byte[] arr) {
        int len = 0;
        while (len < arr.length && arr[len] != 0) {
            len++;
        }
        return new String(arr, 0, len, StandardCharsets.US_ASCII);
    }
}
