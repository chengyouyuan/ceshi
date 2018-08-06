package com.winhxd.b2c.order.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderCreateCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.exception.OrderExcepton;
import com.winhxd.b2c.common.exception.OrderExceptonCodes;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.service.impl.OrderQueryServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(tags = "OrderService")
public class OrderServiceController implements OrderServiceClient {
    
    @Autowired
    private OrderQueryService orderQueryService;

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);
    
    @Override
    public ResponseResult<String> submitOrder(OrderCreateCondition orderCreateCondition) {
        throw new UnsupportedOperationException("订单创建不支持client调用");
    }

    @Override
    public ResponseResult<OrderInfo> getOrderVo(String orderNo) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @ApiOperation(value = "门店销售数据查询接口", response = StoreOrderSalesSummaryVO.class, notes = "门店销售数据查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = StoreOrderSalesSummaryVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummary() {
        String logTitle = "/order/452/v1/queryStoreOrderSalesSummary/";
        logger.info("{} 门店销售数据接口查询开始", logTitle);
        ResponseResult<StoreOrderSalesSummaryVO> result = new ResponseResult<>();
        try {
            //TODO 获取门店id
            long storeId = 0L;
            //查询当天数据
            //获取当天最后一秒
            long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59)).getTime();
            //获取当天开始第一秒
            long startSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0)).getTime();
            Date startDateTime = new Date(startSecond);
            Date endDateTime = new Date(lastSecond);
            result.setData(orderQueryService.getStoreOrderSalesSummary(storeId, startDateTime, endDateTime));
        } catch (Exception e) {
            logger.error(logTitle + " 门店销售数据接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("{} 门店销售数据接口查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "后台订单列表查询接口", response = PagedList.class, notes = "后台订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Management(
            @RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        String logTitle = "/order/453/v1/listOrder4Management/";
        logger.info("{} 后台订单列表查询开始", logTitle);
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            result.setData(orderQueryService.listOrder4Management(infoQuery4ManagementCondition));
        } catch (Exception e) {
            logger.error(logTitle + " 后台订单列表接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("{} 后台订单列表查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "后台订单详情查询接口", response = OrderInfoDetailVO4Management.class, notes = "后台订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = OrderInfoDetailVO4Management.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<OrderInfoDetailVO4Management> getOrderDetail4Management(@PathVariable(value = "orderNo") String orderNo) {
        String logTitle = "/order/453/v1/listOrder4Management/ 后台订单详情接口 ";
        logger.info("{}查询开始", logTitle);
        ResponseResult<OrderInfoDetailVO4Management> result = new ResponseResult<>();
        try {
            if (StringUtils.isBlank(orderNo)) {
                throw new OrderExcepton(OrderExceptonCodes.ORDER_NO_EMPTY);
            }
            result.setData(orderQueryService.getOrderDetail4Management(orderNo));
        } catch (OrderExcepton e) {
            logger.error(logTitle + "查询=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
        } catch (Exception e) {
            logger.error(logTitle + " 查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("{} 查询结束", logTitle);
        return result;
    }
}
