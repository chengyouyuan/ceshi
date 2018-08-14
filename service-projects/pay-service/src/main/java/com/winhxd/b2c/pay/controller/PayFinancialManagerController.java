package com.winhxd.b2c.pay.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.model.PayFinanceAccountDetail;
import com.winhxd.b2c.common.domain.pay.vo.PayFinanceAccountDetailVO;
import com.winhxd.b2c.common.feign.pay.FinancialManagerServiceClient;
import com.winhxd.b2c.pay.service.impl.PayFinancialManagerServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "PayFinancialManager")
public class PayFinancialManagerController implements FinancialManagerServiceClient{
	 private static final Logger logger = LoggerFactory.getLogger(PayFinancialManagerController.class);
	 
	 @Autowired
	 private PayFinancialManagerServiceImpl payFinancialManagerServiceImpl;
	 
	 /**出入帐汇总查询*/
	 @Override
	 @ApiOperation(value = "出入帐汇总查询", notes = "出入帐汇总查询")
	 @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_610021, message = "查询结果有误，请联系管理员")})
	 @PostMapping("/pay/61001/v1/queryStoreFinancialSummary")
	 public ResponseResult<PayFinanceAccountDetailVO> queryStoreFinancialSummary() {
        logger.info("/pay/61001/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        ResponseResult<PayFinanceAccountDetailVO> result = new ResponseResult<PayFinanceAccountDetailVO>();
        StoreUser currentStoreUser = UserContext.getCurrentStoreUser();
        PayFinanceAccountDetailVO findFinanceAccountDetail = payFinancialManagerServiceImpl.findFinanceAccountDetail(currentStoreUser.getBusinessId());
        result.setData(findFinanceAccountDetail);
        logger.info("/pay/v1/queryStoreFinancialSummary/ 出入帐汇总查询");
        return result;
	 }
	 
	 /**财务入账明细*/
	 
	 /**财务出账明细*/
	 
	 /**公司入账明细*/
}
