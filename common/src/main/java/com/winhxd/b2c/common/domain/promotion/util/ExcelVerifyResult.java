package com.winhxd.b2c.common.domain.promotion.util;

import java.io.Serializable;

public class ExcelVerifyResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4590590148122558701L;

	/**
	 * 行号
	 */
	private Integer lineNum;

	/**
	 * 错误信息
	 */
	private String errorMsg;

	public ExcelVerifyResult() {
		super();
	}

	public ExcelVerifyResult(Integer lineNum, String errorMsg) {
		super();
		this.lineNum = lineNum;
		this.errorMsg = errorMsg;
	}

	public Integer getLineNum() {
		return lineNum;
	}

	public void setLineNum(Integer lineNum) {
		this.lineNum = lineNum;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
