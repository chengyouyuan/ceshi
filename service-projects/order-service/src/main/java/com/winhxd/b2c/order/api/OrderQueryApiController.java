package com.winhxd.b2c.order.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author pangjianhua
 * @date 2018/8/3 11:16
 */
@RestController
@Api(tags = "OrderQuery")
@RequestMapping(value = "")
public class OrderQueryApiController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderQueryApiController.class);

    @ApiOperation(value = "订单列表查询接口", response = OrderListVO.class, notes = "订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderListVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/api/order/410/v1/orderListByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderListVO> orderListByCustomer() {
        LOGGER.info("=2220抽奖接口=--开始--{}");
        Long customerId = 1L;
        ResponseResult<OrderListVO> result = new ResponseResult<>();
        try {
            //返回对象
            result.setData(null);
        } catch (Exception e) {
            LOGGER.error("=2220抽奖接口=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        LOGGER.info("=2220抽奖接口=--结束 result={}", result);
        return result;
    }


}
