package com.winhxd.b2c.pay.weixin.base.dto;

import lombok.Data;

/**
 * 下载对账单、资金账单返回对象
 * @author yuluyuan
 *
 * 2018年8月19日
 */
@Data
public class PayBillDownloadResponseDTO extends ResponseBase{

    /**
     * 业务结果:SUCCESS/FAIL
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
    
    /**
     * 返回数据
     */
    private String data;
    
//    ****************************   对账单错误码   start  ******************************************
	/** 
	 * 下载失败，请尝试再次查询。
	 * */
	public static final String SYSTEMERROR = "SYSTEMERROR";
	/**
	 * 参数错误，请重新检查
	 */
	public static final String INVALID_BILL_TYPE = "invalid bill_type";
	/**
	 * 参数错误，请重新检查
	 */
	public static final String DATA_FORMAT_ERROR = "data format error";
	/**
	 * 参数错误，请重新检查
	 */
	public static final String MISSING_PARAMETER = "missing parameter";
	/**
	 * 参数错误，请重新检查
	 */
	public static final String SIGN_ERROR = "SIGN ERROR";
	/**
	 * 账单不存在,请检查当前商户号在指定日期内是否有成功的交易。
	 */
	public static final String NO_BILL_EXIST = "No Bill Exist";
	/**
	 * 账单未生成,请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。
	 */
	public static final String BILL_CREATING = "Bill Creating";
	/**
	 * 账单压缩失败,账单压缩失败，请稍后重试
	 */
	public static final String COMPRESSGZIP_ERROR = "CompressGZip Error";
	/**
	 * 账单压缩失败,账单压缩失败，请稍后重试
	 */
	public static final String UNCOMPRESSGZIP_ERROR = "UnCompressGZip Error";

//  ****************************   对账单错误码   end  ******************************************

//  ****************************   资金账单错误码   start  ******************************************
	
	/**
	 * 参数错误，请对照文档的请求参数说明检查参数。
	 */
	public static final String PARAM_ERROR = "PARAM_ERROR";
	/**
	 * 账单不存在，请检查当前商户号在指定日期内是否有成功的交易。
	 */
	public static final String FINANCIAL_NO_BILL_EXIST = "NO_BILL_EXIST";
	/**
	 * 账单未生成，请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。
	 */
	public static final String FINANCIAL_BILL_CREATING = "BILL_CREATING";
	
//  ****************************   资金账单错误码   end  ******************************************
    
}
