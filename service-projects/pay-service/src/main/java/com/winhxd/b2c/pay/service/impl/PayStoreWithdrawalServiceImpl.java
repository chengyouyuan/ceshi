package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
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
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
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
	private StoreServiceClient storeServiceClient;
	@Autowired
	private Cache cache;
	
	@Autowired
	MessageSendUtils messageSendUtils;
	
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
		PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
		// 返回手机号参数
		ResponseResult<StoreUserInfoVO> findStoreUserInfo = storeServiceClient.findStoreUserInfo(businessId);
		String storeMobile = "";
		if(findStoreUserInfo != null && findStoreUserInfo.getData() != null){
			storeMobile = findStoreUserInfo.getData().getStoreMobile();
		}
		withdrawalPage.setMobile(storeMobile);
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
//				 withdrawalPage.setMobile(bankCard.getMobile());
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
	
	/**验证当前用户的提现次数*/
	public void validWithdrawCount(Long storeId){
		int maxcount = payWithDrawalConfig.getMaxCount();
		List<PayWithdrawals> withdrawInfo = payWithdrawalsMapper.selectWithdrawCount(storeId);
		if(withdrawInfo != null && withdrawInfo.size() >= maxcount){
			LOGGER.info("您本日提现已达3次");
			throw new BusinessException(BusinessCode.CODE_610902);
		}
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
		// 验证提现次数
		validWithdrawCount(businessId);
		
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
		payWithdrawal.setMobile(condition.getMobile());
		if(bankType == condition.getWithdrawType()){
			//验证银行卡是否和门店绑定
			Map<String, Object> map=new HashMap<>();
			map.put("storeId", businessId);
			map.put("cardNumber", condition.getPaymentAccount());
			map.put("swiftCode", condition.getSwiftCode());
			List<StoreBankCard> bankCards = storeBankCardMapper.selectByStoreIdAndCardNumber(map);
			if(CollectionUtils.isEmpty(bankCards)){
				LOGGER.info("参数错误：门店和银行卡不匹配");
				throw new BusinessException(BusinessCode.CODE_610025);
			} 
			
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(bankType);
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
			Map<String, Object> map=new HashMap<>();
			map.put("storeId", businessId);
			map.put("openid", condition.getBuyerId());
			List<PayStoreWallet> payStoreWallet = payStoreWalletMapper.selectByStoreIdAndOpenid(map);
			if(CollectionUtils.isEmpty(payStoreWallet)){
				result.setMessage("参数错误：门店和微信钱包不匹配");
				throw new BusinessException(BusinessCode.CODE_610026);
			}
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(weixType);
			payWithdrawal.setBuyerId(condition.getBuyerId());
			payWithdrawal.setPaymentAccount(condition.getBuyerId());
			payWithdrawal.setName(condition.getNick());
			payWithdrawal.setCreatedByName(condition.getNick());
			payWithdrawal.setUpdatedByName(condition.getNick());
			
			payWithdrawal.setCmmsAmt(BigDecimal.valueOf(0));
			payWithdrawal.setRealFee(condition.getTotalFee());
			payWithdrawal.setRate(BigDecimal.valueOf(0));
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
		
		// 发送短信消息
		PayUtil.sendSmsMsg(messageSendUtils,condition.getMobile(),PayNotifyMsg.STORE_APPLY_WITHDRWAL);
	} 
	
	// 计算手续费率
	public BigDecimal countCmms(BigDecimal rate,BigDecimal totalFee){
		BigDecimal cmms = totalFee.multiply(rate).setScale(2, BigDecimal.ROUND_HALF_UP);
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
			LOGGER.info("提现类型为空");
			res = BusinessCode.CODE_610022;
			throw new BusinessException(res);
		}
		
		BigDecimal totalFee = condition.getTotalFee();
		if(totalFee == null||(!NumberUtil.isPositiveDecimal(totalFee.toString())&&!NumberUtil.isPositiveInteger(totalFee.toString()))){
			LOGGER.info("提现金额输入有误");
			res = BusinessCode.CODE_610032;
			throw new BusinessException(res);
		}
		String totalFeeStr=totalFee.toString();
		if (totalFeeStr.indexOf(".")!=-1) {
			//最多支持两位小数
			String [] totalFeeArr=totalFeeStr.split("\\.");
			if (totalFeeArr!=null&&totalFeeArr[1].length()>2) {
				LOGGER.info("提现金额超过了两位小数");
				res = BusinessCode.CODE_610032;
				throw new BusinessException(res);
			}
		}
		
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
		
		String mobile = condition.getMobile();
		if(StringUtils.isEmpty(mobile)){
			res = BusinessCode.CODE_610015;
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
			
		}
		//最小手续费
		BigDecimal min = payWithDrawalConfig.getMinMoney();
		if (totalFee.compareTo(min)<=0) {
			LOGGER.info("提现金额须大于1元");
			throw new BusinessException(BusinessCode.CODE_611107);
		}
		// 最大提现额度
		BigDecimal max = payWithDrawalConfig.getMaxMoney();
		if (totalFee.compareTo(max)>0) {
			LOGGER.info("单笔提现须小于2万元");
			throw new BusinessException(BusinessCode.CODE_611108);
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
	    StoreUser storeUser = UserContext.getCurrentStoreUser();
        if(storeUser==null||storeUser.getBusinessId()==null){
			LOGGER.info("未获取到门店数据");
			throw new BusinessException(BusinessCode.CODE_610801);
		}
        Long storeId = storeUser.getBusinessId();
        Short withdrawType=condition.getWithdrawType();
        BigDecimal totalFee=condition.getTotalFee();
		// 验证提现次数
        validWithdrawCount(storeId);
		if (withdrawType==null) {
			LOGGER.info(log+"提现类型为空");
			throw new BusinessException(BusinessCode.CODE_611102);
		}
		if (totalFee==null) {
			LOGGER.info(log+"提现金额为空");
			throw new BusinessException(BusinessCode.CODE_611103);
		}
		if(!NumberUtil.isPositiveDecimal(totalFee.toString())&&!NumberUtil.isPositiveInteger(totalFee.toString())){
			LOGGER.info("提现金额输入有误");
			throw new BusinessException(BusinessCode.CODE_611106);
		}
		String totalFeeStr=totalFee.toString();
		if (totalFeeStr.indexOf(".")!=-1) {
			//最多支持两位小数
			String [] totalFeeArr=totalFeeStr.split("\\.");
			if (totalFeeArr!=null&&totalFeeArr[1].length()>2) {
				LOGGER.info("提现金额超过了两位小数");
				throw new BusinessException(BusinessCode.CODE_611106);
			}
		}
		//最小手续费
		BigDecimal min = payWithDrawalConfig.getMinMoney();
		if (totalFee.compareTo(min)<=0) {
			LOGGER.info("提现金额须大于1元");
			throw new BusinessException(BusinessCode.CODE_611107);
		}
		// 最大提现额度
		BigDecimal max = payWithDrawalConfig.getMaxMoney();
		if (totalFee.compareTo(max)>0) {
			LOGGER.info("单笔提现须小于2万元");
			throw new BusinessException(BusinessCode.CODE_611108);
		}
		
		LOGGER.info(log+"参数为--"+condition.toString());
		
        StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(storeId);
        if(storeBankroll == null||storeBankroll.getStoreId()==null){
        	LOGGER.info(log+"可提现金额不足");
        	throw new BusinessException(BusinessCode.CODE_611104);
        }
        //判断提现金额是否大于可提现金额
        BigDecimal total = storeBankroll.getPresentedMoney();
        if(totalFee.compareTo(total) == 1){
        	LOGGER.info(log+"可提现金额不足");
        	throw new BusinessException(BusinessCode.CODE_611104);
        }
		CalculationCmmsAmtVO vo=new CalculationCmmsAmtVO();
		BigDecimal rate = payWithDrawalConfig.getRate();
		BigDecimal cmms = countCmms(rate,totalFee);
		if (withdrawType.equals(PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode())) {
			cmms=BigDecimal.valueOf(0);
		} 
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
