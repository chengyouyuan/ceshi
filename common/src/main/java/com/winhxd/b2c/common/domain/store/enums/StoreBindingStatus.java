package com.winhxd.b2c.common.domain.store.enums;

/**
 * 校验并绑定门店所返回的处理状态
 *
 * @author hupengtao
 * @date 2018/9/18 18:34
 */
public enum StoreBindingStatus {

    /**
     * 尚未绑定门店
     */
    NoneBinding((short) 0, "尚未绑定门店"),

    /**
     * 首次扫码绑定（进店时触发）
     */
    NewBinding((short) 1, "初次绑定门店成功"),

    /**
     * 二次扫码或进同一家店时返回此绑定状态
     */
    AdreadyBinding((short) 2, "已经绑定过该门店"),

    /**
     * 扫码其它门店（已存在绑定门店）时返回此状态
     */
    DifferenceBinding((short) 3, "已绑定其他门店");

    private short status;
    private String desc;

    StoreBindingStatus(short status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public short getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
