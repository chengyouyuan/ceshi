package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifiCodeCondtion;
import com.winhxd.b2c.common.domain.pay.enums.PayWithdrawalTypeEnum;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.GeneratePwd;
import com.winhxd.b2c.common.util.MessageSendUtils;
import com.winhxd.b2c.pay.service.impl.PayStoreBankCardServiceImpl;
import com.winhxd.b2c.pay.service.impl.PayStoreWalletServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhanghuan
 * @date 2018/8/10
 */
@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayStoreBindBankCardController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreBindBankCardController.class);
	
	@Autowired
	private PayStoreBankCardServiceImpl storeBankCardService;
	
	@Autowired
	private PayStoreWalletServiceImpl payStoreWalletMapperService;
	
	@Autowired
	MessageSendUtils messageSendUtils;
	
	@Resource
	private Cache redisClusterCache;
	
	private static final int MOBILEVERIFICATIONCODE = 5*60;// 验证码有效时间
	
	@ApiOperation(value = "B端绑定银行卡", notes = "B端绑定银行卡")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_610011, message = "银行名称为空"),
            @ApiResponse(code = BusinessCode.CODE_610012, message = "银行卡卡号为空"),
            @ApiResponse(code = BusinessCode.CODE_610013, message = "开户人姓名为空"),
            @ApiResponse(code = BusinessCode.CODE_610014, message = "开户支行或分行为空"),
            @ApiResponse(code = BusinessCode.CODE_610015, message = "手机号为空"),
            @ApiResponse(code = BusinessCode.CODE_610016, message = "验证码为空"),
            @ApiResponse(code = BusinessCode.CODE_610017, message = "B端绑定银行卡失败"),
            @ApiResponse(code = BusinessCode.CODE_610019, message = "验证码输入不正确"),
            @ApiResponse(code = BusinessCode.CODE_610020, message = "验证码已失效"),
            @ApiResponse(code = BusinessCode.CODE_610024, message = "当前要绑定的银行卡已经存在"),
            @ApiResponse(code = BusinessCode.CODE_610029, message = "请输入银行swiftcode")
    })
    @RequestMapping(value = "/6105/v1/bindStoreBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> bindStoreBankCard(@RequestBody StoreBankCardCondition condition) {
        String logTitle = "/api-pay/pay/6105/v1/bindStoreBankCard-B端绑定银行卡";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<Integer> result = new ResponseResult<>();
    	LOGGER.info("B端绑定银行卡参数storeBankCard----"+condition);
		// 校验用户填入的信息是否完善
		String bankName = condition.getBankName();
		if (StringUtils.isEmpty(bankName)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610011);
			throw new BusinessException(BusinessCode.CODE_610011);
		}
		String cardNumber = condition.getCardNumber();
		if (StringUtils.isEmpty(cardNumber)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610012);
			throw new BusinessException(BusinessCode.CODE_610012);
		}
		String bankUserName = condition.getBankUserName();
		if (StringUtils.isEmpty(bankUserName)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610013);
			throw new BusinessException(BusinessCode.CODE_610013);
		}
		String bandBranchName = condition.getBandBranchName();
		if (StringUtils.isEmpty(bandBranchName)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610014);
			throw new BusinessException(BusinessCode.CODE_610014);
		}
		String mobile = condition.getMobile();
		if (StringUtils.isEmpty(mobile)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610015);
			throw new BusinessException(BusinessCode.CODE_610015);
		}
		String verificationCode = condition.getVerificationCode();
		if (StringUtils.isEmpty(verificationCode)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610016);
			throw new BusinessException(BusinessCode.CODE_610016);
		}
		String swiftcode = condition.getSwiftCode();
		if (StringUtils.isEmpty(swiftcode)) {
			LOGGER.info("业务异常：" + BusinessCode.CODE_610029);
			throw new BusinessException(BusinessCode.CODE_610029);
		}

    	Integer res = storeBankCardService.saveStoreBankCard(condition);
    	result.setCode(res);
    	LOGGER.info("绑定银行卡返回值：-------"+res);
    	if(res > 0){
    		LOGGER.info("B端绑定银行卡失败；");
    		throw new BusinessException(BusinessCode.CODE_610017);
    	} 
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
	
	@ApiOperation(value = "B端绑定微信账户", notes = "B端绑定微信账户")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
    		@ApiResponse(code = BusinessCode.CODE_610015, message = "手机号为空"),
    		@ApiResponse(code = BusinessCode.CODE_610016, message = "验证码为空"),
    		@ApiResponse(code = BusinessCode.CODE_610019, message = "验证码输入不正确"),
    		@ApiResponse(code = BusinessCode.CODE_610031, message = "请输入微信账号"),
    		@ApiResponse(code = BusinessCode.CODE_610021, message = "查询结果有误，请联系管理员"),
    		@ApiResponse(code = BusinessCode.CODE_610017, message = "B端绑定微信失败") 
    })
    @RequestMapping(value = "/6110/v1/bindWeixiAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> bindWeixiAccount(@RequestBody PayStoreWalletCondition condition) {
        String logTitle = "/api-pay/pay/6110/v1/bindWeixiAccount-B端绑定微信账户";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<Integer> result = new ResponseResult<>();
    	// 获取当前门店id
    	Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
    	//////////////////测试门店id//////////////////
//    	Long businessId = 84l;
    	///////////////////////////////////////////
    	condition.setStoreId(businessId);
    	LOGGER.info("B端绑定微信参数payStoreWallet----"+condition);
    	payStoreWalletMapperService.savePayStoreWallet(condition);
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
	
	/**获取短信验证码*/
	@ApiOperation(value = "获取短信验证码", notes = "获取短信验证码")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_610015, message = "手机号为空"),
            @ApiResponse(code = BusinessCode.CODE_610016, message = "验证码为空"),
            @ApiResponse(code = BusinessCode.CODE_610018, message = "验证码已生成"),
            @ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型")
    })
    @RequestMapping(value = "/6106/v1/verificationCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<String> getVerificationCode(@RequestBody VerifiCodeCondtion condition) {
		String logTitle = "/api-pay/pay/6106/v1/verificationCode-获取短信验证码";
		LOGGER.info("{}=--开始--{}", logTitle,condition);
		ResponseResult<String> result = new ResponseResult<String>();
		Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
		// 验证当前传入的参数是否正确
		vaildatVerifiCode(condition);
		short withdrawType = condition.getWithdrawType();
		String modileVerifyCode = redisClusterCache.get(CacheName.PAY_VERIFICATION_CODE+withdrawType+"_"+businessId);
		modileVerifyCode = GeneratePwd.generatePwd6Mobile();
		LOGGER.info("验证码生成后:------"+modileVerifyCode);
		// 将验证码存放到redis中
		redisClusterCache.set(CacheName.PAY_VERIFICATION_CODE+withdrawType+"_"+businessId, modileVerifyCode);
		redisClusterCache.expire(CacheName.PAY_VERIFICATION_CODE+withdrawType+"_"+businessId, MOBILEVERIFICATIONCODE);
		result.setData("验证码："+modileVerifyCode);
		SMSCondition sMSCondition = new SMSCondition();
		if(withdrawType == PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()){
			sMSCondition.setContent("【惠下单】"+ modileVerifyCode+"（惠小店验证码，您正在进行银行卡绑定，5分钟有效，请勿泄漏给他人）");
		}else if(withdrawType == PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode()){
			sMSCondition.setContent("【惠下单】"+ modileVerifyCode+"（惠小店验证码，您正在授权微信提现账号，5分钟有效，请勿泄漏给他人）");
		}
		sMSCondition.setMobile(condition.getMobile());
		messageSendUtils.sendSms(sMSCondition);
		LOGGER.info("{}=--结束 result={}", logTitle, result);
		return result;
	}

	private void vaildatVerifiCode(VerifiCodeCondtion condition) {
		String mobile = condition.getMobile();
		if(StringUtils.isEmpty(mobile)){
			LOGGER.info("手机号为空");
			throw new BusinessException(BusinessCode.CODE_610015);
		}
		short withdrawType = condition.getWithdrawType();
		if(withdrawType == 0){
			LOGGER.info("请传入提现类型");
			throw new BusinessException(BusinessCode.CODE_610022);
		}
	}
	
}
