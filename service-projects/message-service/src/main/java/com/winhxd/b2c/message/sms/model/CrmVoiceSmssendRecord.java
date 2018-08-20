package com.winhxd.b2c.message.sms.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: CrmVoiceSmssendRecord
 * @Description: 语音发送记录
 * @Author fanzhanzhan
 * @Date 2018-08-20 10:43
 **/
public class CrmVoiceSmssendRecord implements Serializable {

    private static final long serialVersionUID = 7889822018490029793L;
    
    private int recordId;
    /**
     * 来源 110注册  113找回密码
     */
    private String sources;
    private String mobile;
    private Date sendTime;
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
    public Date getSendTime() {
        return sendTime;
    }
    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }
    public String getSources() {
        return sources;
    }
    public void setSources(String sources) {
        this.sources = sources;
    }
}
