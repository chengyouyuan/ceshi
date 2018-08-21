package com.winhxd.b2c.pay.weixin.service;

import java.util.List;

import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.model.PayStatement;
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
    String downloadStatement(DownloadStatementCondition condition);

    /**
     * @author yuluyuan
     * @date 2018年8月15日 上午11:08:21
     * @Description 下载资金账单
     * @return
     */
    String downloadFundFlow(DownloadStatementCondition condition);

	/**
	 * @Description 根据model条件查询数据
	 * @author yuluyuan
	 * @date 2018年8月20日 下午2:15:01
	 * @param record
	 * @return
	 */
	List<PayStatementDownloadRecord> findDownloadRecord(
			PayStatementDownloadRecord record);

	/**
	 * @Description 根据订单号查询成功支付的对账单
	 * @author yuluyuan
	 * @date 2018年8月21日 下午3:49:57
	 * @param outOrderNo
	 * @return
	 */
	PayStatement getPayStatementByOutOrderNo(String outOrderNo);

}
