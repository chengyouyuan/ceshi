package com.winhxd.b2c.pay.weixin.service;


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
    String downloadStatement();

    /**
     * @author yuluyuan
     * @date 2018年8月15日 上午11:08:21
     * @Description 下载资金账单
     * @return
     */
    String downloadFundFlow();

}
