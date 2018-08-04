package com.winhxd.b2c.common.domain.condition;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 接收手机端参数
 * 手机端接口继承该类
 *
 * @author yuluyuan
 */
public class MobileCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "记录订单下单渠道,如ios,android等")
    private String platform;

    @ApiModelProperty(value = "did号")
    private String did;

    @ApiModelProperty(value = "门店下单时所用设备")
    private String imei;

    @ApiModelProperty(value = "src")
    private String src;

    @ApiModelProperty(value = "版本号")
    private String ver;

    @ApiModelProperty(value = "lang")
    private String lang;

    @ApiModelProperty(value = "安卓模拟器ID，用于记录日志使用")
    private String emulatordid;

    @ApiModelProperty(value = "数盟ID，用于判断手机是否多终端登录")
    private String smDid;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getEmulatordid() {
        return emulatordid;
    }

    public void setEmulatordid(String emulatordid) {
        this.emulatordid = emulatordid;
    }

    public String getSmDid() {
        return smDid;
    }

    public void setSmDid(String smDid) {
        this.smDid = smDid;
    }
}