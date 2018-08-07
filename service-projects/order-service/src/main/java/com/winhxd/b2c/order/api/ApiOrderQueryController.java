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
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.OrderQueryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * @author pangjianhua
 * @date 2018/8/3 11:16
 */
@RestController
@Api(tags = "ApiOrderQuery")
@RequestMapping(value = "/api-order/order")
public class ApiOrderQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderQueryController.class);

    @Resource
    private OrderQueryService orderQueryService;

    @ApiOperation(value = "C端订单列表查询接口", response = OrderInfoDetailVO.class, notes = "C端订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_410001, message = "用户不存在")
    })
    @RequestMapping(value = "/410/v1/orderListByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderInfoDetailVO>> orderListByCustomer(@RequestBody AllOrderQueryByCustomerCondition condition) {
        LOGGER.info("=/api-order/order/410/v1/orderListByCustomer-C端订单列表查询接口=--开始--{}");
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.findOrderListByCustomerId(condition);
            result.setData(list);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/410/v1/orderListByCustomer-C端订单列表查询接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/410/v1/orderListByCustomer-C端订单列表查询接口=--结束");
        return result;
    }

    @ApiOperation(value = "C端订单详情查询接口", response = OrderInfoDetailVO.class, notes = "C端订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_411001, message = "参数异常")
    })
    @RequestMapping(value = "/411/v1/getOrderDetailByOrderNo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderInfoDetailVO> getOrderDetailByOrderNo(@RequestBody OrderQueryByCustomerCondition orderQueryByCustomerCondition) {
        String logTitle = "=/api-order/order/411/v1/getOrderDetailByOrderNo-C端订单详情查询接口=";
        LOGGER.info("{}--开始--condition={}", logTitle, orderQueryByCustomerCondition);
        ResponseResult<OrderInfoDetailVO> result = new ResponseResult<>();
        try {
            OrderInfoDetailVO orderVO = this.orderQueryService.findOrderByCustomerId(orderQueryByCustomerCondition);
            result.setData(orderVO);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "--业务异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("{}--结束 result={}", logTitle, result);
        return result;
    }

    @ApiOperation(value = "B端订单列表查询接口", response = OrderInfoDetailVO.class, notes = "B端订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/412/v1/listOrder4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Store(@RequestBody OrderQuery4StoreCondition condition) {
        String logTitle = "/api-order/order/410/v1/listOrder4Store-B端订单列表查询接口";
        LOGGER.info("{}=--开始--{}", logTitle, condition);
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            Long storeId = 0L;
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.listOrder4Store(condition, storeId);
            result.setData(list);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            if (BusinessCode.STORE_ID_EMPTY == e.getErrorCode()) {
                result.setCode(BusinessCode.CODE_1002);
            }
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("{}=--结束", logTitle);
        return result;
    }
}
