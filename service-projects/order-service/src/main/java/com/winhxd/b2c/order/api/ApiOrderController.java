package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.*;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author pangjianhua
 * @date 2018/8/3 11:16
 */
@RestController
@Api(tags = "ApiOrder")
@RequestMapping(value = "/api-order/order")
public class ApiOrderController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderController.class);

    @Resource
    private OrderService orderService;

    @ApiOperation(value = "B端线下计价订单价格修改", notes = "B端线下计价订单价格修改")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_TOTAL_MONEY, message = "订单金额错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_PAID, message = "订单已经支付，无法修改价格")
    })
    @RequestMapping(value = "/4025/v1/orderPriceChange4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderPriceChange4Store(@RequestBody OrderConfirmCondition condition) {
        String logTitle = "/api-order/order/4025/v1/orderPriceChange4Store-B端线下计价订单价格修改";
        LOGGER.info("{}=--开始--{}", logTitle, JsonUtil.toJSONString(condition));
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            condition.setStoreId(storeUser.getBusinessId());
            this.orderService.orderPriceChange4Store(condition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端自提完成", notes = "B端自提完成")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_PICKUP_CODE, message = "提货码错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
    })
    @RequestMapping(value = "/4024/v1/orderPickup4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderPickup4Store(@RequestBody OrderPickupCondition condition) {
        String logTitle = "/api-order/order/4024/v1/orderPickup4Store-B端订单提货接口";
        LOGGER.info("{}=--开始--{}", logTitle, JsonUtil.toJSONString(condition));
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            condition.setStoreId(storeUser.getBusinessId());
            this.orderService.orderPickup4Store(condition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端接单计价", notes = "B端接单计价")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_TOTAL_MONEY, message = "订单金额错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
    })
    @RequestMapping(value = "/4023/v1/orderConfirm4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderConfirm4Store(@RequestBody OrderConfirmCondition condition) {
        String logTitle = "/api-order/order/4023/v1/orderConfirm4Store-B端确认订单接口";
        LOGGER.info("{}=--开始--{}", logTitle, JsonUtil.toJSONString(condition));
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            condition.setStoreId(storeUser.getBusinessId());
            this.orderService.orderConfirm4Store(condition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端退款订单处理接口", notes = "B端退款订单处理接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_402201, message = "参数异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_ALREADY_PAID, message = "已完成的订单不允许退款"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中")
    })
    @RequestMapping(value = "/4022/v1/handleOrderRefundByStore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> handleOrderRefundByStore(@RequestBody OrderRefundStoreHandleCondition condition) {
        String logTitle = "=/api-order/order/4022/v1/handleOrderRefundByStore-B端退款订单处理接口=";
        LOGGER.info("{}--开始--{}", logTitle, JsonUtil.toJSONString(condition));
        if (StringUtils.isBlank(condition.getOrderNo()) || null == condition.getAgree()) {
            throw new BusinessException(BusinessCode.CODE_402201, "参数异常");
        }
        ResponseResult<Void> result = new ResponseResult<>();
        StoreUser store = UserContext.getCurrentStoreUser();
        this.orderService.handleOrderRefundByStore(store,condition);
        LOGGER.info("{}--结束", logTitle);
        return result;
    }

    @ApiOperation(value = "C端订单申请退款接口", notes = "C端订单申请退款接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.ORDER_ALREADY_PAID, message = "已完成的订单不允许退款"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中"),
            @ApiResponse(code = BusinessCode.CODE_402102, message = "订单状态不允许退款")
    })
    @RequestMapping(value = "/4021/v1/orderRefundByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderRefundByCustomer(@RequestBody OrderRefundCondition orderRefundCondition) {
        String logTitle = "=/api-order/order/4021/v1/orderRefundByCustomer-C端订单退款接口=";
        LOGGER.info("{}--开始--{}", logTitle, JsonUtil.toJSONString(orderRefundCondition));
        if (StringUtils.isBlank(orderRefundCondition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        ResponseResult<Void> result = new ResponseResult<>();
        CustomerUser customer = UserContext.getCurrentCustomerUser();
        this.orderService.orderRefundByCustomer(customer,orderRefundCondition);
        LOGGER.info("{}--结束", logTitle);
        return result;
    }

    @ApiOperation(value = "C端订单取消接口", notes = "订单取消接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中")
    })
    @RequestMapping(value = "/4020/v1/cancelOrderByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> cancelOrderByCustomer(@RequestBody OrderCancelCondition orderCancelCondition) {
        if (StringUtils.isBlank(orderCancelCondition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        ResponseResult<Void> result = new ResponseResult<>();
        CustomerUser customer = UserContext.getCurrentCustomerUser();
        this.orderService.cancelOrderByCustomer(customer,orderCancelCondition);
        return result;
    }

    @ApiOperation(value = "B端订单取消接口", notes = "订单取消接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_ALREADY_PAID, message = "已完成的订单不允许退款"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中")
    })
    @RequestMapping(value = "/4026/v1/cancelOrderByStore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> cancelOrderByStore(@RequestBody OrderCancelCondition orderCancelCondition) {
        String logTitle = "=/api-order/order/4026/v1/cancelOrderByStore-B端订单拒单接口=";
        LOGGER.info("{}--开始--{}", logTitle, JsonUtil.toJSONString(orderCancelCondition));
        if (StringUtils.isBlank(orderCancelCondition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        ResponseResult<Void> result = new ResponseResult<>();
        StoreUser store = UserContext.getCurrentStoreUser();
        try {
            this.orderService.cancelOrderByStore(store,orderCancelCondition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "--业务异常" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            throw e;
        }
        LOGGER.info("{}--结束", logTitle);
        return result;
    }
}
