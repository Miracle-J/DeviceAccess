package com.wx.udp;

import java.util.Arrays;

/**
 * Representation of a single STRU_MSG_ITEM message from the HuaShu RuiXun
 * protocol. The structure mirrors the C struct described in the integration
 * documentation.
 */
public class HuaShuMessage {

    private byte productionLineId; // u8ProductionLineId
    private byte macId;            // u8MacId
    private byte moduleId;         // u8ModuleId
    private byte cardId;           // u8CardId
    private int type;              // s32Type
    private int index;             // s32Index
    private int value;             // s32Value
    private final int[] data = new int[2]; // as32Data[2]
    private int[] in;   // as32In[96] when type == 0
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

