package com.winhxd.b2c.common.domain.promotion.util;

public class BaseExcelDomain extends BaseDomain implements IExcelEntity {

	protected String errorMsg;

	@Override
	public String getErrorMsg() {
		return errorMsg;
	}

	@Override
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
