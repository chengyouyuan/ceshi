package com.winhxd.b2c.common.domain.promotion.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;

public class ImportResult<T> {

	/**
	 * 结果集
	 */
	private List<T> list;

	/**
	 * 是否存在校验失败
	 */
	private boolean verfiyFail;

	/**
	 * 错误信息
	 */
	private String errorMsg;

	/**
	 * 数据源
	 */
	private Workbook workbook;

	public ImportResult() {

	}

	public ImportResult(List<T> list, boolean verfiyFail, Workbook workbook) {
		this.list = list;
		this.verfiyFail = verfiyFail;
		this.workbook = workbook;
	}

	public List<T> getList() {
		return list;
	}

	/**
	 * @author 魏冰
	 * @date 2017年4月17日 下午6:52:53
	 * @Description 获取前{0}条校验失败的数据信息
	 * @version 1.0
	 * @param limit 前N条校验失败数据
	 * @return
	 */
	public List<ExcelVerifyResult> getInvalidList(int limit) {
		List<ExcelVerifyResult> invalidList = new ArrayList<>();
		BaseExcelDomain excelDomain;
		String errorMsg;
		for (int i = 0; i < list.size(); i++) {
			if (invalidList.size() >= limit) {
				break;
			}
			excelDomain = (BaseExcelDomain) list.get(i);
			errorMsg = excelDomain.getErrorMsg();
			if (StringUtils.isNotBlank(errorMsg)) {
				invalidList.add(new ExcelVerifyResult(i + 1, errorMsg));
			}
		}
		return invalidList;
	}

	public Workbook getWorkbook() {
		return workbook;
	}

	public boolean isVerfiyFail() {
		return verfiyFail;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public void setVerfiyFail(boolean verfiyFail) {
		this.verfiyFail = verfiyFail;
	}

	public void setWorkbook(Workbook workbook) {
		this.workbook = workbook;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
