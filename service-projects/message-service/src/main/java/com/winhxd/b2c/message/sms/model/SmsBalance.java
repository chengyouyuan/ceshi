package com.winhxd.b2c.message.sms.model;

import java.io.Serializable;
import java.util.Date;

public class SmsBalance implements Serializable {

    private static final long serialVersionUID = 4151851561998712802L;

    private Long id;
    private String supplyId;        //供应商标识
    private double balance;            //短信余额
    private Date queryTime;        //查询时间
    private int preSurplusSmsNum; //预计剩余短信条数

    public int getPreSurplusSmsNum() {
        return preSurplusSmsNum;
    }

    public void setPreSurplusSmsNum(int preSurplusSmsNum) {
        this.preSurplusSmsNum = preSurplusSmsNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Date getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(Date queryTime) {
        this.queryTime = queryTime;
    }
}
