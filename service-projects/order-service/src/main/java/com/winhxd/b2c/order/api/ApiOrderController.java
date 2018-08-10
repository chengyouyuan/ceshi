package com.winhxd.b2c.order.api;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderConfirmCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPickupCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundStoreHandleCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.OrderService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

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

    @ApiOperation(value = "B端接单计价", notes = "B端接单计价")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_PICKUP_CODE, message = "提货码错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
    })
    @RequestMapping(value = "/424/v1/orderConfirm4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderPickup4Store(@RequestBody OrderPickupCondition condition) {
        String logTitle = "/api-order/order/424/v1/orderPickup4Store-B端订单提货接口";
        LOGGER.info("{}=--开始--{}", logTitle, condition);
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null || storeUser.getBusinessId() == null) {
                throw new BusinessException(BusinessCode.CODE_1002);
            }
            condition.setStoreId(storeUser.getBusinessId());
            this.orderService.orderPickup4Store(condition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端接单计价",notes = "B端接单计价")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_TOTAL_MONEY, message = "订单金额错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
    })
    @RequestMapping(value = "/423/v1/orderConfirm4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> orderConfirm4Store(@RequestBody OrderConfirmCondition condition) {
        String logTitle = "/api-order/order/423/v1/orderConfirm4Store-B端确认订单接口";
        LOGGER.info("{}=--开始--{}", logTitle, condition);
        ResponseResult<Void> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null || storeUser.getBusinessId() == null) {
                throw new BusinessException(BusinessCode.CODE_1002);
            }
            condition.setStoreId(storeUser.getBusinessId());
            this.orderService.orderConfirm4Store(condition);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端退款订单处理接口", notes = "B端退款订单处理接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_422001, message = "参数异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_ALREADY_PAID, message = "已完成的订单不允许退款"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中")
    })
    @RequestMapping(value = "/422/v1/handleOrderRefundByStore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> handleOrderRefundByStore(@RequestBody OrderRefundStoreHandleCondition condition) {
        LOGGER.info("=/api-order/order/422/v1/orderRefundByCustomer-B端退款订单处理接口=--开始--{}", condition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            this.orderService.handleOrderRefundByStore(condition);
            result.setData(true);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/422/v1/orderRefundByCustomer-B端退款订单处理接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/422/v1/orderRefundByCustomer-B端退款订单处理接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "C端订单申请退款接口", response = Boolean.class, notes = "C端订单申请退款接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.ORDER_ALREADY_PAID, message = "已完成的订单不允许退款"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中"),
            @ApiResponse(code = BusinessCode.CODE_421002, message = "订单状态不允许退款")
    })
    @RequestMapping(value = "/421/v1/orderCancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> orderRefundByCustomer(@RequestBody OrderRefundCondition orderRefundCondition) {
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--开始--{}", orderRefundCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            this.orderService.orderRefundByCustomer(orderRefundCondition);
            //优惠券一并退回
            result.setData(true);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--异常-" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "C端订单取消接口", response = Boolean.class, notes = "订单取消接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_STORE_ID, message = "门店不存在"),
            @ApiResponse(code = BusinessCode.ORDER_DOES_NOT_EXIST, message = "订单不存在"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_STATUS_CHANGE_FAILURE, message = "订单状态修改失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_MODIFIED, message = "订单修改中")
    })
    @RequestMapping(value = "/420/v1/cancelOrderByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> cancelOrderByCustomer(@RequestBody OrderCancelCondition orderCancelCondition) {
        LOGGER.info("=/api-order/order/420/v1/cancelOrderByCustomer-订单取消接口=--开始--{}", orderCancelCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            this.orderService.cancelOrderByCustomer(orderCancelCondition);
            result.setData(true);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/420/v1/cancelOrderByCustomer-订单取消接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/420/v1/cancelOrderByCustomer-订单取消接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "B端订单取消接口", response = Boolean.class, notes = "订单取消接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
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
    @RequestMapping(value = "/425/v1/cancelOrderByStore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> cancelOrderByStore(@RequestBody OrderCancelCondition orderCancelCondition) {
        String logTitle = "=/api-order/order/425/v1/cancelOrderByStore-B端订单拒单接口=";
        LOGGER.info("{}--开始--{}", logTitle, orderCancelCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            this.orderService.cancelOrderByStore(orderCancelCondition);
            result.setData(true);
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("{}--结束 result={}", logTitle, result);
        return result;
    }
}
