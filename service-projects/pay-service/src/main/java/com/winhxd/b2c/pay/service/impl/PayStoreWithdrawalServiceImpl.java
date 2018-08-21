package com.winhxd.b2c.pay.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayWithdrawalTypeEnum;
import com.winhxd.b2c.common.domain.pay.model.PayStoreWallet;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.model.StoreBankroll;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreApplyWithdrawVO;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreUserInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.pay.config.PayWithdrawalConfig;
import com.winhxd.b2c.pay.dao.PayStoreWalletMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsTypeMapper;
import com.winhxd.b2c.pay.dao.StoreBankrollMapper;
import com.winhxd.b2c.pay.service.PayStoreWithdrawalService;

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
	
	@Resource
	private Cache redisClusterCache;
	
	@Resource
	private PayWithdrawalConfig payWithDrawalConfig;
	
	private static final String ACCOUNT_NAME = "微信钱包";
	
	private static final int EXPIRE_TIME = 60*60*24;

	/**判断当前用户是否绑定了微信或者银行卡，如果绑定过了则返回页面回显信息*/
	@Override
	public ResponseResult<PayWithdrawalPageVO> showPayWithdrawalDetail(PayStoreApplyWithDrawCondition condition) {
		ResponseResult<PayWithdrawalPageVO> result = new ResponseResult<PayWithdrawalPageVO>();
		short bankType = PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode();
		short weixType= PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode();
//		Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
		////////////////////////测试数据////////////////////////////////////
		Long businessId = 1l;
		//////////////////////结束////////////////////////////////////////
		if(bankType == condition.getWithdrawType()){
			ResponseResult<PayStoreUserInfoVO> bindBank = validStoreBindBank(businessId);
			int code = bindBank.getCode();
			result.setCode(code);
			if(code == 0){
				PayStoreUserInfoVO data = bindBank.getData();
				 PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
				 withdrawalPage.setPresented_money(data.getTotalFee());
				 withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
				 LOGGER.info("最大提现额度：---"+ payWithDrawalConfig.getMaxMoney());
				 String carnumber = data.getCardNumber();
				 withdrawalPage.setUserAcountName(data.getBankName()+"("+carnumber.substring(carnumber.length()-4, carnumber.length())+")");
				 withdrawalPage.setRate(payWithDrawalConfig.getRate());
				 withdrawalPage.setBandBranchName(data.getBandBranchName());
				 withdrawalPage.setBankName(data.getBankName());
				 withdrawalPage.setBankUserName(data.getBankUserName());
				 withdrawalPage.setCardNumber(data.getCardNumber());
				 withdrawalPage.setMobile(data.getStoreMobile());
				 withdrawalPage.setSwiftCode(data.getSwiftCode());
//				 withdrawalPage.setPersonId(data.getOpenid());
				 result.setData(withdrawalPage);
				 // 将用户信息保存到redis中，以便在做保存操作的时候获取信息 格式： 电话,用户名称,实际账户总额
				 redisClusterCache.set(CacheName.STOR_WITHDRAWAL_INFO+businessId, data.getStoreMobile()+","+ data.getStoreName()+","+data.getTotalFee());
				 redisClusterCache.expire(CacheName.STOR_WITHDRAWAL_INFO+businessId, EXPIRE_TIME);
			 } 
		}else if(weixType == condition.getWithdrawType()){
			 ResponseResult<PayStoreUserInfoVO> bindAccount = validStoreBindAccount(businessId);
			 int code = bindAccount.getCode();
			 result.setCode(code);
			 if(code == 0){
				 PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
				 PayStoreUserInfoVO data = bindAccount.getData();
				 withdrawalPage.setPresented_money(data.getTotalFee());
				 withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
				 withdrawalPage.setUserAcountName(ACCOUNT_NAME+"("+data.getNick()+")");
				 withdrawalPage.setMobile(data.getStoreMobile());
				 withdrawalPage.setNick(data.getNick());
				 withdrawalPage.setOpenid(data.getOpenid());
				 withdrawalPage.setRate(payWithDrawalConfig.getRate());
				 result.setData(withdrawalPage);
				 redisClusterCache.set(CacheName.STOR_WITHDRAWAL_INFO+businessId, data.getOpenid()+","+ data.getName()+","+data.getTotalFee());
				 redisClusterCache.expire(CacheName.STOR_WITHDRAWAL_INFO+businessId, EXPIRE_TIME);
			 }
		}
		return result;
	}

	@Override
	public ResponseResult<Integer> saveStorWithdrawalInfo(@RequestBody PayStoreApplyWithDrawCondition condition) {
		ResponseResult<Integer> result = new ResponseResult<Integer>();
//		Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
		///////////////测试数据//////////////////////////
		Long businessId = 1l;
		//////////////////结束/////////////////////////
		String payWithdrawalStr = redisClusterCache.get(CacheName.STOR_WITHDRAWAL_CHECK_INFO+businessId);
		PayWithdrawals payWithdrawal = null;
		if(payWithdrawalStr != null){
			payWithdrawal = JsonUtil.parseJSONObject(payWithdrawalStr, PayWithdrawals.class);
			LOGGER.info("获取redis中获取提现缓存数据：--"+payWithdrawal);
			saveStoreWithdrawalInfo(businessId, payWithdrawal);
			result.setCode(0);
		}else{
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
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
	
	private int valiApplyWithDrawCondition(PayStoreApplyWithDrawCondition condition) {
		int res = 0;
		BigDecimal totalFee = condition.getTotalFee();
		if(BigDecimal.ZERO.equals(totalFee) || totalFee == null){
			res = BusinessCode.CODE_610032;
		}
		short type = condition.getFlowDirectionType();
		if(type == 0){
			res = BusinessCode.CODE_610033;
		}
		
		String name = condition.getFlowDirectionName();
		if(StringUtils.isEmpty(name)){
			res = BusinessCode.CODE_610034;
		}
		short withdrawType = condition.getWithdrawType();
		if(withdrawType == 1){
			String openId = condition.getBuyerId();
			if(StringUtils.isEmpty(openId)){
				res = BusinessCode.CODE_610031;
			}
		}
		String paymentAccount = condition.getPaymentAccount();
		if(StringUtils.isEmpty(paymentAccount)){
			res = BusinessCode.CODE_610012;
		}
		String swiftCode = condition.getSwiftCode();
		if(StringUtils.isEmpty(swiftCode)){
			res = BusinessCode.CODE_610029;
		}
		return res;
	}

	/** 验证当前用户是否绑定了微信账户；  验证方式暂时是查询当前用户是否拥有微信的唯一标识*/
	public ResponseResult<PayStoreUserInfoVO> validStoreBindAccount(Long businessId){
		ResponseResult<PayStoreUserInfoVO> res = new ResponseResult<PayStoreUserInfoVO>();
		// 获取门店微信账户信息
		List<PayStoreWallet> payStoreWallet = payStoreWalletMapper.selectByStoreId(businessId);
		// 获取门店资金信息
		StoreBankroll storeBankroll = storeBankrollMapper.selectStoreBankrollByStoreId(businessId);
		PayStoreUserInfoVO storeUserinfo = new PayStoreUserInfoVO();
		if(storeBankroll != null){
			PayStoreWallet storeWallet = payStoreWallet.get(0);
			if(payStoreWallet.size() > 0 && storeWallet != null){
				storeUserinfo.setFlowDirectionName(storeWallet.getNick());
				storeUserinfo.setFlowDirectionType((short)1);
				storeUserinfo.setOpenid(storeWallet.getOpenid());
				storeUserinfo.setTotalMoney(storeBankroll.getTotalMoeny());
				storeUserinfo.setTotalFee(storeBankroll.getPresentedMoney());
				storeUserinfo.setNick(storeWallet.getNick());
				storeUserinfo.setName(storeWallet.getName());
				res.setData(storeUserinfo);
				res.setCode(0);
			}else{
				res.setCode(BusinessCode.CODE_610026);
				LOGGER.info("当前用户没有绑定微信账户");
			}
		}else{
			res.setCode(BusinessCode.CODE_610027);
			LOGGER.info("门店当前没有可提现的金额记录");
		}
		return res;
	}
	/**验证当前用户是否绑定了银行卡账户,并且返回最新的一条银行卡信息*/
	public ResponseResult<PayStoreUserInfoVO> validStoreBindBank(Long businessId){
		ResponseResult<PayStoreUserInfoVO> res = new ResponseResult<PayStoreUserInfoVO>();
		List<PayStoreUserInfoVO> selectStorBankCardInfo = payWithdrawalsMapper.getStorBankCardInfo(businessId);
		LOGGER.info("当前用户绑定银行卡列表：----"+selectStorBankCardInfo);
		if(selectStorBankCardInfo == null){
			res.setCode(BusinessCode.CODE_610025);
			LOGGER.info("当前用户没有绑定银行卡");
		}else{
			res.setCode(0);
			res.setData(selectStorBankCardInfo.get(0));//返回最新插入的一条银行卡信息
		}
		return res;
	}
	/**验证通过之后保存当前的用户提现信息*/
	public ResponseResult<Integer> saveStoreWithdrawalInfo(Long businessId,PayWithdrawals payWithdrawals){
		ResponseResult<Integer> res = new ResponseResult<Integer>();
		int re = payWithdrawalsMapper.insertSelective(payWithdrawals);
		LOGGER.info("保存用户提现信息返回值：----"+re);
		return res;
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
	public ResponseResult<List<PayWithdrawalsType>> getAllWithdrawalType() {
		ResponseResult<List<PayWithdrawalsType>> result = new ResponseResult<>();
		List<PayWithdrawalsType> types = payWithdrawalsTypeMapper.selectAll();
		 LOGGER.info("types------"+types);
		if(!types.isEmpty()){
			result.setData(types);
		}else{
			result.setCode(BusinessCode.CODE_610023);
		}
		return result;
	}

	//审核当前用户提现信息
	public ResponseResult<PayStoreApplyWithdrawVO> checkStorWithdrawalInfo(PayStoreApplyWithDrawCondition condition) {
		ResponseResult<PayStoreApplyWithdrawVO> result = new ResponseResult<PayStoreApplyWithdrawVO>();
//		Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
		///////////////测试数据//////////////////////////
		Long businessId = 1l;
		//////////////////结束/////////////////////////
		// 验证入参是否传入正确
		int res = valiApplyWithDrawCondition(condition);
		if(res > 0){
			result.setCode(res);
			return result;
		}
		// 返回页面提现审核页面参数
		PayStoreApplyWithdrawVO payStoreApplyWithdraw = new PayStoreApplyWithdrawVO();
		
		String userInfo = redisClusterCache.get(CacheName.STOR_WITHDRAWAL_INFO+businessId);
		String[] user = userInfo.split(",");
		short bankType = PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode();
		short weixType= PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode();
		PayWithdrawals payWithdrawal = new PayWithdrawals();
		payWithdrawal.setStoreId(businessId);
		// 生成提现订单号
		payWithdrawal.setWithdrawalsNo(generateWithdrawalsNo());
		BigDecimal totalFee = condition.getTotalFee();
		// 当前提现金而不能大于实际账户总额
		BigDecimal total = new BigDecimal(user[2]);
		if(totalFee.compareTo(total) == 1){
			result.setCode(BusinessCode.CODE_610035);
			LOGGER.info("业务异常："+BusinessCode.CODE_610035);
			return result;
		}
		payWithdrawal.setTotalFee(totalFee);
		if(bankType == condition.getWithdrawType()){
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(bankType);
			payWithdrawal.setMobile(user[0]);
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
			
			payStoreApplyWithdraw.setCmmsAmt(cmms);
			payStoreApplyWithdraw.setRealFee(realFee);
			payStoreApplyWithdraw.setRate(rate);
			result.setData(payStoreApplyWithdraw);
		}else if(weixType == condition.getWithdrawType()){
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType(weixType);
			payWithdrawal.setBuyerId(condition.getBuyerId());
		}
		payWithdrawal.setAuditStatus((short)0);
//		payWithdrawal.setAuditDesc(auditDesc);
		payWithdrawal.setName(user[1]);
		payWithdrawal.setCreated(new Date());
		payWithdrawal.setCreatedByName(user[1]);
		payWithdrawal.setCreatedBy(businessId);
		payWithdrawal.setUpdated(new Date());
		payWithdrawal.setUpdatedBy(businessId);
		payWithdrawal.setUpdatedByName(user[1]);
		//将本次的提现数据存入redis
		redisClusterCache.set(CacheName.STOR_WITHDRAWAL_CHECK_INFO+businessId, JsonUtil.toJSONString(payWithdrawal));
		redisClusterCache.expire(CacheName.STOR_WITHDRAWAL_CHECK_INFO+businessId, EXPIRE_TIME);
		redisClusterCache.expire(CacheName.STOR_WITHDRAWAL_INFO+businessId, 0);//删除缓存
		return result;
	}
}
