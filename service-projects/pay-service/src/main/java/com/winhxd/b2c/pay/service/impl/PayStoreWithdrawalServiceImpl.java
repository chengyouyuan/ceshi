package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.PayNotifyMsg;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.enums.MsgCategoryEnum;
import com.winhxd.b2c.common.domain.pay.condition.CalculationCmmsAmtCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.condition.UpdateStoreBankRollCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayWithdrawalTypeEnum;
import com.winhxd.b2c.common.domain.pay.enums.ReviewStatusEnum;
import com.winhxd.b2c.common.domain.pay.enums.StoreBankRollOpearateEnums;
import com.winhxd.b2c.common.domain.pay.enums.WithdrawalsStatusEnum;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.CalculationCmmsAmtVO;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreUserInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.pay.config.PayWithdrawalConfig;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsTypeMapper;
import com.winhxd.b2c.pay.dao.StoreBankCardMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayStoreWithdrawalService;
import com.winhxd.b2c.pay.util.NumberUtil;
import com.winhxd.b2c.pay.util.PayUtil;

/**
 * @Author zhanghaun
 * @Date 2018/8/14 17:04
 * @Description
 **/
@Service
public class PayStoreWithdrawalServiceImpl implements PayStoreWithdrawalService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PayFinancialManagerServiceImpl.class);
	@Autowired
	private PayWithdrawalsMapper payWithdrawalsMapper;
	@Autowired
	private PayWithdrawalsTypeMapper payWithdrawalsTypeMapper;
	
	@Autowired
	private PayStoreWalletMapper payStoreWalletMapper;
	@Autowired
	private StoreBankrollMapper storeBankrollMapper;

	@Autowired
	private StoreBankCardMapper storeBankCardMapper;
	
	@Autowired
	private PayServiceImpl payServiceImpl;
	
	@Resource
	private Cache redisClusterCache;
	
	@Resource
	private PayWithdrawalConfig payWithDrawalConfig;
	
	private static final String ACCOUNT_NAME = "微信钱包";
	
	@Resource
	private MessageSendUtils messageServiceClient;
	
	@Autowired
	private Cache cache;
	
	/**判断当前用户是否绑定了微信或者银行卡，如果绑定过了则返回页面回显信息*/
	@Override
	public PayWithdrawalPageVO showPayWithdrawalDetail(PayStoreApplyWithDrawCondition condition) {
		short bankType = PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode();
		short weixType= PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode();
		if(condition.getWithdrawType() == 0){
			LOGGER.info("请传入提现类型参数");
			throw new BusinessException(BusinessCode.CODE_610022);
		}
		StoreUser storeUser=UserContext.getCurrentStoreUser();
		if(storeUser==null||storeUser.getBusinessId()==null){
			LOGGER.info("未获取到门店数据");
			throw new BusinessException(BusinessCode.CODE_610801);
		}
		Long businessId = storeUser.getBusinessId();
//		Long businessId = 130l;
		PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
		if(bankType == condition.getWithdrawType()){
			//获取绑定银行卡信息
			List<StoreBankCard> bankCards = storeBankCardMapper.selectByStoreId(businessId);
			if(CollectionUtils.isNotEmpty(bankCards)){
				StoreBankCard bankCard=bankCards.get(0);
				 String carnumber = bankCard.getCardNumber();
				 withdrawalPage.setUserAcountName(bankCard.getBankName()+"("+carnumber.substring(carnumber.length()-4, carnumber.length())+")");
				 withdrawalPage.setBandBranchName(bankCard.getBandBranchName());
				 withdrawalPage.setBankName(bankCard.getBankName());
				 withdrawalPage.setBankUserName(bankCard.getBankUserName());
				 withdrawalPage.setCardNumber(bankCard.getCardNumber());
				 withdrawalPage.setMobile(bankCard.getMobile());
				 withdrawalPage.setSwiftCode(bankCard.getSwiftCode()); 
			}
			
		}else if(weixType == condition.getWithdrawType()){
			//获取绑定微信钱包数据
			List<PayStoreWallet> payStoreWalletList = payStoreWalletMapper.selectByStoreId(businessId);
			if(CollectionUtils.isNotEmpty(payStoreWalletList)){
				 PayStoreWallet wallet=payStoreWalletList.get(0);
				 withdrawalPage.setUserAcountName(ACCOUNT_NAME+"("+wallet.getNick()+")");
				 withdrawalPage.setNick(wallet.getNick());
				 withdrawalPage.setOpenid(wallet.getOpenid());
			}
		}
		// 获取当前门店的资金信息
		StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(businessId);
		//可提现金额
		BigDecimal presentedMoney=BigDecimal.valueOf(0);
		if(storeBankroll != null){
			presentedMoney=storeBankroll.getPresentedMoney();
			presentedMoney=presentedMoney==null?BigDecimal.valueOf(0):presentedMoney;
		}
		 withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
		 LOGGER.info("最大提现额度：---"+ payWithDrawalConfig.getMaxMoney());
		 withdrawalPage.setRate(payWithDrawalConfig.getRate());
		 withdrawalPage.setPresented_money(presentedMoney);
		 withdrawalPage.setMinMoeny(payWithDrawalConfig.getMinMoney());
		return withdrawalPage;
	}
	

	@Override
	@Transactional
	public void saveStorWithdrawalInfo(@RequestBody PayStoreApplyWithDrawCondition condition) {
		ResponseResult<Integer> result = new ResponseResult<Integer>();

		StoreUser storeUser=UserContext.getCurrentStoreUser();
		if(storeUser==null||storeUser.getBusinessId()==null){
			LOGGER.info("未获取到门店数据");
			throw new BusinessException(BusinessCode.CODE_610901);
		}
		Long businessId = storeUser.getBusinessId();		
		
		// 加入redis控制访问频率
		String limitTimeKey = CacheName.LIMIT_INTERFACE_ACCESS + businessId;
		if (cache.exists(limitTimeKey)) {
			LOGGER.info("提现操作次数太多");
			throw new BusinessException(BusinessCode.CODE_611105);
		} else {
			cache.set(limitTimeKey, String.valueOf(1));
			cache.expire(limitTimeKey, 3);
		}
		
		// 验证入参是否传入正确
		valiApplyWithDrawCondition(condition);
		short bankType = PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode();
		short weixType= PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode();
		PayWithdrawals payWithdrawal = new PayWithdrawals();
		payWithdrawal.setSpbillCreateIp(condition.getSpbillCreateIp());
		payWithdrawal.setStoreId(businessId);
		// 生成提现单号
		payWithdrawal.setWithdrawalsNo(generateWithdrawalsNo());
		BigDecimal totalFee = condition.getTotalFee();
		getWithdrawMoney(businessId,totalFee);
		payWithdrawal.setTotalFee(totalFee);
		if(bankType == condition.getWithdrawType()){
			List<PayStoreUserInfoVO> selectStorBankCardInfo = payWithdrawalsMapper.getStorBankCardInfo(businessId);
			if(CollectionUtils.isEmpty(selectStorBankCardInfo)){
				LOGGER.info("当前用户没有绑定银行卡");
				throw new BusinessException(BusinessCode.CODE_610025);
			} 
			
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(bankType);
			payWithdrawal.setMobile(condition.getMobile());
			BigDecimal rate = payWithDrawalConfig.getRate();
			BigDecimal cmms = countCmms(rate,totalFee);
			payWithdrawal.setCmmsAmt(cmms);
			LOGGER.info("当前计算所得的手续费为："+cmms);
			BigDecimal realFee = totalFee.subtract(cmms);
			LOGGER.info("当前计算所得实际提现金额："+realFee +";当前的银行费率："+ rate);
			payWithdrawal.setRealFee(realFee);
			payWithdrawal.setRate(rate);
			payWithdrawal.setPaymentAccount(condition.getPaymentAccount());
			payWithdrawal.setSwiftCode(condition.getSwiftCode());
			payWithdrawal.setName(condition.getStroeName());
			payWithdrawal.setCreatedByName(condition.getStroeName());
			payWithdrawal.setUpdatedByName(condition.getStroeName());
		}else if(weixType == condition.getWithdrawType()){
			List<PayStoreWallet> payStoreWallet = payStoreWalletMapper.selectByStoreId(businessId);
			if(payStoreWallet.size() == 0){
				result.setMessage("门店当前没有微信绑定记录");
				throw new BusinessException(BusinessCode.CODE_610026);
			}
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(weixType);
			payWithdrawal.setBuyerId(condition.getBuyerId());
			payWithdrawal.setPaymentAccount(condition.getBuyerId());
			payWithdrawal.setName(condition.getNick());
			payWithdrawal.setCreatedByName(condition.getNick());
			payWithdrawal.setUpdatedByName(condition.getNick());
		}
		payWithdrawal.setAuditStatus(ReviewStatusEnum.TO_AUDIT.getStatus());
//		payWithdrawal.setAuditDesc(auditDesc);
		payWithdrawal.setCreated(new Date());
		payWithdrawal.setCreatedBy(businessId);
		payWithdrawal.setUpdated(new Date());
		payWithdrawal.setUpdatedBy(businessId);
		payWithdrawal.setCallbackStatus(WithdrawalsStatusEnum.APPLY.getStatusCode());
		saveStoreWithdrawalInfo(businessId, payWithdrawal);
		
		// 更新账户金额
		UpdateStoreBankRollCondition rollCondtion = new UpdateStoreBankRollCondition();
		rollCondtion.setType(StoreBankRollOpearateEnums.WITHDRAWALS_APPLY.getCode());
		rollCondtion.setStoreId(businessId);
		rollCondtion.setWithdrawalsNo(payWithdrawal.getWithdrawalsNo());
		rollCondtion.setMoney(payWithdrawal.getTotalFee());
		LOGGER.info("当前更新账户金额入参：--"+rollCondtion);
		payServiceImpl.updateStoreBankroll(rollCondtion);
		
		// 提下完成之后发送云信消息
		PayUtil.sendMsg(messageServiceClient,PayNotifyMsg.STORE_APPLY_WITHDRWAL,MsgCategoryEnum.WITHDRAW_APPLY.getTypeCode(),businessId);
	} 
	
	// 计算手续费率
	public BigDecimal countCmms(BigDecimal rate,BigDecimal totalFee){
		BigDecimal cmms = totalFee.multiply(rate);
		// 计算手续费
		BigDecimal mix = new BigDecimal(1);
	    BigDecimal max = new BigDecimal(25);
		int a = cmms.compareTo(mix);
		int b = cmms.compareTo(max);
		if(a == -1){
			cmms = mix;
		}
		if(b == 1){
			cmms = max;
		}
		return cmms;
	}
	
	private void valiApplyWithDrawCondition(PayStoreApplyWithDrawCondition condition) {
		int res= 0;
		short  withdralType = condition.getWithdrawType();
		if(withdralType != PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode() && withdralType != PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()){
			LOGGER.info("体现类型为空");
			res = BusinessCode.CODE_610022;
			throw new BusinessException(res);
		}
		
		BigDecimal totalFee = condition.getTotalFee();
		if(totalFee == null||!NumberUtil.isPositiveDecimal(totalFee.toString())){
			LOGGER.info("提现金额输入有误");
			res = BusinessCode.CODE_610032;
			throw new BusinessException(res);
		}
		String totalFeeStr=totalFee.toString();
//		if (totalFeeStr.indexOf(".")!=-1) {
//			//最多支持两位小数
//			String [] totalFeeArr=totalFeeStr.split(".");
//			if (totalFeeArr[1].length()>2) {
//				LOGGER.info("提现金额超过了两位小数");
//				res = BusinessCode.CODE_610032;
//				throw new BusinessException(res);
//			}
//		}
		short type = condition.getFlowDirectionType();
		if(type == 0){
			res = BusinessCode.CODE_610033;
			throw new BusinessException(res);
		}
		
		String name = condition.getFlowDirectionName();
		if(StringUtils.isEmpty(name)){
			res = BusinessCode.CODE_610034;
			throw new BusinessException(res);
		}
		short withdrawType = condition.getWithdrawType();
		if(withdrawType == PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode()){
			String openId = condition.getBuyerId();
			if(StringUtils.isEmpty(openId)){
				res = BusinessCode.CODE_610031;
				throw new BusinessException(res);
			}
			String nick = condition.getNick(); 
			if(StringUtils.isEmpty(nick)){
				res = BusinessCode.CODE_610036;
				throw new BusinessException(res);
			}
		}
		String paymentAccount = condition.getPaymentAccount();
		if(StringUtils.isEmpty(paymentAccount)){
			res = BusinessCode.CODE_610012;
			throw new BusinessException(res);
		}
		if(withdrawType == PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()){
			String swiftCode = condition.getSwiftCode();
			if(StringUtils.isEmpty(swiftCode)){
				res = BusinessCode.CODE_610029;
				throw new BusinessException(res);
			}
			String storeName = condition.getStroeName();
			if(StringUtils.isEmpty(storeName)){
				res = BusinessCode.CODE_610037;
				throw new BusinessException(res);
			}
			String mobile = condition.getMobile();
			if(StringUtils.isEmpty(mobile)){
				res = BusinessCode.CODE_610015;
				throw new BusinessException(res);
			}
		}
	}

	
	/**验证通过之后保存当前的用户提现信息*/
	public void saveStoreWithdrawalInfo(Long businessId,PayWithdrawals payWithdrawals){
		payWithdrawalsMapper.insertSelective(payWithdrawals);
	}
	
	
	/**
     * 生成提现订单号  格式：T+YYMMDDHH(年月日小时8位)XXXXXXXXX(9位随机吗)= 18位
     *
     * @return
     * @author wangbin
     * @date 2018年8月3日 上午11:34:29
     */
    private String generateWithdrawalsNo() {
        String withdrawalsNo = null;
        Date date = new Date();
        do {
            UUID uuid = UUID.randomUUID();
            int hashCodeV = (uuid.toString() + System.currentTimeMillis()).hashCode();
            if (hashCodeV < 0) {
                // 有可能是负数
                hashCodeV = -hashCodeV;
            }
            while (hashCodeV > 999999999) {
                hashCodeV = hashCodeV >> 1;
            }
            String withdrawalsNoDateTimeFormatter = "yyMMddHH";
            // 0 代表前面补充0       
            // 9 代表长度为4       
            // d 代表参数为正数型 
            String randomFormat = "%09d";
            withdrawalsNo = "T" + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter) + String.format(randomFormat, hashCodeV);
        } while (!checkOrderNoAvailable(withdrawalsNo, date));
        return withdrawalsNo;
    }
    /**
     * 校验提现单号生成是否重复
     *
     * @param orderNo
     * @param date
     * @return
     * @author wangbin
     * @date 2018年8月11日 下午5:32:33
     */
    private boolean checkOrderNoAvailable(String withdrawalsNo, Date date) {

        String withdrawalsNoDateTimeFormatter = "yyMMddHH";
        String val = redisClusterCache.hget(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), withdrawalsNo);
        if (StringUtils.isNotBlank(val)) {
            LOGGER.info("订单号生成出现重复：orderNo={}", withdrawalsNo);
            return false;
        } else {
        	redisClusterCache.hset(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), withdrawalsNo, withdrawalsNo);
            //下一小时就过期
            Long expires = (DateUtils.truncate(DateUtils.addHours(date, 1), Calendar.HOUR_OF_DAY).getTime() - date.getTime()) / 1000;
            redisClusterCache.expire(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), expires.intValue());
            return true;
        }
    }

	@Override
	public List<PayWithdrawalsType> getAllWithdrawalType() {
		List<PayWithdrawalsType> types = payWithdrawalsTypeMapper.selectAll();
		if(types.isEmpty()){
			LOGGER.info("types------"+types);
			throw new BusinessException(BusinessCode.CODE_610023);
		} 
		return types;
	}

	@Override
	public CalculationCmmsAmtVO calculationCmmsAmt(CalculationCmmsAmtCondition condition) {
		String log="门店提现计算手续费---";
		LOGGER.info(log+"开始");
		if (condition==null) {
			LOGGER.info(log+"参数为空");
			throw new BusinessException(BusinessCode.CODE_611101);
		}
		Short withdrawType=condition.getWithdrawType();
		BigDecimal totalFee=condition.getTotalFee();
		if (withdrawType==null) {
			LOGGER.info(log+"提现类型为空");
			throw new BusinessException(BusinessCode.CODE_611102);
		}
		if (totalFee==null) {
			LOGGER.info(log+"提现金额为空");
			throw new BusinessException(BusinessCode.CODE_611103);
		}
		LOGGER.info(log+"参数为--"+condition.toString());
		//获取门店资金信息
	    StoreUser storeUser = UserContext.getCurrentStoreUser();
        Long storeId = storeUser.getBusinessId();
		// 写死门店id
//		Long storeId = 130l;
        StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(storeId);
        if(storeBankroll != null){
        	//判断提现金额是否大于可提现金额
        	BigDecimal total = storeBankroll.getPresentedMoney();
    		if(totalFee.compareTo(total) == 1){
    			LOGGER.info(log+"可提现金额不足");
    			throw new BusinessException(BusinessCode.CODE_611104);
    		}
        }
		CalculationCmmsAmtVO vo=new CalculationCmmsAmtVO();
		BigDecimal rate = payWithDrawalConfig.getRate();
		BigDecimal cmms = countCmms(rate,totalFee);
		vo.setCmmsAmt(cmms);
		vo.setRealFee(totalFee.subtract(cmms));
		return vo;
	}
	
	/** 查询当前账户可提现金额 
	 * totalFee 当前用户的提现金额*/
	public PayWithdrawalPageVO getWithdrawMoney(Long storeid,BigDecimal totalFee){
		PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
		//返回当前账户钱包里的可提现金额
		  StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(storeid);
		  if(storeBankroll != null){
			  if(storeBankroll.getTotalMoeny().compareTo(BigDecimal.valueOf(0L)) == 0){
				  LOGGER.info("账户没有可提现资金");
				  throw new BusinessException(BusinessCode.CODE_610038);
			  }
			  if(totalFee != null && storeBankroll.getPresentedMoney() != null){
				  if(storeBankroll.getPresentedMoney().compareTo(totalFee) == -1){
					  LOGGER.info("当前提现金额不可大于可提现金额");
					  throw new BusinessException(BusinessCode.CODE_610035);
				  }
			  }else{
				  LOGGER.info("当前门店没有可提现金额");
				  throw new BusinessException(BusinessCode.CODE_610038);
			  }
			  withdrawalPage.setPresented_money(storeBankroll.getPresentedMoney());
		  }else{
			  LOGGER.info("账户没有可提现资金");
			  throw new BusinessException(BusinessCode.CODE_610038);
		  } 
		  
		  withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
		  withdrawalPage.setRate(payWithDrawalConfig.getRate());
		  LOGGER.info("当前用户可提现信息：---"+withdrawalPage);
		  return withdrawalPage;
	}
}
