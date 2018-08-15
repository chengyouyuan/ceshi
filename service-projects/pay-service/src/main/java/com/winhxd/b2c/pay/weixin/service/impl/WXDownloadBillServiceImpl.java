package com.winhxd.b2c.pay.weixin.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPay;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;

/**
 * 
 * @author yuluyuan
 *
 * 2018年8月15日
 */
@Service(value = "WXDownloadBillService")
public class WXDownloadBillServiceImpl implements WXDownloadBillService {

    private static final Logger logger = LoggerFactory.getLogger(WXTransfersServiceImpl.class);
    
    //返回当日所有订单信息，默认值
    private static final String ALL = "ALL";
    //返回当日成功支付的订单
    private static final String SUCCESS = "SUCCESS";
    //返回当日退款订单
    private static final String REFUND = "REFUND";
    //返回当日充值退款订单
    private static final String RECHARGE_REFUND = "RECHARGE_REFUND";
    
    //压缩账单：非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
    private static final String GZIP = "GZIP";
    
    @Autowired
    private WXPay wXPay;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
	@Override
	public String downloadStatement() {

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("bill_date", sdf.format(new Date()));
		reqData.put("bill_type", ALL);
		logger.info(reqData.toString());
		try {
			Map<String, String> downloadBill = wXPay.downloadBill(reqData);
			logger.info(downloadBill.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String downloadFinancialBill() {
		// TODO Auto-generated method stub
		return null;
	}

}
