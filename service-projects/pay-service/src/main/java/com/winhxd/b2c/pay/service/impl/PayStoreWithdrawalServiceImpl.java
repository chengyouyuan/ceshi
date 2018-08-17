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
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayWithdrawalTypeEnum;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawals;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreUserInfoVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.pay.config.PayWithdrawalConfig;
import com.winhxd.b2c.pay.dao.PayWithdrawalsMapper;
import com.winhxd.b2c.pay.dao.PayWithdrawalsTypeMapper;
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
	
	@Resource
	Cache cache;
	
	@Resource
	private PayWithdrawalConfig payWithDrawalConfig;

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
			if(code == 0){
				 result.setCode(BusinessCode.CODE_610022); 
				 LOGGER.info("当前用户没有绑定银行卡");
			 }else{
				 PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
				 withdrawalPage.setPresented_money(bindBank.getData().getTotalFee());
				 withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
				 String carnumber = bindBank.getData().getCardNumber();
				 withdrawalPage.setUserAcountName(bindBank.getData().getStoreName()+"("+carnumber.substring(carnumber.length()-4, carnumber.length())+")");
				 result.setData(withdrawalPage);
				 // 将用户信息保存到redis中，以便在做保存操作的时候获取信息 格式： 电话,用户名称
				 cache.set(CacheName.STOR_WITHDRAWAL_INFO+businessId, bindBank.getData().getStoreMobile()+","+ bindBank.getData().getStoreName());
			 } 
		}else if(weixType == condition.getWithdrawType()){
			 ResponseResult<PayStoreUserInfoVO> bindAccount = validStoreBindAccount(businessId);
			 int code = bindAccount.getCode();
			 if(code == 0){
				 result.setCode(BusinessCode.CODE_610022); 
				 LOGGER.info("当前用户没有绑定微信");
			 }else{
				 PayWithdrawalPageVO withdrawalPage = new PayWithdrawalPageVO();
				 withdrawalPage.setPresented_money(bindAccount.getData().getTotalFee());
				 withdrawalPage.setTotal_moeny(payWithDrawalConfig.getMaxMoney());
				 withdrawalPage.setUserAcountName(bindAccount.getData().getOpenid());
				 result.setData(withdrawalPage);
				 cache.set(CacheName.STOR_WITHDRAWAL_INFO+businessId, bindAccount.getData().getContactMobile()+","+ bindAccount.getData().getStoreName());
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
		
		String userInfo = cache.get(CacheName.STOR_WITHDRAWAL_INFO+businessId);
		String[] info = userInfo.split(",");
		short bankType = PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode();
		short weixType= PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode();
		PayWithdrawals payWithdrawal = new PayWithdrawals();
		payWithdrawal.setStoreId(businessId);
		// 生成提现订单号
		payWithdrawal.setWithdrawalsNo(generateWithdrawalsNo());
		payWithdrawal.setTotalFee(condition.getTotalFee());
		if(bankType == condition.getWithdrawType()){
			payWithdrawal.setRealFee((condition.getTotalFee()).multiply(BigDecimal.valueOf(1d).subtract(payWithDrawalConfig.getCmmsamt())));
			System.out.println("当前计算所得银行的实际提现金额："+payWithdrawal.getRealFee() +";当前的费率："+ payWithDrawalConfig.getCmmsamt());
			payWithdrawal.setFlowDirectionName(condition.getFlowDirectionName());
			payWithdrawal.setFlowDirectionType((short)2);
		}else if(weixType == condition.getWithdrawType()){
			payWithdrawal.setRealFee((condition.getTotalFee()).multiply(BigDecimal.valueOf(1d).subtract(payWithDrawalConfig.getRate())));
			System.out.println("当前计算所得微信的实际提现金额："+payWithdrawal.getRealFee() +";当前的费率："+ payWithDrawalConfig.getRate());
			payWithdrawal.setFlowDirectionName("微信");
			payWithdrawal.setFlowDirectionType((short)1);
		}
		payWithdrawal.setCmmsAmt(payWithDrawalConfig.getCmmsamt());
		payWithdrawal.setRate(payWithDrawalConfig.getRate());
		payWithdrawal.setAuditStatus((short)0);
//		payWithdrawal.setAuditDesc(auditDesc);
		payWithdrawal.setName(info[1]);
		payWithdrawal.setMobile(info[0]);
		cache.expire(CacheName.STOR_WITHDRAWAL_INFO+businessId, 0);//删除缓存
		payWithdrawal.setCreated(new Date());
		payWithdrawal.setCreatedByName(info[1]);
		payWithdrawal.setCreatedBy(businessId);
		payWithdrawal.setUpdated(new Date());
		payWithdrawal.setUpdatedBy(businessId);
		payWithdrawal.setUpdatedByName(info[1]);
		saveStoreWithdrawalInfo(businessId, payWithdrawal);
		return result;
	} 
	
	/** 验证当前用户是否绑定了微信账户；  验证方式暂时是查询当前用户是否拥有微信的唯一标识*/
	public ResponseResult<PayStoreUserInfoVO> validStoreBindAccount(Long businessId){
		ResponseResult<PayStoreUserInfoVO> res = new ResponseResult<PayStoreUserInfoVO>();
		PayStoreUserInfoVO payStoreUserInfo = payWithdrawalsMapper.getPayStoreUserInfo(businessId);
		if(payStoreUserInfo != null){
			String openid = payStoreUserInfo.getOpenid();
			int b = openid == null ? 0:1;
			System.out.print("当前用户是否有微信标识：----"+ b);
			res.setCode(b);
			res.setData(payStoreUserInfo);
		}else{
			LOGGER.info("当前没有用户信息，无法判断当前用户是否绑定微信号");
		}
		return res;
	}
	/**验证当前用户是否绑定了银行卡账户*/
	public ResponseResult<PayStoreUserInfoVO> validStoreBindBank(Long businessId){
		ResponseResult<PayStoreUserInfoVO> res = new ResponseResult<PayStoreUserInfoVO>();
		PayStoreUserInfoVO selectStorBankCardInfo = payWithdrawalsMapper.getStorBankCardInfo(businessId);
		if(selectStorBankCardInfo == null){
			res.setCode(0);
		}else{
			res.setCode(1);
			res.setData(selectStorBankCardInfo);
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
        String val = cache.hget(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), withdrawalsNo);
        if (StringUtils.isNotBlank(val)) {
            LOGGER.info("订单号生成出现重复：orderNo={}", withdrawalsNo);
            return false;
        } else {
            cache.hset(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), withdrawalsNo, withdrawalsNo);
            //下一小时就过期
            Long expires = (DateUtils.truncate(DateUtils.addHours(date, 1), Calendar.HOUR_OF_DAY).getTime() - date.getTime()) / 1000;
            cache.expire(CacheName.CACHE_KEY_ORDERNO_CHECK_EXISTS + DateFormatUtils.format(date, withdrawalsNoDateTimeFormatter), expires.intValue());
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
}
