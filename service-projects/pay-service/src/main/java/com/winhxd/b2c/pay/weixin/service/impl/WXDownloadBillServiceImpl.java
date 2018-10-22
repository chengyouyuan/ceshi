package com.winhxd.b2c.pay.weixin.service.impl;

import com.winhxd.b2c.common.domain.pay.condition.DownloadStatementCondition;
import com.winhxd.b2c.common.domain.pay.model.*;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.weixin.base.dto.PayBillDownloadResponseDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayFinancialBillDTO;
import com.winhxd.b2c.pay.weixin.base.dto.PayStatementDTO;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayApi;
import com.winhxd.b2c.pay.weixin.base.wxpayapi.WXPayConstants;
import com.winhxd.b2c.pay.weixin.dao.*;
import com.winhxd.b2c.pay.weixin.service.WXDownloadBillService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author yuluyuan
 *
 * 2018年8月15日
 * @param <>
 */
@Service
public class WXDownloadBillServiceImpl implements WXDownloadBillService {

    private static final Logger logger = LoggerFactory.getLogger(WXDownloadBillServiceImpl.class);
    
    /**
     * 微信侧返回数据的分隔符
     */
    private static final String DATA_SEPARATE = "`";
    
    /**
     * 微信侧返回数据的分隔符
     */
    private static final String SEPARATE = ",`";
    
    /**
     * 对账单下载异常
     */
    private static final String ERROR_CODE_6155 = "6155";
    
    /**
     * 微信入参时间格式
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    /**
     * 微信返回的时间格式
     */
    private static SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
    
    /**
     * 下载对账单
     *
     * @param billDate
     * @param condition
     */
    @EventMessageListener(value = EventTypeHandler.EVENT_DOWNLOAD_STATEMENT_HANDLER, concurrency = "3-6")
    public void downloadStatementHandler(String billDate, DownloadStatementCondition condition) {
    	this.downloadStatement(condition);
    }
    
    /**
     * 下载资金账单
     *
     * @param billDate
     * @param condition
     */
    @EventMessageListener(value = EventTypeHandler.EVENT_DOWNLOAD_FINANCIAL_BILL_HANDLER, concurrency = "3-6")
    public void downloadFinancialBillHandler(String billDate, DownloadStatementCondition condition) {
    	this.downloadFundFlow(condition);
    }
	
	@Override
	public String downloadStatement(DownloadStatementCondition condition) {

		Date billDate = condition.getBillDate();
		if (billDate == null) {
			billDate = DateUtils.addDays(new Date(), -1);
		}

		logger.info("开始下载对账单====：{}", sdf1.format(billDate));
		//某天资金账单已下载,则不再重复下载
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode());
		record.setNotStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
		List<PayStatementDownloadRecord> selectByModel = payStatementDownloadRecordMapper.selectByModel(record);
		if (CollectionUtils.isNotEmpty(selectByModel)) {
			logger.info("{}当天的对账单已下载,不再重复下载！", sdf.format(billDate));
			return PayStatementDownloadRecord.DOWNLOADED;
		}
		
