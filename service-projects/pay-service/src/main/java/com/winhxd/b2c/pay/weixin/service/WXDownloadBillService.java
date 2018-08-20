package com.winhxd.b2c.pay.weixin.service;

import java.util.Date;
import java.util.List;

import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;


/**
 * 下载对账单、资金账单
 * @author yuluyuan
 *
 * 2018年8月15日
 */
public interface WXDownloadBillService {

    /**
     * @author yuluyuan
     * @date 2018年8月15日 上午11:08:21
     * @Description 下载对账单
     * @return
     */
    String downloadStatement(Date billDate);

    /**
     * @author yuluyuan
     * @date 2018年8月15日 上午11:08:21
     * @Description 下载资金账单
     * @return
     */
    String downloadFundFlow(Date billDate);

	/**
	 * @Description 根据model条件查询数据
	 * @author yuluyuan
	 * @date 2018年8月20日 下午2:15:01
	 * @param record
	 * @return
	 */
	List<PayStatementDownloadRecord> findDownloadRecord(
			PayStatementDownloadRecord record);

}
