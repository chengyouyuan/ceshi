package com.winhxd.b2c.message.sms.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 语音短信发送记录
 * */
public class CrmVoiceSmssendRecord implements Serializable {

    private static final long serialVersionUID = 7889822018490029793L;
    
    private int recordId;
    private String sources;// 来源 110注册  113找回密码
    private String mobile;
    private Date sendtime;
    public int getRecordId() {
        return recordId;
    }
    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }
    public String getMobile() {
        return mobile;
    }
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
    public Date getSendtime() {
        return sendtime;
    }
    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }
    public String getSources() {
        return sources;
    }
    public void setSources(String sources) {
        this.sources = sources;
    }
}