		//开始下载后添加下载中记录
		PayStatementDownloadRecord statementRecord = this.dealLoading(billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode());
		PayStatementDownloadRecord statementCountRecord = this.dealLoading(billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode());
		try {
			PayStatementDTO dto = new PayStatementDTO();
			dto.setBillDate(sdf.format(billDate));
			dto.setBillType(condition.getStatementType());
		
			PayBillDownloadResponseDTO responseDTO = wXPayApi.downloadBill(dto);
			logger.info("对账单下载返回数据：{}", String.valueOf(responseDTO));
			
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(responseDTO.getReturnCode())) {
				
				this.dealRequestFail(statementRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), responseDTO.getReturnMsg());
				this.dealRequestFail(statementCountRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), responseDTO.getReturnMsg());
				logger.info("对账单下载失败，返回信息：{}", responseDTO.getReturnCode());
				if (PayBillDownloadResponseDTO.SYSTEMERROR.equals(responseDTO.getReturnMsg())) {
					logger.info("下载失败，请尝试再次查询。");
				} else if (PayBillDownloadResponseDTO.INVALID_BILL_TYPE.equals(responseDTO.getReturnMsg())
						|| PayBillDownloadResponseDTO.DATA_FORMAT_ERROR.equals(responseDTO.getReturnMsg())
						|| PayBillDownloadResponseDTO.SIGN_ERROR.equals(responseDTO.getReturnMsg())
						|| PayBillDownloadResponseDTO.MISSING_PARAMETER.equals(responseDTO.getReturnMsg())) {
					logger.info("参数错误，请重新检查");
				} else if (PayBillDownloadResponseDTO.NO_BILL_EXIST.equals(responseDTO.getReturnMsg())) {
					logger.info("账单不存在,请检查当前商户号在指定日期内是否有成功的交易。");
				} else if (PayBillDownloadResponseDTO.BILL_CREATING.equals(responseDTO.getReturnMsg())) {
					logger.info("账单未生成,请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。");
				} else if (PayBillDownloadResponseDTO.COMPRESSGZIP_ERROR.equals(responseDTO.getReturnMsg())
						|| PayBillDownloadResponseDTO.UNCOMPRESSGZIP_ERROR.equals(responseDTO.getReturnMsg())) {
					logger.info("账单压缩失败,账单压缩失败，请稍后重试");
				} else {
					logger.info("微信侧返回的其他错误码");
				}

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(responseDTO.getResultCode())) {
					
					this.dealBusiFail(statementRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					this.dealBusiFail(statementCountRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					logger.info("对账单下载失败，错误码：{}；错误信息：{}", responseDTO.getErrCode(), responseDTO.getErrCodeDes());
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = responseDTO.getData();
					
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
					List<PayStatement> arrayList = new ArrayList<PayStatement>();
					int num = 0;
					for (PayStatement payStatement : list) {
						try {
							arrayList.add(payStatement);
							num++;
							if (num % 20 == 0 || num == list.size()) {
								payStatementMapper.insertBatch(arrayList);
								arrayList.clear();
							}
						} catch (Exception e) {
							this.dealSuccess(statementRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
							logger.error("{},保存对账单失败：{}", sdf.format(billDate), e);
							logger.error("保存对账单失败：" + JsonUtil.toJSONString(arrayList));
							throw new Exception(e);
						}
					}
					//业务成功，记录到记录表
					this.dealSuccess(statementRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
					logger.info("对账单插入成功");

//					=====================处理统计数据=================================================================================

					this.dealStatementCountData(statementCountRecord, billDate, payStatementCount);

					return PayStatementDownloadRecord.DOWNLOAD_SUCCESS;
				}
			}
			
			logger.info(responseDTO.toString());
		} catch (Exception e) {
			logger.error("内部错误，下载对账单失败");
			this.dealRequestFail(statementRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT.getCode(), "未知异常");
			this.dealRequestFail(statementCountRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), "未知异常");
			payStatementMapper.deleteByBillDate(billDate);
			payStatementCountMapper.deleteByBillDate(billDate);
			e.printStackTrace();
		}
		return PayStatementDownloadRecord.DOWNLOAD_FAIL;
	}

	private void dealStatementCountData(PayStatementDownloadRecord statementCountRecord, Date billDate, PayStatementCount payStatementCount) throws Exception{

		try {
			payStatementCountMapper.insertSelective(payStatementCount);
		} catch (Exception e) {
			//插入数据失败，记录到记录表
			this.dealSuccess(statementCountRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
			logger.error("内部错误，插入对账单统计数据失败", e);
			throw new Exception(e);
		}

		//业务成功，记录到记录表
		this.dealSuccess(statementCountRecord, billDate, PayStatementDownloadRecord.BillType.STATEMENT_COUNT.getCode(), PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		logger.info("对账单统计数据插入成功");
	}

	/**
	 * @Description 开始下载后添加下载中记录
	 * @author yuluyuan
	 * @date 2018年9月26日 下午4:52:21
	 * @param billDate
	 * @param code
	 * @param code2
	 */
	private PayStatementDownloadRecord dealLoading(Date billDate, int billType) {

		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setStatus(PayStatementDownloadRecord.RecordStatus.LOADING.getCode());
		//保存记录表
		payStatementDownloadRecordMapper.insertSelective(record);
		return record;
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
	private void dealRequestFail(PayStatementDownloadRecord record, Date billDate, int billType,
			String returnMsg) {
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setErrCode(WXPayConstants.FAIL);
		record.setErrCodeDes(returnMsg);
		// 当日没有对账单视为下载成功
		if (PayBillDownloadResponseDTO.NO_BILL_EXIST.equals(returnMsg)) {
			record.setStatus(PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		} else {
			record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
		}
		//保存记录表
		payStatementDownloadRecordMapper.updateByPrimaryKeySelective(record);
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
	private void dealBusiFail(PayStatementDownloadRecord record, Date billDate, int billType,
			String errCode, String errCodeDes) {
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setErrCode(errCode);
		record.setErrCodeDes(errCodeDes);
		// 当日没有对账单视为下载成功
		if (PayBillDownloadResponseDTO.FINANCIAL_NO_BILL_EXIST.equals(errCode)) {
			record.setStatus(PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		} else {
			record.setStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
		}
		//保存记录表
		payStatementDownloadRecordMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * @Description 数据插入后，记录到记录表
	 * @author yuluyuan
	 * @date 2018年8月18日 上午11:03:50
	 * @param billDate
	 * @param billType
	 * @return
	 */
	private void dealSuccess(PayStatementDownloadRecord record, Date billDate, int billType, int recordStatus) {
		record.setBillDate(billDate);
		record.setBillType(billType);
		record.setStatus(recordStatus);
		if (PayStatementDownloadRecord.RecordStatus.FAIL.getCode() == recordStatus) {
			record.setErrCode(ERROR_CODE_6155);
			record.setErrCodeDes("服务器内部错误");
		}
		//保存记录表
		payStatementDownloadRecordMapper.updateByPrimaryKeySelective(record);
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
		String[] everyDataArray = everyData.split(SEPARATE, -1);
		PayStatement statement = new PayStatement();
		statement.setUpdated(new Date());
		
		statement.setPayTime(sdf1.parse(replaceSeparate(everyDataArray[0])));
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
		
		if (DownloadStatementCondition.StatementType.ALL.getText().equals(billType)) {
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
			
		} else if (DownloadStatementCondition.StatementType.SUCCESS.getText().equals(billType)) {
			//商品名称,商户数据包,手续费,费率
			statement.setProdName(everyDataArray[14]);
			statement.setMchData(everyDataArray[15]);
			statement.setFee(new BigDecimal(everyDataArray[16]));
			//费率保存时，去掉百分号，值除以100后存入
			String rate = everyDataArray[17].replace("%", "");
			statement.setRate(Float.valueOf(rate) / 100);
			
		} else if (DownloadStatementCondition.StatementType.REFUND.getText().equals(billType)) {
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
		String[] totalDataArray = everyData.split(SEPARATE, -1);
		PayStatementCount statementCount = new PayStatementCount();
		statementCount.setUpdated(new Date());
		statementCount.setPayNumCount(Integer.valueOf(replaceSeparate(totalDataArray[0])));
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

        logger.info("开始下载资金账单====：{}", sdf1.format(billDate));
		//某天资金账单已下载,则不再重复下载
		PayStatementDownloadRecord record = new PayStatementDownloadRecord();
		record.setBillDate(billDate);
		record.setBillType(PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode());
		record.setNotStatus(PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
		List<PayStatementDownloadRecord> selectByModel = payStatementDownloadRecordMapper.selectByModel(record);
		if (CollectionUtils.isNotEmpty(selectByModel)) {
			logger.info("{}当天的资金账单已下载,不再重复下载！", sdf.format(billDate));
			return PayStatementDownloadRecord.DOWNLOADED;
		}
		
		//开始下载后添加下载中记录
		PayStatementDownloadRecord billRecord = this.dealLoading(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode());
		PayStatementDownloadRecord billCountRecord = this.dealLoading(billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode());
		
		try {
			PayFinancialBillDTO dto = new PayFinancialBillDTO();
			dto.setBillDate(sdf.format(billDate));
			dto.setAccountType(condition.getAccountType());
		
			PayBillDownloadResponseDTO billMap = wXPayApi.downloadFundFlow(dto);

			logger.info("资金账单下载返回数据：{}", String.valueOf(billMap));
			//通信失败，则记录失败原因到记录表
			if (WXPayConstants.FAIL.equals(billMap.getReturnCode())) {
				
				this.dealRequestFail(billRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), billMap.getReturnMsg());
				this.dealRequestFail(billCountRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.getReturnMsg());
				logger.info("资金账单下载失败，返回信息：{}", billMap.getReturnCode());
				if (PayBillDownloadResponseDTO.SYSTEMERROR.equals(billMap.getReturnMsg())) {
					logger.info("下载失败，请尝试再次查询。");
				} else if (PayBillDownloadResponseDTO.PARAM_ERROR.equals(billMap.getReturnMsg())) {
					logger.info("参数错误，请对照文档的请求参数说明检查参数。");
				} else if (PayBillDownloadResponseDTO.FINANCIAL_NO_BILL_EXIST.equals(billMap.getReturnMsg())) {
					logger.info("账单不存在，请检查当前商户号在指定日期内是否有成功的交易。");
				} else if (PayBillDownloadResponseDTO.FINANCIAL_BILL_CREATING.equals(billMap.getReturnMsg())) {
					logger.info("账单未生成，请先检查当前商户号在指定日期内是否有成功的交易，如指定日期有交易则表示账单正在生成中，请在上午10点以后再下载。");
				} else {
					logger.info("微信侧返回的其他错误码");
				}

			//通信成功
			} else {
				//业务失败，记录失败原因到记录表
				if (WXPayConstants.FAIL.equals(billMap.getResultCode())) {
					
					this.dealBusiFail(billRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), billMap.getErrCode(), billMap.getErrCodeDes());
					this.dealBusiFail(billCountRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), billMap.getErrCode(), billMap.getErrCodeDes());
					logger.info("资金账单下载失败，错误码：{}；错误信息：{}", billMap.getErrCode(), billMap.getErrCodeDes());
					
				//成功则开始插入数据
				}else{
					//获取微信返回的总数据
					String data = billMap.getData();
					
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
					List<PayFinancialBill> arrayList = new ArrayList<PayFinancialBill>();
					int num = 0;
					for (PayFinancialBill payFinancialBill : list) {
						try {
							arrayList.add(payFinancialBill);
							num++;
							if (num % 20 == 0 || num == list.size()) {
								payFinancialBillMapper.insertBatch(arrayList);
								arrayList.clear();
							}
						} catch (Exception e) {
							this.dealSuccess(billRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), PayStatementDownloadRecord.RecordStatus.FAIL.getCode());
							logger.error("{},保存资金账单失败：{}", sdf.format(billDate), e);
							logger.error("保存资金账单失败：" + JsonUtil.toJSONString(arrayList));
							throw new Exception(e);
						}
					}
					//业务成功，记录到记录表
					this.dealSuccess(billRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
					logger.info("资金账单插入成功");

//					=====================处理统计数据=================================================================================
					this.dealFinancialBillCountData(billCountRecord, billDate, payFinancialBillCount);

					return PayStatementDownloadRecord.DOWNLOAD_SUCCESS;
				}
			}
		} catch (Exception e) {
			logger.error("内部错误，下载资金账单失败{}", e);
			this.dealRequestFail(billRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL.getCode(), "未知异常");
			this.dealRequestFail(billCountRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), "未知异常");
			payFinancialBillMapper.deleteByBillDate(billDate);
			payFinancialBillCountMapper.deleteByBillDate(billDate);
		}
		return PayStatementDownloadRecord.DOWNLOAD_FAIL;
	}

	private void dealFinancialBillCountData(PayStatementDownloadRecord billCountRecord, Date billDate,PayFinancialBillCount payFinancialBillCount) throws Exception{

		try {
			payFinancialBillCountMapper.insertSelective(payFinancialBillCount);
		} catch (Exception e) {
			this.dealSuccess(billCountRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), PayStatementDownloadRecord.RecordStatus.FAIL.getCode());

			logger.error("资金账单统计数据插入失败", e);
			throw new Exception(e);
		}

		//业务成功，记录到记录表
		this.dealSuccess(billCountRecord, billDate, PayStatementDownloadRecord.BillType.FINANCIAL_BILL_COUNT.getCode(), PayStatementDownloadRecord.RecordStatus.SUCCESS.getCode());
		logger.info("资金账单统计数据插入成功");
		
	}

	private PayFinancialBill assemblePayFinancialBill(String everyData,
			String accountType, Date billDate) throws ParseException {
		//把数据放入bean中

		String[] everyDataArray = everyData.split(SEPARATE, -1);
		
		PayFinancialBill financialBill = new PayFinancialBill();
		financialBill.setUpdated(new Date());
		
		if (DownloadStatementCondition.SourceType.BASIC.getText().equals(accountType)) {
			financialBill.setAccountingTime(sdf1.parse(replaceSeparate(everyDataArray[0])));
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
			
		} else if (DownloadStatementCondition.SourceType.OPERATION.getText().equals(accountType)) {
			
		} else if (DownloadStatementCondition.SourceType.FEES.getText().equals(accountType)) {
			
		}
		return financialBill;
	}
	
	private PayFinancialBillCount assemblePayFinancialBillCount(
			String everyData, Date billDate) {
		//把数据放入bean中
		String[] totalDataArray = everyData.split(SEPARATE, -1);
		PayFinancialBillCount financialBillCount = new PayFinancialBillCount();
		financialBillCount.setUpdated(new Date());
		financialBillCount.setFinancialSwiftNumCount(new BigDecimal(replaceSeparate(totalDataArray[0])));
		financialBillCount.setIncomeNumCount(new BigDecimal(totalDataArray[1]));
		financialBillCount.setIncomeAmountCount(new BigDecimal(totalDataArray[2]));
		financialBillCount.setExpenditureNumCount(new BigDecimal(totalDataArray[3]));
		financialBillCount.setExpenditureAmountCount(new BigDecimal(totalDataArray[4]));
		financialBillCount.setBillDate(billDate);
		return financialBillCount;
	}

	/**
	 * @Description 去掉分隔数据的字符
	 * @author yuluyuan
	 * @date 2018年8月22日 下午12:40:19
	 * @param string
	 * @return
	 */
	private String replaceSeparate(String string) {
		
		return string.replace(DATA_SEPARATE, "");
	}
	
	@Override
	public List<Date> findUnDownloadRecord(PayStatementDownloadRecord record) {
		//获取前X天的时间List
		List<Date> dateList = this.getBeforeXDateList(new Date(), record.getFailedDays());
		List<Date> list = new ArrayList<Date>();
		for (int i = 0; i < dateList.size(); i++) {
			record.setBillDate(dateList.get(i));
			List<PayStatementDownloadRecord> recordList = payStatementDownloadRecordMapper.selectUnDownloadRecord(record);
			if (CollectionUtils.isNotEmpty(recordList)) {
				list.add(recordList.get(0).getBillDate());
			}
		}
		return list;
	}

	/**
	 * @Description 获取前X天的时间List
	 * @author yuluyuan
	 * @date 2018年8月27日 上午11:36:22
	 * @param date
	 * @return
	 */
	private List<Date> getBeforeXDateList(Date date, int x) {
		List<Date> dateList = new ArrayList<Date>();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(DateUtils.addDays(date, -x));
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		
		while(cal2.after(cal1)){
			dateList.add(cal1.getTime());
			cal1.add(Calendar.DAY_OF_MONTH, 1);  
		}
		
		return dateList;
	}

	@Override
	public PayStatement getPayStatementByOutOrderNo(String outOrderNo) {
		return payStatementMapper.selectByOutOrderNo(outOrderNo);
	}

}
