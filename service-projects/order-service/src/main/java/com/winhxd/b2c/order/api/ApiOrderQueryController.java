package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderListCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoListVO;
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
@Api(tags = "OrderQuery")
@RequestMapping(value = "")
public class ApiOrderQueryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiOrderQueryController.class);

    @Resource
    private OrderQueryService orderQueryService;

    @ApiOperation(value = "订单列表查询接口", response = OrderInfoListVO.class, notes = "订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoListVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api-order/api/order/410/v1/orderListByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderInfoDetailVO>> orderListByCustomer(@RequestBody OrderListCondition orderListCondition) {
        LOGGER.info("=/api-order/api/order/410/v1/orderListByCustomer订单列表查询接口=--开始--{}");
        Long customerId = 1L;
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            //返回对象
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.findOrderByCustomerId(orderListCondition);
            result.setData(list);
        } catch (Exception e) {
            LOGGER.error("=/api-order/api/order/410/v1/orderListByCustomer订单列表查询接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/api/order/410/v1/orderListByCustomer订单列表查询接口=--结束 result={}", result);
        return result;
    }

    @ApiOperation(value = "订单列表查询接口", response = OrderInfoDetailVO.class, notes = "订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api-order/api/order/411/v1/getOrderDetailByOrderNo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderInfoDetailVO> getOrderDetailByOrderNo() {
        LOGGER.info("=/api-order/api/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--开始--{}");
        Long customerId = 1L;
        ResponseResult<OrderInfoDetailVO> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=/api-order/api/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=/api-order/api/order/411/v1/getOrderDetailByOrderNo订单详情查询接口=--结束 result={}", result);
        return result;
    }
}
