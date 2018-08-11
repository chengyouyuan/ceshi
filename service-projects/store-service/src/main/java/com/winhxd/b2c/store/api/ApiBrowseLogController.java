package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * C端用户浏览门店记录日志
 *
 * @author liutong
 * @date 2018-08-07 17:59:13
 */
@Api(tags = "C端用户浏览门店记录日志")
@RestController
@RequestMapping(value = "/api-store/store")
public class ApiBrowseLogController {

    private static final Logger logger = LoggerFactory.getLogger(ApiBrowseLogController.class);

    @Autowired
    private StoreBrowseLogService storeBrowseLogService;

    @ApiOperation(value = "C端用户浏览门店进入日志", notes = "C端用户浏览门店进入日志")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1016/v1/saveBrowseLogLogin/{storeCustomerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> saveBrowseLogLogin(@PathVariable("storeCustomerId") Long storeCustomerId) {
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        if (UserContext.getCurrentStoreUser() == null) {
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.info("C端用户浏览门店进入日志 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        try {
            Long customerId = UserContext.getCurrentCustomerUser().getCustomerId();
            logger.info("C端用户浏览门店进入日志接口入参为 storeCustomerId：{}, customerId:{}", storeCustomerId, customerId);
            CustomerBrowseLog customerBrowseLog = new CustomerBrowseLog();
            customerBrowseLog.setStoreCustomerId(storeCustomerId);
            customerBrowseLog.setCustomerId(customerId);
            customerBrowseLog.setLoginDatetime(new Date());
            storeBrowseLogService.saveBrowseLogLogin(customerBrowseLog);
        } catch (Exception e) {
            logger.error("C端用户浏览门店进入日志接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "C端用户浏览门店退出日志", notes = "C端用户浏览门店退出日志")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！")})
    @PostMapping(value = "/1017/v1/saveBrowseLogLogout/{storeCustomerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> saveBrowseLogLogout(@PathVariable("storeCustomerId") Long storeCustomerId) {
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        if (UserContext.getCurrentStoreUser() == null) {
            responseResult.setCode(BusinessCode.CODE_1001);
            logger.info("C端用户浏览门店退出日志 未获取到当前用户信息");
            throw new BusinessException(BusinessCode.CODE_1001);
        }
        if (storeCustomerId == null) {
            logger.error("C端用户浏览门店退出日志 saveBrowseLogLogout,storeCustomerId 参数为空");
            throw new BusinessException(BusinessCode.CODE_200002, "storeCustomerId 参数为空");
        }
        try {
            Long customerId = UserContext.getCurrentCustomerUser().getCustomerId();
            logger.info("CC端用户浏览门店退出日志接口入参为 storeCustomerId:{}, customerId：{}", storeCustomerId, customerId);
            Date currentTime = new Date();
            CustomerBrowseLog customerBrowseLog = storeBrowseLogService.getIdForLoginOut(storeCustomerId, customerId);
            customerBrowseLog.setLogoutDatetime(currentTime);
            long times = (currentTime.getTime() - customerBrowseLog.getLoginDatetime().getTime());
            customerBrowseLog.setStayTimeMillis(times);
            storeBrowseLogService.modifyByPrimaryKey(customerBrowseLog);
        } catch (Exception e) {
            logger.error("C端用户浏览门店退出日志接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

}
