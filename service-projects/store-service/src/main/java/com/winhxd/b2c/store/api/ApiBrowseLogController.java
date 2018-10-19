package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.StoreBrowseLogCondition;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！")})
    @PostMapping(value = "/1016/v1/saveBrowseLogLogin", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> saveBrowseLogLogin(@RequestBody StoreBrowseLogCondition condition) {
        if (condition.getStoreId() == null) {
            logger.error("C端用户浏览门店进入日志 saveBrowseLogLogout, 参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        Long customerId = UserContext.getCurrentCustomerUser().getCustomerId();
        logger.info("C端用户浏览门店进入日志 入参为 storeId：{}, customerId:{}", condition.getStoreId(), customerId);
        storeBrowseLogService.saveBrowseLogLogin(condition.getStoreId(),customerId);
        return new ResponseResult<>();
    }

    @ApiOperation(value = "C端用户浏览门店退出日志", notes = "C端用户浏览门店退出日志")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！")})
    @PostMapping(value = "/1017/v1/saveBrowseLogLogout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> saveBrowseLogLogout(@RequestBody StoreBrowseLogCondition condition) {
        if (condition.getStoreId() == null) {
            logger.error("C端用户浏览门店退出日志 saveBrowseLogLogout,storeCustomerId 参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        Long customerId = UserContext.getCurrentCustomerUser().getCustomerId();
        logger.info("C端用户浏览门店退出日志 入参为 storeId:{}, customerId：{}", condition.getStoreId(), customerId);
        storeBrowseLogService.saveBrowseLogLogout(condition.getStoreId(),customerId);
        return new ResponseResult<>();
    }

}
