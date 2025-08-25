package com.wx.udp;

import java.util.Arrays;

/**
 * STRU_MSG_ITEM 结构体的 Java 映射。
 *
 * <p>该类将 UDP 报文中的设备数据字段一一映射为 Java 成员变量，
 * 并提供标准的 getter/setter 方法，便于在业务代码中访问和修改。
 * 字段含义与设备通讯协议保持一致。</p>
 */
public class MsgItem {
    /** 工房ID */
    private int workshopId;
    /** 产线ID */
    private int lineId;
    /** 设备ID */
    private int deviceId;
    /** 模块ID */
    private int partId;
    /** 数据类型 */
    private int type;
    /** 数据数值 */
    private int data;
    /** 设备状态：0停止，1启动，2暂停，3故障 */
    private int deviceState;
    /** 当班产量 */
    private int yieldCur;
    /** 总产量 */
    private int yieldTotal;
    /** 小时产能 */
    private int uph;
    /** 注塑废品数量 */
    private int zhuSuNGNum;
    /** 线卡废品数量 */
    private int xianKaNGNum;
    /** 产品米数，实际数据需除以10 */
    private int productTypeNum;
    /** 绕线数 */
    private int productCircleNum;
    /** A绕扎丝圈数 */
    private int tieWindCircleANum;
    /** B绕扎丝圈数 */
    private int tieWindCircleBNum;
    /** 抽检个数 */
    private int spotChecks;
    /** 脱模拉丝次数 */
    private int surLazhasiTM;
    /** 辅助数据数组，长度22 */
    private final int[] aux = new int[22];
    /** 模具序列号 DetoSN */
    private String detoSN;
    /** 设备序列号 SerialNo */
    private String serialNo;
    /** 夹具序列号 BaqSN */
    private String baqSN;
    /** 箱体序列号 BoxSN */
    private String boxSN;

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getPartId() {
        return partId;
    }

    public void setPartId(int partId) {
        this.partId = partId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(int deviceState) {
        this.deviceState = deviceState;
    }

    public int getYieldCur() {
        return yieldCur;
    }

    public void setYieldCur(int yieldCur) {
        this.yieldCur = yieldCur;
    }

    public int getYieldTotal() {
        return yieldTotal;
    }

    public void setYieldTotal(int yieldTotal) {
        this.yieldTotal = yieldTotal;
    }

    public int getUph() {
        return uph;
    }

    public void setUph(int uph) {
        this.uph = uph;
    }

    public int getZhuSuNGNum() {
        return zhuSuNGNum;
    }

    public void setZhuSuNGNum(int zhuSuNGNum) {
        this.zhuSuNGNum = zhuSuNGNum;
    }

    public int getXianKaNGNum() {
        return xianKaNGNum;
    }

    public void setXianKaNGNum(int xianKaNGNum) {
        this.xianKaNGNum = xianKaNGNum;
    }

    public int getProductTypeNum() {
        return productTypeNum;
    }

    public void setProductTypeNum(int productTypeNum) {
        this.productTypeNum = productTypeNum;
    }

    public int getProductCircleNum() {
        return productCircleNum;
    }

    public void setProductCircleNum(int productCircleNum) {
        this.productCircleNum = productCircleNum;
    }

    public int getTieWindCircleANum() {
        return tieWindCircleANum;
    }

    public void setTieWindCircleANum(int tieWindCircleANum) {
        this.tieWindCircleANum = tieWindCircleANum;
    }

    public int getTieWindCircleBNum() {
        return tieWindCircleBNum;
    }

    public void setTieWindCircleBNum(int tieWindCircleBNum) {
        this.tieWindCircleBNum = tieWindCircleBNum;
    }

    public int getSpotChecks() {
        return spotChecks;
    }

    public void setSpotChecks(int spotChecks) {
        this.spotChecks = spotChecks;
    }

    public int getSurLazhasiTM() {
        return surLazhasiTM;
    }

    public void setSurLazhasiTM(int surLazhasiTM) {
        this.surLazhasiTM = surLazhasiTM;
    }

    /**
     * 获取辅助数据数组。
     *
     * @return 长度为22的整型数组
     */
    public int[] getAux() {
        return aux;
    }

    public String getDetoSN() {
        return detoSN;
    }

    public void setDetoSN(String detoSN) {
        this.detoSN = detoSN;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getBaqSN() {
        return baqSN;
    }

    public void setBaqSN(String baqSN) {
        this.baqSN = baqSN;
    }

    public String getBoxSN() {
        return boxSN;
    }

    public void setBoxSN(String boxSN) {
        this.boxSN = boxSN;
    }

    @Override
    public String toString() {
        // 输出完整字段信息，便于调试查看
        return "MsgItem{" +
            "workshopId=" + workshopId +
            ", lineId=" + lineId +
            ", deviceId=" + deviceId +
            ", partId=" + partId +
            ", type=" + type +
            ", data=" + data +
            ", deviceState=" + deviceState +
            ", yieldCur=" + yieldCur +
            ", yieldTotal=" + yieldTotal +
            ", uph=" + uph +
            ", zhuSuNGNum=" + zhuSuNGNum +
            ", xianKaNGNum=" + xianKaNGNum +
            ", productTypeNum=" + productTypeNum +
            ", productCircleNum=" + productCircleNum +
            ", tieWindCircleANum=" + tieWindCircleANum +
            ", tieWindCircleBNum=" + tieWindCircleBNum +
            ", spotChecks=" + spotChecks +
            ", surLazhasiTM=" + surLazhasiTM +
            ", aux=" + Arrays.toString(aux) +
            ", detoSN='" + detoSN + '\'' +
            ", serialNo='" + serialNo + '\'' +
            ", baqSN='" + baqSN + '\'' +
            ", boxSN='" + boxSN + '\'' +
            "}";
    }
}

