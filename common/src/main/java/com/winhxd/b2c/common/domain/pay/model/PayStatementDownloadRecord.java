package com.winhxd.b2c.common.domain.pay.model;

import java.util.Date;
/**
 * 微信对账单、资金账单记录表
 * @author yuluyuan
 *
 * 2018年8月18日
 */
public class PayStatementDownloadRecord {
	
	public static final String DOWNLOAD_SUCCESS = "SUCCESS";
	
	public static final String DOWNLOAD_FAIL = "FAIL";
	
	public static final String DOWNLOADED = "DOWNLOADED";
	
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
	 * 错误代码
	 */
    private String errCode;

	/**
	 * 错误代码描述
	 */
    private String errCodeDes;
    
	/**
	 * 账单状态：0，失败；1成功
	 */
    private Integer status;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 账单类型枚举
	 * 
	 * @author yuluyuan
	 *
	 *         2018年8月16日
	 */
	public enum BillType {
	
		/**
		 * 对账单
		 */
		STATEMENT(1, "对账单"),
	
		/**
		 * 对账单统计
		 */
		STATEMENT_COUNT(2, "对账单统计"),
	
		/**
		 * 资金账单
		 */
		FINANCIAL_BILL(3, "资金账单"),
	
		/**
		 * 资金账单统计
		 */
		FINANCIAL_BILL_COUNT(4, "资金账单统计");
	
		private int code;
		private String text;
	
		private BillType(int code, String text) {
			this.code = code;
			this.text = text;
		}
	
		public int getCode() {
			return code;
		}
	
		public String getText() {
			return text;
		}
	}

	public enum RecordStatus {

		FAIL(0, "失败"), SUCCESS(1, "成功");
		
		int code;
		String desc;
		
		private RecordStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}

}