package com.wx.udp;

import java.util.Arrays;

/**
 * 华曙睿讯协议中单个STRU_MSG_ITEM消息的表示。该结构与集成文档中描述的C结构体相对应。
 */
public class HuaShuMessage {

    /**
     * 产线ID
     */
    private byte productionLineId; // u8ProductionLineId
    /**
     * 设备
     */
    private byte macId;            // u8MacId
    /**
     * 模块编号
     */
    private byte moduleId;         // u8ModuleId
    /**
     * 控制器编号
     */
    private byte cardId;           // u8CardId
    /**
     * 消息类型, 0:输入信号&位置信号 1输出信号 2告警信息 3错误信息 4轴转动信息
     */
    private int type;              // s32Type
    /**
     * 数据节点
     */
    private int index;             // s32Index
    /**
     * 值
     */
    private int value;             // s32Value
    /**
     * 数据
     */
    private final int[] data = new int[2]; // as32Data[2]
    /**
     * 96个输入信号状态
     */
    private int[] in;   // as32In[96] when type == 0
    /**
     * 16个轴的位置
     */
    private int[] pos;  // as32Pos[16] when type == 0

    public byte getProductionLineId() {
        return productionLineId;
    }

    public void setProductionLineId(byte productionLineId) {
        this.productionLineId = productionLineId;
    }

    public byte getMacId() {
        return macId;
    }

    public void setMacId(byte macId) {
        this.macId = macId;
    }

    public byte getModuleId() {
        return moduleId;
    }

    public void setModuleId(byte moduleId) {
        this.moduleId = moduleId;
    }

    public byte getCardId() {
        return cardId;
    }

    public void setCardId(byte cardId) {
        this.cardId = cardId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int[] getData() {
        return data;
    }

    public int[] getIn() {
        return in;
    }

    public void setIn(int[] in) {
        this.in = in;
    }

    public int[] getPos() {
        return pos;
    }

    public void setPos(int[] pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HuaShuMessage{")
          .append("productionLineId=").append(productionLineId)
          .append(", macId=").append(macId)
          .append(", moduleId=").append(moduleId)
          .append(", cardId=").append(cardId)
          .append(", type=").append(type)
          .append(", index=").append(index)
          .append(", value=").append(value)
          .append(", data=").append(Arrays.toString(data));
        if (in != null) {
            sb.append(", in=").append(Arrays.toString(in));
        }
        if (pos != null) {
            sb.append(", pos=").append(Arrays.toString(pos));
        }
        sb.append('}');
        return sb.toString();
    }
}

