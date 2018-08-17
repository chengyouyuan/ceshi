package com.winhxd.b2c.message.sms.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 原始接受短信
 *
 * @author yaoshuai
 */
public class SmsRevice implements Serializable {

    private static final long serialVersionUID = -2865748399489988328L;
    private Long id;
    private String content;        //接受短信内容
    private Date reviceTime;        //接受时间
    private String telephone;    //发送短信号码
    private Date sendTime;    //发送时间
    private String channelNumber;    //为止，待测试确定
    private int customRead;            //客户端是否读取  1为读取  0为未读
    private String supplyId;        //供应商标识
    private String serialId;        //关键字  用于处理不同类型的短信  如：xxtx
    private long storeId;

    public String getSerialId() {
        return serialId;
    }

    public void setSerialId(String serialId) {
        this.serialId = serialId;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public void setReviceTime(Date reviceTime) {
        this.reviceTime = reviceTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Date getReviceTime() {
        return reviceTime;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(String channelNumber) {
        this.channelNumber = channelNumber;
    }

    public int getCustomRead() {
        return customRead;
    }

    public void setCustomRead(int customRead) {
        this.customRead = customRead;
    }

}
