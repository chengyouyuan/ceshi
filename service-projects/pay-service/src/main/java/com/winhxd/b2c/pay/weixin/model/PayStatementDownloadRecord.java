package com.winhxd.b2c.pay.weixin.model;

import java.util.Date;
/**
 * 微信对账单、资金账单记录表
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayStatementDownloadRecord {
	
	/**
	 * 主键
	 */
    private Long id;

	/**
	 * 账单类型：1对账单表，2对账单统计表，3资金账单表，4资金账单统计表
	 */
    private Integer billType;

	/**
	 * 账单日期
	 */
    private Date billDate;

	/**
	 * 状态码：SUCCESS/FAIL
	 */
    private String returnCode;

	/**
	 * 返回信息   例如:签名失败；参数格式校验错误
	 */
    private String returnMsg;

	/**
	 * 业务结果  FAIL：此字段是业务标识，表示业务是否成功。目前只在失败时返回这个字段，所以只会出现FAIL值
	 */
    private String resultCode;

	/**
	 * 错误代码
	 */
    private String errCode;

	/**
	 * 错误代码描述
	 */
    private String errCodeDes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getBillType() {
        return billType;
    }

    public void setBillType(Integer billType) {
        this.billType = billType;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }
}