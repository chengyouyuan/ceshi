package com.winhxd.b2c.pay.weixin.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.pay.weixin.base.dto.PayFinancialBillDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayStatementDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConfig;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.impl.WXPayApiImpl;
import com.winhxd.b2c.pay.weixin.constant.BillType;
import com.winhxd.b2c.pay.weixin.dao.PayFinancialBillCountMapper;
import com.winhxd.b2c.pay.weixin.dao.PayFinancialBillMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementCountMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementDownloadRecordMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementMapper;
import com.winhxd.b2c.pay.weixin.model.PayFinancialBill;
import com.winhxd.b2c.pay.weixin.model.PayFinancialBillCount;
import com.winhxd.b2c.pay.weixin.model.PayStatement;
import com.winhxd.b2c.pay.weixin.model.PayStatementCount;
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
    
    /**
     * 通信成功返回信息
     */
    private static final String OK = "OK";
    
    /**
     * 微信侧返回数据的分隔符
     */
    private static final String DATA_SEPARATE = "`";
    
    /**
     * 对账单去掉尾部标识符
     */
    private static final String STATEMENT_REMOVE_TAIL = "总";
    
    /**
     * 资金账单去掉尾部标识符
     */
    private static final String FINANCIAL_BILL_REMOVE_TAIL = "资金";
    
    /**
     * 对账单统计数据 去掉头部标识符
     */
    private static final String STATEMENT_COUNT_REMOVE_HEAD = "额";
    
    /**
     * 微信入参时间格式
     */
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    /**
     * 微信返参
     */
    private static final String RETURN_CODE = "return_code";
    
    /**
     * 微信返参
     */
    private static final String RETURN_MSG = "return_msg";
    
    /**
     * 微信返参
     */
    private static final String RESULT_CODE = "result_code";
    
    /**
     * 微信返参
     */
    private static final String ERR_CODE = "err_code";
    
    /**
     * 微信返参
     */
    private static final String ERR_CODE_DES = "err_code_des";
    
    /**
     * 微信返回的时间格式，不确定是哪个，例：2014-11-1016：33：45           2018-02-01 04:21:23
     */
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH：mm：ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private WXPayApiImpl wXPayApiImpl;
    
    @Autowired
    private WXPayConfig wXPayConfig;
    
    @Autowired
    private PayStatementMapper payStatementMapper;
    
    @Autowired
    private PayStatementCountMapper payStatementCountMapper;
    
    @Autowired
    private PayFinancialBillMapper payFinancialBillMapper;
    
    @Autowired
    private PayFinancialBillCountMapper payFinancialBillCountMapper;
    
    @Autowired
    private PayStatementDownloadRecordMapper payStatementDownloadRecordMapper;
	
	
	@Override
	public String downloadStatement() {
		Date billDate = DateUtils.addDays(new Date(), -1);
		
		PayStatementDTO dto = new PayStatementDTO();
		dto.setBillDate(sdf.format(billDate));
		dto.setBillType(PayStatementDTO.BillType.ALL.getText());
		
		try {
			Map<String, String> billMap = wXPayApiImpl.downloadBill(dto);
			
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.get(RETURN_CODE))) {
				
				this.dealRequestFail(billDate, BillType.STATEMENT.getCode(), billMap.get(RETURN_MSG));
				this.dealRequestFail(billDate, BillType.STATEMENT_COUNT.getCode(), billMap.get(RETURN_MSG));
				logger.info("对账单下载失败，返回信息：{}", billMap.get(RETURN_CODE));

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.get(RESULT_CODE))) {
					
					this.dealBusiFail(billDate, BillType.STATEMENT.getCode(), billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					this.dealBusiFail(billDate, BillType.STATEMENT_COUNT.getCode(), billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					logger.info("对账单下载失败，错误码：{}；错误信息：{}", billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = billMap.get("data");
					//去掉表头
					String substring1 = data.substring(data.indexOf(DATA_SEPARATE));
					//去掉统计数据
					String substring2 = substring1.substring(0, substring1.indexOf(STATEMENT_REMOVE_TAIL)).replace(DATA_SEPARATE, "");
					//把数据按条分割
					substring2 = substring2.replace("\r\n", "\n");
					String[] dataArray = substring2.split("\n");
//					String lineSeparator = System.lineSeparator();
					List<PayStatement> list = new ArrayList<PayStatement>();
					for (String everyData : dataArray) {
						//把每一条数据按逗号分割
						String[] everyDataArray = everyData.split(",");
						PayStatement statement = this.assemblePayStatement(everyDataArray, dto.getBillType(), billDate);
						list.add(statement);
					}
					//保存数据
					//此处list过大需要分批次插入
					for (PayStatement payStatement : list) {
						payStatementMapper.insertSelective(payStatement);
					}
					//业务成功，记录到记录表
					this.dealSuccess(billDate, BillType.STATEMENT.getCode());
					logger.info("对账单插入成功");
					
//					=====================处理统计数据=================================================================================

					String totalData = data.substring(data.lastIndexOf(STATEMENT_COUNT_REMOVE_HEAD)+1, data.length()).replace(DATA_SEPARATE, "");

					String[] totalDataArray = totalData.split(",");
					PayStatementCount statementCount = this.assemblePayStatementCount(totalDataArray, billDate);
					payStatementCountMapper.insertSelective(statementCount);

					//业务成功，记录到记录表
					this.dealSuccess(billDate, BillType.STATEMENT_COUNT.getCode());
					logger.info("对账单统计数据插入成功");
					
				}
			}
			
			logger.info(billMap.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	/**
	 * @Description 通信失败后，记录到记录表
	 * @author yuluyuan
	 * @date 2018年8月18日 上午11:12:37
	 * @param billDate
	 * @param billType
	 * @param returnMsg
	 * @return
	 */
	private void dealRequestFail(Date billDate, int billType,
			String returnMsg) {
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setReturnCode(WXPayConstants.FAIL);
		//通信失败时只有return_msg，没有业务结果
		record.setReturnMsg(returnMsg);
		//保存记录表
		payStatementDownloadRecordMapper.insertSelective(record);
	}

	/**
	 * @Description 业务失败后，记录到记录表
	 * @author yuluyuan
	 * @date 2018年8月18日 上午11:08:44
	 * @param billDate
	 * @param billType
	 * @param errCode
	 * @param errCodeDes
	 * @return
	 */
	private void dealBusiFail(Date billDate, int billType,
			String errCode, String errCodeDes) {
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setReturnCode(WXPayConstants.SUCCESS);
		record.setReturnMsg(OK);
		record.setResultCode(WXPayConstants.FAIL);
		record.setErrCode(errCode);
		record.setErrCodeDes(errCodeDes);
		//保存记录表
		payStatementDownloadRecordMapper.insertSelective(record);
	}

	/**
	 * @Description 数据插入成功后，记录到记录表
	 * @author yuluyuan
	 * @date 2018年8月18日 上午11:03:50
	 * @param billDate
	 * @param billType
	 * @return
	 */
	private void dealSuccess(Date billDate, int billType) {
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record .setBillDate(billDate);
		record.setBillType(billType);
		record.setReturnCode(WXPayConstants.SUCCESS);
		record.setReturnMsg(OK);
		record.setResultCode(WXPayConstants.SUCCESS);
		//保存记录表
		payStatementDownloadRecordMapper.insertSelective(record);
	}

	/**
	 * @Description 组装PayStatement数据
	 * @author yuluyuan
	 * @date 2018年8月16日 下午3:54:56
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
		
		if (PayStatementDTO.BillType.ALL.getText().equals(billType)) {
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
			String rate = everyDataArray[23].replace("%", "");
			statement.setRate(Float.valueOf(rate) / 100);
			
		} else if (PayStatementDTO.BillType.SUCCESS.getText().equals(billType)) {
			//商品名称,商户数据包,手续费,费率
			statement.setProdName(everyDataArray[14]);
			statement.setMchData(everyDataArray[15]);
			statement.setFee(new BigDecimal(everyDataArray[16]));
			//费率保存时，去掉百分号，值除以100后存入
			String rate = everyDataArray[17].replace("%", "");
			statement.setRate(Float.valueOf(rate) / 100);
			
		} else if (PayStatementDTO.BillType.REFUND.getText().equals(billType)) {
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
			String rate = everyDataArray[25].replace("%", "");
			statement.setRate(Float.valueOf(rate) / 100);
		}
		return statement;
	}

	/**
	 * @Description 组装PayStatementCount数据
	 * @author yuluyuan
	 * @date 2018年8月18日 上午11:35:49
	 * @param totalDataArray
	 * @param string
	 * @param billDate
	 * @return
	 */
	private PayStatementCount assemblePayStatementCount(String[] totalDataArray, Date billDate) {
		//把数据放入bean中
		PayStatementCount statementCount = new PayStatementCount();
		statementCount.setPayNumCount(Integer.valueOf(totalDataArray[0]));
		statementCount.setPayAmountCount(new BigDecimal(totalDataArray[1]));
		statementCount.setRefundAmountCount(new BigDecimal(totalDataArray[2]));
		statementCount.setRefundDiscountCount(new BigDecimal(totalDataArray[3]));
		statementCount.setFeeCount(new BigDecimal(totalDataArray[4]));
		statementCount.setBillDate(billDate);
		return statementCount;
	}
	
	@Override
	public String downloadFundFlow() {

		Date billDate = DateUtils.addDays(new Date(), -1);
		
		PayFinancialBillDTO dto = new PayFinancialBillDTO();
		dto.setBillDate(sdf.format(billDate));
		dto.setAccountType(PayFinancialBillDTO.SourceType.BASIC.getText());
		
		try {
			Map<String, String> billMap = wXPayApiImpl.downloadFundFlow(dto);

			logger.info("资金账单下载返回数据：{}", String.valueOf(billMap.toString()));
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.get(RETURN_CODE))) {
				
				this.dealRequestFail(billDate, BillType.FINANCIAL_BILL.getCode(), billMap.get(RETURN_MSG));
				this.dealRequestFail(billDate, BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.get(RETURN_MSG));
				logger.info("资金账单下载失败，返回信息：{}", billMap.get(RETURN_CODE));

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.get(RESULT_CODE))) {
					
					this.dealBusiFail(billDate, BillType.FINANCIAL_BILL.getCode(), billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					this.dealBusiFail(billDate, BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					logger.info("资金账单下载失败，错误码：{}；错误信息：{}", billMap.get(ERR_CODE), billMap.get(ERR_CODE_DES));
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = billMap.get("data");
					//去掉表头
					String substring1 = data.substring(data.indexOf(DATA_SEPARATE));
					//去掉统计数据
					String substring2 = substring1.substring(0, substring1.indexOf(FINANCIAL_BILL_REMOVE_TAIL)).replace(DATA_SEPARATE, "");
					//把数据按条分割
					substring2 = substring2.replace("\r\n", "\n");
					String[] dataArray = substring2.split("\n");
					List<PayFinancialBill> list = new ArrayList<PayFinancialBill>();
					for (String everyData : dataArray) {
						//把每一条数据按逗号分割
						String[] everyDataArray = everyData.split(",");
						PayFinancialBill payFinancialBill = this.assemblePayFinancialBill(everyDataArray, dto.getAccountType(), billDate);
						list.add(payFinancialBill);
					}
					//保存数据
					//此处list过大需要分批次插入
					for (PayFinancialBill payFinancialBill : list) {
						payFinancialBillMapper.insertSelective(payFinancialBill);
					}
					//业务成功，记录到记录表
					this.dealSuccess(billDate, BillType.FINANCIAL_BILL.getCode());
					logger.info("资金账单插入成功");
					
//					=====================处理统计数据=================================================================================

					String totalData = data.substring(data.lastIndexOf(STATEMENT_COUNT_REMOVE_HEAD)+1, data.length()).replace(DATA_SEPARATE, "");

					String[] totalDataArray = totalData.split(",");
					PayFinancialBillCount payFinancialBillCount = this.assemblePayFinancialBillCount(totalDataArray, billDate);
					payFinancialBillCountMapper.insertSelective(payFinancialBillCount);

					//业务成功，记录到记录表
					this.dealSuccess(billDate, BillType.FINANCIAL_BILL_COUNT.getCode());
					logger.info("资金账单统计数据插入成功");
					
				}
			}
			
		} catch (Exception e) {
			logger.error("内部错误，下载资金账单失败{}", e.toString());
		}
		return "SUCCESS";
	}

	private PayFinancialBill assemblePayFinancialBill(String[] everyDataArray,
			String accountType, Date billDate) throws ParseException {
		//把数据放入bean中
		PayFinancialBill financialBill = new PayFinancialBill();
		if (PayFinancialBillDTO.SourceType.BASIC.getText().equals(accountType)) {
			financialBill.setAccountingTime(sdf2.parse(everyDataArray[0]));
			financialBill.setWxPayNo(everyDataArray[1]);
			financialBill.setSwiftNo(everyDataArray[2]);
			financialBill.setBusiName(everyDataArray[3]);
			financialBill.setBusiType(everyDataArray[4]);
			financialBill.setBudget(everyDataArray[5]);
			financialBill.setBudgetAmount(new BigDecimal(everyDataArray[6]));
			financialBill.setAccountBalance(new BigDecimal(everyDataArray[7]));
			financialBill.setSubmitBy(everyDataArray[8]);
			financialBill.setRemark(everyDataArray[9]);
			financialBill.setBusiCredentialNo(everyDataArray[10]);
			financialBill.setBillDate(billDate);
			
		} else if (PayFinancialBillDTO.SourceType.OPERATION.getText().equals(accountType)) {
			
		} else if (PayFinancialBillDTO.SourceType.FEES.getText().equals(accountType)) {
			
		}
		return financialBill;
	}
	
	private PayFinancialBillCount assemblePayFinancialBillCount(
			String[] totalDataArray, Date billDate) {
		//把数据放入bean中
		PayFinancialBillCount financialBillCount = new PayFinancialBillCount();
		financialBillCount.setFinancialSwiftNumCount(new BigDecimal(totalDataArray[0]));
		financialBillCount.setIncomeNumCount(new BigDecimal(totalDataArray[1]));
		financialBillCount.setIncomeAmountCount(new BigDecimal(totalDataArray[2]));
		financialBillCount.setExpenditureNumCount(new BigDecimal(totalDataArray[3]));
		financialBillCount.setExpenditureAmountCount(new BigDecimal(totalDataArray[4]));
		financialBillCount.setBillDate(billDate);
		return financialBillCount;
	}

}
