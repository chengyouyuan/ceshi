package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCancelCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderRefundStoreHandleCondition;
import com.winhxd.b2c.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "B端退款订单处理接口", response = Boolean.class, notes = "B端退款订单处理接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/422/v1/handleOrderRefundByStore", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> handleOrderRefundByStore(@RequestBody OrderRefundStoreHandleCondition condition) {
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-B端退款订单处理接口=--开始--{}", condition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {

            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/421/v1/orderRefundByCustomer-B端退款订单处理接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-B端退款订单处理接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "C端订单退款接口", response = Boolean.class, notes = "C端订单退款接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/421/v1/orderCancel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> orderRefundByCustomer(@RequestBody OrderRefundCondition orderRefundCondition) {
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--开始--{}", orderRefundCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            //优惠券一并退回
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/421/v1/orderRefundByCustomer-C端订单退款接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "订单取消接口", response = Boolean.class, notes = "订单取消接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = Boolean.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @RequestMapping(value = "/420/v1/cancelOrder", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Boolean> cancelOrder(@RequestBody OrderCancelCondition orderCancelCondition) {
        LOGGER.info("=/api-order/order/420/v1/cancelOrder-订单取消接口=--开始--{}", orderCancelCondition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            boolean modifyResult = this.orderService.cancelOrder(orderCancelCondition);
            result.setData(modifyResult);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/420/v1/cancelOrder-订单取消接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/420/v1/cancelOrder-订单取消接口=--结束 result={}", result);
        return result;
    }
}
