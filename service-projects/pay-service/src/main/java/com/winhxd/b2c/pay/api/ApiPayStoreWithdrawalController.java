package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.CalculationCmmsAmtCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayCondition;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreApplyWithDrawCondition;
import com.winhxd.b2c.common.domain.pay.enums.PayWithdrawalTypeEnum;
import com.winhxd.b2c.common.domain.pay.model.PayWithdrawalsType;
import com.winhxd.b2c.common.domain.pay.vo.CalculationCmmsAmtVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalPageVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsTypeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.IpUtil;
import com.winhxd.b2c.pay.config.PayWithdrawalConfig;
import com.winhxd.b2c.pay.service.PayStoreWithdrawalService;
import com.winhxd.b2c.pay.util.NumberUtil;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayStoreWithdrawalController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreWithdrawalController.class);
	
	@Autowired
	private PayStoreWithdrawalService payStoreWithdrawalService;

    @Resource
    private PayWithdrawalConfig payWithDrawalConfig;
	
	@ApiOperation(value = "返回提现类型", notes = "返回提现类型")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610023, message = "当前没有提现类型")
	})
	@PostMapping(value = "/6107/v1/getWithdrawalType", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PagedList<PayWithdrawalsTypeVO>> getStoreWithdrawalType(@RequestBody PayCondition condition){
		ResponseResult<PagedList<PayWithdrawalsTypeVO>> result = new ResponseResult<PagedList<PayWithdrawalsTypeVO>>();
		List<PayWithdrawalsType> data = payStoreWithdrawalService.getAllWithdrawalType();
		if(data.size() > 0){
            PagedList<PayWithdrawalsTypeVO> pagelistvo = new PagedList<PayWithdrawalsTypeVO>();
            List<PayWithdrawalsTypeVO> listvo = new ArrayList<PayWithdrawalsTypeVO>(5);
			for(PayWithdrawalsType type:data){
				PayWithdrawalsTypeVO typevo = new PayWithdrawalsTypeVO();
				BeanUtils.copyProperties(type, typevo);
				listvo.add(typevo);
				pagelistvo.setData(listvo);
			}
			result.setData(pagelistvo);
		}
		return result;
	}
	
	@ApiOperation(value = "门店进入提现页面", notes = "门店进入提现页面")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型参数"),
		@ApiResponse(code = BusinessCode.CODE_610025, message = "请先绑定银行卡"),
		@ApiResponse(code = BusinessCode.CODE_610026, message = "请先绑定微信账号"),
		@ApiResponse(code = BusinessCode.CODE_610027, message = "门店当前没有可提现的记录"),
		@ApiResponse(code = BusinessCode.CODE_610038, message = "当前门店没有可提现金额")
	})
	@PostMapping(value = "/6108/v1/toWithdrawalPage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<PayWithdrawalPageVO> toPayStoreWithdrawalPage(@RequestBody PayStoreApplyWithDrawCondition condition){
		ResponseResult<PayWithdrawalPageVO> result = new ResponseResult<PayWithdrawalPageVO>();
		LOGGER.info("/6108/v1/toWithdrawalPage-门店进入提现页面入参："+condition);
        if (condition.getWithdrawType() == 0) {
            LOGGER.info("请传入提现类型参数");
            throw new BusinessException(BusinessCode.CODE_610022);
        }
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        Long businessId = storeUser.getBusinessId();
        PayWithdrawalPageVO detail = payStoreWithdrawalService.showPayWithdrawalDetail(condition, businessId);
		result.setData(detail);
		return result;
	}
	
	@ApiOperation(value = "确认门店提现到微信或者银行卡", notes = "确认门店提现到微信或者银行卡")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
		@ApiResponse(code = BusinessCode.CODE_610035, message = "提取限额不能大于实际账户余额"),
		@ApiResponse(code = BusinessCode.CODE_610032, message = "请输入提现金额"),
		@ApiResponse(code = BusinessCode.CODE_610033, message = "请输入流向类型"),
		@ApiResponse(code = BusinessCode.CODE_610034, message = "请输入流向名称"),
		@ApiResponse(code = BusinessCode.CODE_610031, message = "请输入微信账号"),
		@ApiResponse(code = BusinessCode.CODE_610012, message = "银行卡卡号为空"),
		@ApiResponse(code = BusinessCode.CODE_610029, message = "请输入银行swiftcode"),
		@ApiResponse(code = BusinessCode.CODE_610022, message = "请传入提现类型"),
		@ApiResponse(code = BusinessCode.CODE_610036, message = "请输入微信昵称"),
		@ApiResponse(code = BusinessCode.CODE_610037, message = "请输入门店名称"),
		@ApiResponse(code = BusinessCode.CODE_610012, message = "银行卡卡号为空"),
		@ApiResponse(code = BusinessCode.CODE_610015, message = "手机号为空"),
		@ApiResponse(code = BusinessCode.CODE_610038, message = "当前门店没有可提现金额"),
		@ApiResponse(code = BusinessCode.CODE_610026, message = "参数错误：门店和微信钱包不匹配"),
		@ApiResponse(code = BusinessCode.CODE_610902, message = "今天提现次数已达上限"),
		@ApiResponse(code = BusinessCode.CODE_611107, message = "低于最低值1元：提现金额须大于1元"),
		@ApiResponse(code = BusinessCode.CODE_611108, message = "高于最大2万：单笔提现须小于2万元")
	})
	@PostMapping(value = "/6109/v1/withdrawal", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<Integer> payStoreWithdrawal(@RequestBody PayStoreApplyWithDrawCondition condition){
		LOGGER.info("/6109/v1/withdrawal-门店提现到微信或者银行卡："+condition);
		ResponseResult<Integer> result=new ResponseResult<>();
		String ip=IpUtil.getIpAddr();
		condition.setSpbillCreateIp(ip);
        // 验证入参是否传入正确
        valiApplyWithDrawCondition(condition);
		payStoreWithdrawalService.saveStorWithdrawalInfo(condition);
		return result;
	}
	
	@ApiOperation(value = "计算门店提现手续费", notes = "计算门店提现手续费")
	@ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
		@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
	})
	@PostMapping(value = "/6111/v1/calculationCmmsAmt", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	private ResponseResult<CalculationCmmsAmtVO> calculationCmmsAmt(@RequestBody CalculationCmmsAmtCondition condition){
		LOGGER.info("/6111/v1/withdrawal-计算门店提现手续费："+condition);
        ResponseResult<CalculationCmmsAmtVO> result = new ResponseResult<CalculationCmmsAmtVO>();
        if (condition == null) {
            LOGGER.info("参数为空");
            throw new BusinessException(BusinessCode.CODE_611101);
        }
        if (condition.getWithdrawType() == null) {
            LOGGER.info("提现类型为空");
            throw new BusinessException(BusinessCode.CODE_611102);
        }
        BigDecimal totalFee = condition.getTotalFee();
        if (totalFee == null) {
            LOGGER.info("提现金额为空");
            throw new BusinessException(BusinessCode.CODE_611103);
        }
        if (!NumberUtil.isPositiveDecimal(totalFee.toString()) && !NumberUtil.isPositiveInteger(totalFee.toString())) {
            LOGGER.info("提现金额输入有误");
            throw new BusinessException(BusinessCode.CODE_611106);
        }
        String totalFeeStr = totalFee.toString();
        if (totalFeeStr.indexOf(".") != -1) {
            //最多支持两位小数
            String[] totalFeeArr = totalFeeStr.split("\\.");
            if (totalFeeArr != null && totalFeeArr[1].length() > 2) {
                LOGGER.info("提现金额超过了两位小数");
                throw new BusinessException(BusinessCode.CODE_611106);
            }
        }
        BigDecimal min = payWithDrawalConfig.getMinMoney();
        if (totalFee.compareTo(min) <= 0) {
            LOGGER.info("提现金额须大于1元");
            throw new BusinessException(BusinessCode.CODE_611107);
        }
        // 最大提现额度
        BigDecimal max = payWithDrawalConfig.getMaxMoney();
        if (totalFee.compareTo(max) > 0) {
            LOGGER.info("单笔提现须小于2万元");
            throw new BusinessException(BusinessCode.CODE_611108);
        }
		CalculationCmmsAmtVO vo = payStoreWithdrawalService.calculationCmmsAmt(condition);
		result.setData(vo);
		return result;
	}

    private void valiApplyWithDrawCondition(PayStoreApplyWithDrawCondition condition) {
        int res = 0;
        short withdralType = condition.getWithdrawType();
        if (withdralType != PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode() && withdralType != PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()) {
            LOGGER.info("提现类型为空");
            throw new BusinessException(BusinessCode.CODE_610022);
        }
        BigDecimal totalFee = condition.getTotalFee();
        if (totalFee == null || (!NumberUtil.isPositiveDecimal(totalFee.toString()) && !NumberUtil.isPositiveInteger(totalFee.toString()))) {
            LOGGER.info("提现金额输入有误");
            throw new BusinessException(BusinessCode.CODE_610032);
        }
        String totalFeeStr = totalFee.toString();
        if (totalFeeStr.indexOf(".") != -1) {
            //最多支持两位小数
            String[] totalFeeArr = totalFeeStr.split("\\.");
            if (totalFeeArr != null && totalFeeArr[1].length() > 2) {
                LOGGER.info("提现金额超过了两位小数");
                throw new BusinessException(BusinessCode.CODE_610032);
            }
        }
        short type = condition.getFlowDirectionType();
        if (type == 0) {
            throw new BusinessException(BusinessCode.CODE_610033);
        }

        String name = condition.getFlowDirectionName();
        if (StringUtils.isEmpty(name)) {
            throw new BusinessException(BusinessCode.CODE_610034);
        }
        short withdrawType = condition.getWithdrawType();
        if (withdrawType == PayWithdrawalTypeEnum.WECHART_WITHDRAW.getStatusCode()) {
            String openId = condition.getBuyerId();
            if (StringUtils.isEmpty(openId)) {
                throw new BusinessException(BusinessCode.CODE_610031);
            }
            String nick = condition.getNick();
            if (StringUtils.isEmpty(nick)) {
                throw new BusinessException(BusinessCode.CODE_610036);
            }
        }
        String paymentAccount = condition.getPaymentAccount();
        if (StringUtils.isEmpty(paymentAccount)) {
            throw new BusinessException(BusinessCode.CODE_610012);
        }
        String mobile = condition.getMobile();
        if (StringUtils.isEmpty(mobile)) {
            throw new BusinessException(BusinessCode.CODE_610015);
        }
        if (withdrawType == PayWithdrawalTypeEnum.BANKCARD_WITHDRAW.getStatusCode()) {
            String swiftCode = condition.getSwiftCode();
            if (StringUtils.isEmpty(swiftCode)) {
                throw new BusinessException(BusinessCode.CODE_610029);
            }
            String storeName = condition.getStroeName();
            if (StringUtils.isEmpty(storeName)) {
                throw new BusinessException(BusinessCode.CODE_610037);
            }
        }
        //最小手续费
        BigDecimal min = payWithDrawalConfig.getMinMoney();
        if (totalFee.compareTo(min) <= 0) {
            LOGGER.info("提现金额须大于1元");
            throw new BusinessException(BusinessCode.CODE_611107);
        }
        // 最大提现额度
        BigDecimal max = payWithDrawalConfig.getMaxMoney();
        if (totalFee.compareTo(max) > 0) {
            LOGGER.info("单笔提现须小于2万元");
            throw new BusinessException(BusinessCode.CODE_611108);
        }
    }
	
}
