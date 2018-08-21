package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.SMSCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreWalletCondition;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.condition.VerifiCodeCondtion;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;
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
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
	
	@Autowired
	private Cache cache;
	
	private static final int MOBILEVERIFICATIONCODE = 2*60;// 验证码有效时间

	@ApiOperation(value = "B端获取银行卡信息", notes = "B端获取银行卡信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") 
    })
//    @RequestMapping(value = "/6104/v1/storeBindBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBankCardVO> queryStoreBindBankCard(@RequestBody StoreBankCardCondition condition) {
        String logTitle = "/api-pay/pay/6104/v1/storeBindBankCard-B端获取银行卡信息";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<StoreBankCardVO> result = new ResponseResult<>();
        StoreBankCardVO storeBankCardInfo = new StoreBankCardVO();
        storeBankCardInfo = storeBankCardService.findStoreBankCardInfo(condition);
    	if(storeBankCardInfo == null){
    		LOGGER.info("当前用户没有银行卡信息");
    		result.setCode(BusinessCode.CODE_610001);
    	}else{
    		result.setData(storeBankCardInfo);
    		result.setCode(BusinessCode.CODE_OK);
    	}
    	LOGGER.info("B端获取银行卡信息返回数据---："+storeBankCardInfo);
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
	
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
            @ApiResponse(code = BusinessCode.CODE_610020, message = "请先获取验证码"),
            @ApiResponse(code = BusinessCode.CODE_610024, message = "当前要绑定的银行卡已经存在"),
            @ApiResponse(code = BusinessCode.CODE_610029, message = "请输入银行swiftcode")
    })
    @RequestMapping(value = "/6105/v1/bindStoreBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> bindStoreBankCard(@RequestBody StoreBankCardCondition condition) {
        String logTitle = "/api-pay/pay/6105/v1/bindStoreBankCard-B端绑定银行卡";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<Integer> result = new ResponseResult<>();
    	StoreBankCard storeBankCard = new StoreBankCard();
    	BeanUtils.copyProperties(condition, storeBankCard);
    	LOGGER.info("B端绑定银行卡参数storeBankCard----"+storeBankCard);
    	Integer res = storeBankCardService.saveStoreBankCard(storeBankCard);
    	result.setCode(res);
    	LOGGER.info("绑定银行卡返回值：-------"+res);
    	if(res > 0){
    		result.setData(BusinessCode.CODE_610017);
    		LOGGER.info("B端绑定银行卡失败；");
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
    		@ApiResponse(code = BusinessCode.CODE_610031, message = "请输入微信账号") 
    })
    @RequestMapping(value = "/6110/v1/bindWeixiAccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> bindWeixiAccount(@RequestBody PayStoreWalletCondition condition) {
        String logTitle = "/api-pay/pay/6110/v1/bindWeixiAccount-B端绑定微信账户";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<Integer> result = new ResponseResult<>();
    	BeanUtils.copyProperties(condition, condition);
    	LOGGER.info("B端绑定微信参数payStoreWallet----"+condition);
    	Integer res = payStoreWalletMapperService.savePayStoreWallet(condition);
    	result.setCode(res);
    	LOGGER.info("绑定微信返回值：-------"+res);
    	if(res > 0){
    		result.setData(BusinessCode.CODE_610017);
    		LOGGER.info("B端绑定微信失败；");
    	} 
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
	
	/**获取短信验证码*/
	@ApiOperation(value = "获取短信验证码", notes = "获取短信验证码")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_610016, message = "验证码为空"),
            @ApiResponse(code = BusinessCode.CODE_610018, message = "验证码已生成")
    })
    @RequestMapping(value = "/6106/v1/verificationCode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<String> getVerificationCode(@RequestBody VerifiCodeCondtion condition) {
		String logTitle = "/api-pay/pay/6106/v1/verificationCode-获取短信验证码";
		LOGGER.info("{}=--开始--{}", logTitle,condition);
		ResponseResult<String> result = new ResponseResult<String>();
		
		/////////////////////////////// 测试数据
		StoreUser currentStoreUser = new StoreUser();
		currentStoreUser.setBusinessId(1l);
		Long businessId = currentStoreUser.getBusinessId();
		/////////////////////////////////////////////
//		Long businessId = UserContext.getCurrentStoreUser().getBusinessId();
		
		String modileVerifyCode = cache.get(CacheName.PAY_VERIFICATION_CODE+condition.getWithdrawType()+"_"+businessId);
		LOGGER.info("验证码生成前:------"+modileVerifyCode);
		//生成验证码
		if(modileVerifyCode != null){
			LOGGER.info("验证码已生成");
			result.setCode(BusinessCode.CODE_610018);
		}else{
			modileVerifyCode = GeneratePwd.generate4MobileCode();
			LOGGER.info("验证码生成后:------"+modileVerifyCode);
		} 
		// 将验证码存放到redis中
		cache.set(CacheName.PAY_VERIFICATION_CODE+condition.getWithdrawType()+"_"+businessId, modileVerifyCode);
		cache.expire(CacheName.PAY_VERIFICATION_CODE+condition.getWithdrawType()+"_"+businessId, MOBILEVERIFICATIONCODE);
		if(StringUtils.isEmpty(condition.getMobile())){
			result.setCode(BusinessCode.CODE_610015);
			LOGGER.info("手机号为空");
		}else{
			SMSCondition sMSCondition = new SMSCondition();
			sMSCondition.setContent("您的手机验证码："+ modileVerifyCode+";有效时间2分钟");
			sMSCondition.setMobile(condition.getMobile());
			messageSendUtils.sendSms(sMSCondition);
		}
		LOGGER.info("{}=--结束 result={}", logTitle, result);
		return result;
	}
	
}
