package com.winhxd.b2c.pay.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.PayStoreCashCondition;
import com.winhxd.b2c.common.domain.pay.vo.PayStoreTransactionRecordVO;
import com.winhxd.b2c.common.domain.pay.vo.PayWithdrawalsVO;
import com.winhxd.b2c.common.domain.pay.vo.StoreBankrollVO;
import com.winhxd.b2c.pay.service.PayStoreCashService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * @Author wl
 * @Date 2018/8/14 16:56
 * @Description
 **/
@RestController
@Api(tags = "ApiPay")
@RequestMapping(value = "/api-pay/pay")
public class ApiPayStoreCashController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiPayStoreCashController.class);
    @Autowired
    private PayStoreCashService payStoreCashService;

    @ApiOperation(value = "资金提现首页", notes = "资金提现首页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
    @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @RequestMapping(value = "/6013/v1/getStoreBankrollByStoreId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<StoreBankrollVO> getStoreBankrollByStoreId(@RequestBody PayStoreCashCondition condition){
       LOGGER.info("资金提现首页参数:"+condition);
       ResponseResult<StoreBankrollVO> result = new  ResponseResult<StoreBankrollVO>();
       StoreBankrollVO vo = payStoreCashService.getStoreBankrollByStoreId(condition);
       result.setData(vo);
       LOGGER.info("资金提现首页结果:"+result);
       return result;
    }


    @ApiOperation(value = "门店交易记录收支明细", notes = "门店交易记录收支明细")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
    @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @RequestMapping(value = "/6014/v1/getPayStoreTransRecordByStoreId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<PayStoreTransactionRecordVO>> getPayStoreTransRecordByStoreId(@RequestBody PayStoreCashCondition condition){
        LOGGER.info("门店交易记录收支明细:"+condition);
        ResponseResult<PagedList<PayStoreTransactionRecordVO>> result = new ResponseResult<PagedList<PayStoreTransactionRecordVO>>();
        PagedList<PayStoreTransactionRecordVO> pagedList = payStoreCashService.getPayStoreTransRecordByStoreId(condition);
        LOGGER.info("门店交易记录收支明细结果:"+pagedList);
        result.setData(pagedList);
        return result;
    }





    @ApiOperation(value = "门店提现记录", notes = "门店提现记录")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
    @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @RequestMapping(value = "/6015/v1/getPayWithdrawalsByStoreId", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<PayWithdrawalsVO>> getPayWithdrawalsByStoreId(@RequestBody PayStoreCashCondition condition){
        LOGGER.info("门店提现记录:"+condition);
        ResponseResult<PagedList<PayWithdrawalsVO>> result = new ResponseResult<PagedList<PayWithdrawalsVO>>();
        PagedList<PayWithdrawalsVO> pagedList = payStoreCashService.getPayWithdrawalsByStoreId(condition);
        result.setData(pagedList);
        LOGGER.info("门店提现记录结果:"+result);
        return result;
    }



}
