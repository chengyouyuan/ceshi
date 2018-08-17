package com.winhxd.b2c.pay.weixin.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPay;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.constant.BillType;
import com.winhxd.b2c.pay.weixin.dao.PayStatementDownloadRecordMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementMapper;
import com.winhxd.b2c.pay.weixin.model.PayStatement;
import com.winhxd.b2c.pay.weixin.model.PayStatementDownloadRecord;
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
    
    //通信成功返回信息
    private static final String OK = "OK";
    
    //账单的资金来源账户
    //基本账户
    private static final String BASIC = "Basic";
    //运营账户
    private static final String OPERATION = "Operation";
    //手续费账户
    private static final String FEES = "Fees";
    
    
    //压缩账单：非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式。
    private static final String GZIP = "GZIP";
    
    @Autowired
    private WXPay wXPay;
    
    @Autowired
    private PayStatementMapper payStatementMapper;
    
    @Autowired
    private PayStatementDownloadRecordMapper payStatementDownloadRecordMapper;

    //入参时间格式
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	//微信返回的时间格式
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH：mm：ss");
	
	
	@Override
	public String downloadStatement() {

		Map<String, String> reqData = new HashMap<String, String>();
		Date billDate = DateUtils.addDays(new Date(), -1);
		reqData.put("bill_date", sdf.format(billDate));
		reqData.put("bill_type", ALL);
		logger.info(reqData.toString());
		try {
			Map<String, String> billMap = wXPay.downloadBill(reqData);
			
			PayStatementDownloadRecord record = new PayStatementDownloadRecord();
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.get("return_code"))) {
				record.setBillDate(billDate);
				record.setBillType(BillType.STATEMENT.getCode());
				record.setReturnCode(WXPayConstants.FAIL);
				//通信失败时只有return_msg，没有业务结果
				record.setReturnMsg(billMap.get("return_msg"));
				
			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.get("result_code"))) {

					record.setBillDate(billDate);
					record.setBillType(BillType.STATEMENT.getCode());
					record.setReturnCode(WXPayConstants.SUCCESS);
					record.setReturnMsg(OK);
					record.setResultCode(WXPayConstants.FAIL);
					record.setErrCode(billMap.get("err_code"));
					record.setErrCodeDes(billMap.get("err_code_des"));
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = billMap.get("data");
					//去掉表头
					String substring1 = data.substring(data.indexOf("`"));
					//去掉统计数据
					String substring2 = substring1.substring(0, substring1.indexOf("总")).replace("`", "");
					//把数据按条分割
					String[] dataArray = substring2.split("%");
					List<PayStatement> list = new ArrayList<PayStatement>();
					for (String everyData : dataArray) {
						//把每一条数据按逗号分割
						String[] everyDataArray = everyData.split(",");
						PayStatement statement = this.assemblePayStatement(everyDataArray, reqData.get("bill_type"), billDate);
						list.add(statement);
					}
					//保存数据
					for (PayStatement payStatement : list) {
						payStatementMapper.insertSelective(payStatement);
					}
					//业务成功，记录到记录表
					record.setBillDate(billDate);
					record.setBillType(BillType.STATEMENT.getCode());
					record.setReturnCode(WXPayConstants.SUCCESS);
					record.setReturnMsg(OK);
					record.setResultCode(WXPayConstants.SUCCESS);
				}
			}
			//保存记录表
			payStatementDownloadRecordMapper.insertSelective(record);
			
			
			
			
			
			
			logger.info(billMap.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @author yuluyuan
	 * @date 2018年8月16日 下午3:54:56
	 * @Description 组装PayStatement数据
	 * @param everyDataArray
	 * @return
	 * @throws ParseException 
	 */
	private PayStatement assemblePayStatement(String[] everyDataArray, String billType, Date billDate) throws ParseException {
		//把数据放入bean中
		PayStatement statement = new PayStatement();
		statement.setPayTime(sdf1.parse(everyDataArray[0]));
		statement.setAppid(everyDataArray[1]);
		statement.setMchId(everyDataArray[2]);
		statement.setSubMchId(everyDataArray[3]);
		statement.setDeviceId(everyDataArray[4]);
		statement.setWxOrderNo(everyDataArray[5]);
		statement.setOutOrderNo(everyDataArray[6]);
		statement.setUserIdentity(everyDataArray[7]);
		statement.setPayType(everyDataArray[8]);
		statement.setPayStatus(everyDataArray[9]);
		statement.setBankType(everyDataArray[10]);
		statement.setCurrencyType(everyDataArray[11]);
		statement.setTotalAmount(new BigDecimal(everyDataArray[12]));
		statement.setDiscountAmount(new BigDecimal(everyDataArray[13]));
		
		statement.setBillDate(billDate);
		
		if (ALL.equals(billType)) {
			//微信退款单号,商户退款单号,退款金额,代金券或立减优惠退款金额，退款类型，退款状态,商品名称,商户数据包,手续费,费率
			statement.setRefundWxOrderNo(everyDataArray[14]);
			statement.setRefundOutOrderNo(everyDataArray[15]);
			statement.setRefundAmount(new BigDecimal(everyDataArray[16]));
			statement.setRefundDiscountAmount(new BigDecimal(everyDataArray[17]));
			statement.setRefundType(everyDataArray[18]);
			statement.setRefundStatus(everyDataArray[19]);
			statement.setProdName(everyDataArray[20]);
			statement.setMchData(everyDataArray[21]);
			statement.setFee(new BigDecimal(everyDataArray[22]));
			//费率保存时，去掉百分号，值除以100后存入
			statement.setRate(Float.valueOf(everyDataArray[23]) / 100);
			
		} else if (SUCCESS.equals(billType)) {
			//商品名称,商户数据包,手续费,费率
			statement.setProdName(everyDataArray[14]);
			statement.setMchData(everyDataArray[15]);
			statement.setFee(new BigDecimal(everyDataArray[16]));
			//费率保存时，去掉百分号，值除以100后存入
			statement.setRate(Float.valueOf(everyDataArray[17]) / 100);
			
		} else if (REFUND.equals(billType)) {
			//退款申请时间,退款成功时间,微信退款单号,商户退款单号,退款金额,代金券或立减优惠退款金额,退款类型,退款状态,商品名称,商户数据包,手续费,费率
			statement.setrefundStartTime(sdf1.parse(everyDataArray[14]));
			statement.setRefundSuccessTime(sdf1.parse(everyDataArray[15]));
			statement.setRefundWxOrderNo(everyDataArray[16]);
			statement.setRefundOutOrderNo(everyDataArray[17]);
			statement.setRefundAmount(new BigDecimal(everyDataArray[18]));
			statement.setRefundDiscountAmount(new BigDecimal(everyDataArray[19]));
			statement.setRefundType(everyDataArray[20]);
			statement.setRefundStatus(everyDataArray[21]);
			statement.setProdName(everyDataArray[22]);
			statement.setMchData(everyDataArray[23]);
			statement.setFee(new BigDecimal(everyDataArray[24]));
			//费率保存时，去掉百分号，值除以100后存入
			statement.setRate(Float.valueOf(everyDataArray[25]) / 100);
		}
		return statement;
	}

	@Override
	public String downloadFundFlow() {

		Map<String, String> reqData = new HashMap<String, String>();
		reqData.put("bill_date", sdf.format(DateUtils.addDays(new Date(), -1)));
		reqData.put("account_type", BASIC);
		logger.info(reqData.toString());
		try {
			Map<String, String> billMap = wXPay.downloadFundFlow(reqData);
			
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.get("return_code"))) {

				
			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.get("result_code"))) {
					
					
				//成功则开始插入数据
				}else{
					
				}

			}
			
			
			
			
			
			
			
			logger.info(billMap.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			e.printStackTrace();
		}
		return null;
	}

}
