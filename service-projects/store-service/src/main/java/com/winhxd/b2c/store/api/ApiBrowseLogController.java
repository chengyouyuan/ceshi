package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BrowseLogCondition;
import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * C端用户浏览门店记录日志
 *
 * @author liutong
 * @date 2018-08-07 17:59:13
 */
@RestController
@RequestMapping(value = "api-store/")
public class ApiBrowseLogController {

    private static final Logger logger = LoggerFactory.getLogger(ApiBrowseLogController.class);

    @Autowired
    private StoreBrowseLogService storeBrowseLogService;

    @ApiOperation(value = "C端用户浏览门店进入日志", notes = "C端用户浏览门店进入日志")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1016/v1/saveBrowseLogLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult saveBrowseLogLogin(@RequestBody BrowseLogCondition browseLogCondition) {
        if (browseLogCondition == null || browseLogCondition.getCustomerId() == null) {
            logger.error("惠小店管理首页获取数据接口 getStoreBaseInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (browseLogCondition.getStoreId() == null) {
            logger.error("惠小店管理首页获取数据接口 getStoreBaseInfo,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult responseResult = new ResponseResult<>();
        try {
            logger.info("C端用户浏览门店进入日志接口入参为：{}", JsonUtil.toJSONString(browseLogCondition));
            CustomerBrowseLog customerBrowseLog = new CustomerBrowseLog();
            customerBrowseLog.setStoreId(browseLogCondition.getStoreId());
            customerBrowseLog.setCustomerId(browseLogCondition.getCustomerId());
            customerBrowseLog.setLoginTime(new Date());
            storeBrowseLogService.saveBrowseLogLogin(customerBrowseLog);
        } catch (Exception e) {
            logger.error("C端用户浏览门店进入日志接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "C端用户浏览门店退出日志", notes = "C端用户浏览门店退出日志")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1017/v1/saveBrowseLogLogout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult saveBrowseLogLogout(@RequestBody BrowseLogCondition browseLogCondition) {
        if (browseLogCondition == null || browseLogCondition.getStoreId() == null) {
            logger.error("惠小店开店基础信息查询接口 saveBrowseLogoutLog,storeId参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult responseResult = new ResponseResult<>();
        try {
            logger.info("C端用户浏览门店退出日志接口入参为：{}", JsonUtil.toJSONString(browseLogCondition));
            Date currentTime = new Date();
            CustomerBrowseLog customerBrowseLog = storeBrowseLogService.getIdForLoginOut(browseLogCondition.getStoreId(),browseLogCondition.getCustomerId());
            customerBrowseLog.setLogoutTime(currentTime);
            long times = (currentTime.getTime() - customerBrowseLog.getLoginTime().getTime());
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
