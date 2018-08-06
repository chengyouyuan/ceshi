package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.order.service.OrderQueryService;
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
@Api(tags = "ApiOrderQuery")
@RequestMapping(value = "/api-order/order")
public class ApiOrderQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderQueryController.class);

    @Resource
    private OrderQueryService orderQueryService;

    @ApiOperation(value = "订单列表查询接口", response = OrderInfoDetailVO.class, notes = "订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/410/v1/orderListByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderInfoDetailVO>> orderListByCustomer(@RequestBody AllOrderQueryByCustomerCondition condition) {
        LOGGER.info("=/api-order/order/410/v1/orderListByCustomer订单列表查询接口=--开始--{}");
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            //返回对象
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.findOrderListByCustomerId(condition);
            for (int i = 0; i < 10; i++) {
                String s = this.orderQueryService.getPickUpCode(1);
                LOGGER.info("取货码：{}", s);
            }
            result.setData(list);
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/410/v1/orderListByCustomer订单列表查询接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/410/v1/orderListByCustomer订单列表查询接口=--结束");
        return result;
    }

    @ApiOperation(value = "订单详情查询接口", response = OrderInfoDetailVO.class, notes = "订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_411001, message = "参数异常")
    })
    @RequestMapping(value = "/411/v1/getOrderDetailByOrderNo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderInfoDetailVO> getOrderDetailByOrderNo(@RequestBody OrderQueryByCustomerCondition orderQueryByCustomerCondition) {
        LOGGER.info("=/api-order/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--开始--{}");
        Long customerId = 1L;
        ResponseResult<OrderInfoDetailVO> result = new ResponseResult<>();
        try {
            OrderInfoDetailVO orderVO = this.orderQueryService.findOrderByCustomerId(orderQueryByCustomerCondition);
            //返回对象
            result.setData(orderVO);
        } catch (BusinessException e) {
            LOGGER.error("=/api-order/order/410/v1/orderListByCustomer订单列表查询接口=--业务异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            LOGGER.error("=/api-order/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--结束 result={}", result);
        return result;
    }
}
