package com.winhxd.b2c.pay.api;

import java.util.Date;

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

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.StoreBankCardCondition;
import com.winhxd.b2c.common.domain.pay.model.StoreBankCard;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankCardVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.pay.service.impl.PayStoreBankCardServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

	@ApiOperation(value = "B端获取银行卡信息", notes = "B端获取银行卡信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常") 
    })
    @RequestMapping(value = "/610/v1/storeBindBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBankCardVO> queryStoreBindBankCard(@RequestBody StoreBankCardCondition condition) {
        String logTitle = "/api-pay/bankCard/610/v1/storeBindBankCard-B端获取银行卡信息";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<StoreBankCardVO> result = new ResponseResult<>();
        StoreBankCardVO storeBankCardInfo = new StoreBankCardVO();
        try {
        	storeBankCardInfo = storeBankCardService.findStoreBankCardInfo(condition);
        	if(storeBankCardInfo == null){
        		LOGGER.info("当前用户没有银行卡信息");
        		throw new BusinessException(BusinessCode.CODE_6101);
        	}else{
        		result.setData(storeBankCardInfo);
        	}
        	LOGGER.info("B端获取银行卡信息返回数据---："+storeBankCardInfo);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("B端获取银行卡信息失败；失败原因---："+ e);
		}
        
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
	
	@ApiOperation(value = "B端绑定银行卡", notes = "B端绑定银行卡")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_6111, message = "银行名称为空"),
            @ApiResponse(code = BusinessCode.CODE_6112, message = "银行卡卡号为空"),
            @ApiResponse(code = BusinessCode.CODE_6113, message = "开户人姓名为空"),
            @ApiResponse(code = BusinessCode.CODE_6114, message = "开户支行或分行为空"),
            @ApiResponse(code = BusinessCode.CODE_6115, message = "手机号为空"),
            @ApiResponse(code = BusinessCode.CODE_6116, message = "验证码为空")
    })
    @RequestMapping(value = "/611/v1/bindStoreBankCard", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> bindStoreBankCard(@RequestBody StoreBankCardCondition condition) {
        String logTitle = "/api-pay/bankCard/611/v1/bindStoreBankCard-B端绑定银行卡";
        LOGGER.info("{}=--开始--{}", logTitle,condition);
        ResponseResult<Integer> result = new ResponseResult<>();
        Integer res = null;
        try {
        	// 校验用户填入的信息是否完善
        	String bankName = condition.getBankName();
        	if(StringUtils.isEmpty(bankName)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6111);
        		throw new BusinessException(BusinessCode.CODE_6111);
        	}
        	String cardNumber = condition.getCardNumber();
        	if(StringUtils.isEmpty(cardNumber)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6112);
        		throw new BusinessException(BusinessCode.CODE_6112);
        	}
        	String bankUserName = condition.getBankUserName();
        	if(StringUtils.isEmpty(bankUserName)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6113);
        		throw new BusinessException(BusinessCode.CODE_6113);
        	}
        	String bandBranchName = condition.getBandBranchName();
        	if(StringUtils.isEmpty(bandBranchName)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6114);
        		throw new BusinessException(BusinessCode.CODE_6114);
        	}
        	String mobile = condition.getMobile();
        	if(StringUtils.isEmpty(mobile)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6115);
        		throw new BusinessException(BusinessCode.CODE_6115);
        	}
        	String verificationCode = condition.getVerificationCode();
        	if(StringUtils.isEmpty(verificationCode)){
        		LOGGER.info("业务异常："+BusinessCode.CODE_6116);
        		throw new BusinessException(BusinessCode.CODE_6116);
        	}
        	StoreBankCard storeBankCard = new StoreBankCard();
        	BeanUtils.copyProperties(condition, storeBankCard);
        	storeBankCard.setCreated(new Date());
        	storeBankCard.setUpdated(new Date());
        	storeBankCard.setCreatedBy(condition.getStoreId());
        	storeBankCard.setUpdatedBy(condition.getStoreId());
        	storeBankCard.setCreatedByName(bankUserName);
        	storeBankCard.setUpdatedByName(bankUserName);
        	System.out.print("storeBankCard----"+storeBankCard);
        	res = storeBankCardService.saveStoreBankCard(storeBankCard);
        	if(res > 0){
        		// 银行卡绑定成功之后，需要给用户发送短信提醒
        		
        	}
        	result.setCode(res);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("B端绑定银行卡失败；失败原因---："+ e);
		}
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
}
