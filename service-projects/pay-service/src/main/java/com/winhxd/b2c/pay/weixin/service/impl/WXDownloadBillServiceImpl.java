package com.winhxd.b2c.pay.weixin.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.model.PayFinancialBill;
import com.winhxd.b2c.common.domain.pay.model.PayFinancialBillCount;
import com.winhxd.b2c.common.domain.pay.model.PayStatement;
import com.winhxd.b2c.common.domain.pay.model.PayStatementCount;
import com.winhxd.b2c.common.domain.pay.model.PayStatementDownloadRecord;
import com.winhxd.b2c.pay.weixin.base.dto.PayBillDownloadResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayFinancialBillDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayStatementDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.dao.PayFinancialBillCountMapper;
import com.winhxd.b2c.pay.weixin.dao.PayFinancialBillMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementCountMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementDownloadRecordMapper;
import com.winhxd.b2c.pay.weixin.dao.PayStatementMapper;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;

/**
 * 
 * @author yuluyuan
 *
 * 2018年8月15日
 */
@Service
public class WXDownloadBillServiceImpl implements WXDownloadBillService {

    private static final Logger logger = LoggerFactory.getLogger(WXTransfersServiceImpl.class);
    
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
     * 微信返回的时间格式，不确定是哪个，例：2014-11-1016：33：45           2018-02-01 04:21:23
     */
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-ddHH：mm：ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    @Autowired
    private WXPayApi wXPayApi;
    
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
	public String downloadStatement(DownloadStatementCondition condition) {
		Date billDate = condition.getBillDate();
		if (billDate == null) {
			billDate = DateUtils.addDays(new Date(), -1);
		}

		//某天资金账单已下载,则不再重复下载
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode());
		record.setStatus(PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		List<PayStatementDownloadRecord> selectByModel = payStatementDownloadRecordMapper.selectByModel(record);
		if (CollectionUtils.isNotEmpty(selectByModel)) {
			logger.info("{}当天的对账单已下载,不再重复下载！", billDate);
			return PayStatementDownloadRecord.DOWNLOADED;
		}
		
		PayStatementDTO dto = new PayStatementDTO();
		dto.setBillDate(sdf.format(billDate));
		dto.setBillType(condition.getStatementType());
		
		try {
			PayBillDownloadResponseDTO responseDTO = wXPayApi.downloadBill(dto);
			
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(responseDTO.getReturnCode())) {
				
				this.dealRequestFail(billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), responseDTO.getReturnMsg());
				this.dealRequestFail(billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), responseDTO.getReturnMsg());
				logger.info("对账单下载失败，返回信息：{}", responseDTO.getReturnCode());

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(responseDTO.getResultCode())) {
					
					this.dealBusiFail(billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					this.dealBusiFail(billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					logger.info("对账单下载失败，错误码：{}；错误信息：{}", responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					
				//成功则开始插入数据
				}else{
					
					
					

					//获取微信返回的总数据
					String data = responseDTO.getData().replace(DATA_SEPARATE, "");
					
					List<PayStatement> list = new ArrayList<PayStatement>();
					PayStatementCount payStatementCount = new PayStatementCount();

					String[] split = data.split(System.lineSeparator());

					for (int i = 0; i < split.length; i++) {
						//对第一行和倒数第二行(表头文字)不作处理
						if (i == 0 || i == split.length - 2) {
							continue;
						}
						//把 第二行~倒数第三行 的数据转成PayStatement
						if (i > 0 && i < split.length - 2) {
							PayStatement payFinancialBill = this.assemblePayStatement(split[i], dto.getBillType(), billDate);
							list.add(payFinancialBill);
						}
						//把 最后一行 的数据转成PayStatementCount
						if (i == split.length - 1) {
							payStatementCount = this.assemblePayStatementCount(split[i], billDate);
						}
					}
					
					//保存数据
					//此处list过大需要分批次插入
					for (PayStatement payStatement : list) {
						payStatementMapper.insertSelective(payStatement);
					}
					//业务成功，记录到记录表
					this.dealSuccess(billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode());
					logger.info("对账单插入成功");
					
//					=====================处理统计数据=================================================================================

					payStatementCountMapper.insertSelective(payStatementCount);

					//业务成功，记录到记录表
					this.dealSuccess(billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode());
					logger.info("对账单统计数据插入成功");

					return PayStatementDownloadRecord.DOWNLOAD_SUCCESS;
				}
			}
			
			logger.info(responseDTO.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			payStatementMapper.deleteByBillDate(billDate);
			payStatementCountMapper.deleteByBillDate(billDate);
			e.printStackTrace();
		}
		return PayStatementDownloadRecord.DOWNLOAD_FAIL;
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
		record.setErrCode(WXPayConstants.FAIL);
		record.setErrCodeDes(returnMsg);
		record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
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
		record.setErrCode(errCode);
		record.setErrCodeDes(errCodeDes);
		record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
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
		record.setStatus(PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
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
	private PayStatement assemblePayStatement(String everyData, String billType, Date billDate) throws ParseException {
		//把数据放入bean中
		String[] everyDataArray = everyData.split(",");
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
		
		if (PayStatementDTO.StatementType.ALL.getText().equals(billType)) {
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
			
		} else if (PayStatementDTO.StatementType.SUCCESS.getText().equals(billType)) {
			//商品名称,商户数据包,手续费,费率
			statement.setProdName(everyDataArray[14]);
			statement.setMchData(everyDataArray[15]);
			statement.setFee(new BigDecimal(everyDataArray[16]));
			//费率保存时，去掉百分号，值除以100后存入
			String rate = everyDataArray[17].replace("%", "");
			statement.setRate(Float.valueOf(rate) / 100);
			
		} else if (PayStatementDTO.StatementType.REFUND.getText().equals(billType)) {
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
	private PayStatementCount assemblePayStatementCount(String everyData, Date billDate) {
		//把数据放入bean中
		String[] totalDataArray = everyData.split(",");
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
	public String downloadFundFlow(DownloadStatementCondition condition) {
		Date billDate = condition.getBillDate();
		if (condition.getBillDate() == null) {
			billDate = DateUtils.addDays(new Date(), -1);
		}
		
		//某天资金账单已下载,则不再重复下载
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode());
		record.setStatus(PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		List<PayStatementDownloadRecord> selectByModel = payStatementDownloadRecordMapper.selectByModel(record);
		if (CollectionUtils.isNotEmpty(selectByModel)) {
			logger.info("{}当天的资金账单已下载,不再重复下载！", billDate);
			return PayStatementDownloadRecord.DOWNLOADED;
		}
		
		PayFinancialBillDTO dto = new PayFinancialBillDTO();
		dto.setBillDate(sdf.format(billDate));
		dto.setAccountType(condition.getAccountType());
		
		try {
			PayBillDownloadResponseDTO billMap = wXPayApi.downloadFundFlow(dto);

			logger.info("资金账单下载返回数据：{}", String.valueOf(billMap));
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.getReturnCode())) {
				
				this.dealRequestFail(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), billMap.getReturnMsg());
				this.dealRequestFail(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.getReturnMsg());
				logger.info("资金账单下载失败，返回信息：{}", billMap.getReturnCode());

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.getResultCode())) {
					
					this.dealBusiFail(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), billMap.getErrCode(), billMap.getErrCodeDes());
					this.dealBusiFail(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.getErrCode(), billMap.getErrCodeDes());
					logger.info("资金账单下载失败，错误码：{}；错误信息：{}", billMap.getErrCode(), billMap.getErrCodeDes());
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = billMap.getData().replace(DATA_SEPARATE, "");
					
					List<PayFinancialBill> list = new ArrayList<PayFinancialBill>();
					PayFinancialBillCount payFinancialBillCount = new PayFinancialBillCount();

					String[] split = data.split(System.lineSeparator());

					for (int i = 0; i < split.length; i++) {
						//对第一行和倒数第二行(表头文字)不作处理
						if (i == 0 || i == split.length - 2) {
							continue;
						}
						//把 第二行~倒数第三行 的数据转成PayFinancialBill
						if (i > 0 && i < split.length - 2) {
							PayFinancialBill payFinancialBill = this.assemblePayFinancialBill(split[i], dto.getAccountType(), billDate);
							list.add(payFinancialBill);
						}
						//把 最后一行 的数据转成PayFinancialBillCount
						if (i == split.length - 1) {
							payFinancialBillCount = this.assemblePayFinancialBillCount(split[i], billDate);
						}
					}
					
					//保存数据
					//此处list过大需要分批次插入
					for (PayFinancialBill payFinancialBill : list) {
						payFinancialBillMapper.insertSelective(payFinancialBill);
					}
					//业务成功，记录到记录表
					this.dealSuccess(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode());
					logger.info("资金账单插入成功");
					
//					=====================处理统计数据=================================================================================
					payFinancialBillCountMapper.insertSelective(payFinancialBillCount);

					//业务成功，记录到记录表
					this.dealSuccess(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode());
					logger.info("资金账单统计数据插入成功");

					return PayStatementDownloadRecord.DOWNLOAD_SUCCESS;
				}
			}
		} catch (Exception e) {
			logger.error("内部错误，下载资金账单失败{}", e.toString());
			payFinancialBillMapper.deleteByBillDate(billDate);
			payFinancialBillCountMapper.deleteByBillDate(billDate);
		}
		return PayStatementDownloadRecord.DOWNLOAD_FAIL;
	}

	private PayFinancialBill assemblePayFinancialBill(String everyData,
			String accountType, Date billDate) throws ParseException {
		//把数据放入bean中

		String[] everyDataArray = everyData.split(",");
		
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
			String everyData, Date billDate) {
		//把数据放入bean中
		String[] totalDataArray = everyData.split(",");
		PayFinancialBillCount financialBillCount = new PayFinancialBillCount();
		financialBillCount.setFinancialSwiftNumCount(new BigDecimal(totalDataArray[0]));
		financialBillCount.setIncomeNumCount(new BigDecimal(totalDataArray[1]));
		financialBillCount.setIncomeAmountCount(new BigDecimal(totalDataArray[2]));
		financialBillCount.setExpenditureNumCount(new BigDecimal(totalDataArray[3]));
		financialBillCount.setExpenditureAmountCount(new BigDecimal(totalDataArray[4]));
		financialBillCount.setBillDate(billDate);
		return financialBillCount;
	}

	@Override
	public List<PayStatementDownloadRecord> findDownloadRecord(
			PayStatementDownloadRecord record) {
		return payStatementDownloadRecordMapper.selectByModel(record);
	}

}
