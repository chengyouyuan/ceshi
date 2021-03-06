package com.winhxd.b2c.order.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.*;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailListVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.service.OrderService;
import com.winhxd.b2c.order.service.impl.OrderQueryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "OrderService")
public class OrderServiceController implements OrderServiceClient {

    @Autowired
    private OrderQueryService orderQueryService;
    @Resource
    private OrderService orderService;


    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    @Override
    public ResponseResult<String> submitOrder(OrderCreateCondition orderCreateCondition) {
        throw new UnsupportedOperationException("订单创建不支持client调用");
    }

    @Override
    @ApiOperation(value = "门店当天销售数据查询接口", notes = "门店当天销售数据查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummary() {
        String logTitle = "/order/4052/v1/queryStoreOrderSalesSummary/";
        logger.info("{} 门店当天销售数据接口查询开始", logTitle);
        ResponseResult<StoreOrderSalesSummaryVO> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            //查询当天数据
            result.setData(orderQueryService.getStoreIntradayOrderSalesSummary(storeUser.getBusinessId()));
        } catch (BusinessException e) {
            logger.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            logger.error(logTitle + " 门店当天销售数据接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 门店当天销售数据接口查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "后台订单列表查询接口", notes = "后台订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Management(
            @RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        String logTitle = "/order/4053/v1/listOrder4Management/";
        logger.info("{} 后台订单列表查询开始", logTitle);
        logger.info("{} 后台订单列表参数", infoQuery4ManagementCondition);
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            result.setData(orderQueryService.listOrder4Management(infoQuery4ManagementCondition));
        } catch (Exception e) {
            logger.error(logTitle + " 后台订单列表接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 后台订单列表查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "后台订单详情查询接口", notes = "后台订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<OrderInfoDetailVO4Management> getOrderDetail4Management(@PathVariable(value = "orderNo") String orderNo) {
        String logTitle = "/order/4054/v1/getOrderDetail4Management/ 后台订单详情接口 ";
        logger.info("{}查询开始", logTitle);
        ResponseResult<OrderInfoDetailVO4Management> result = new ResponseResult<>();
        try {
            if (StringUtils.isBlank(orderNo)) {
                throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
            }
            result.setData(orderQueryService.getOrderDetail4Management(orderNo));
        } catch (BusinessException e) {
            logger.error(logTitle + "查询=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            logger.error(logTitle + " 查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "门店销售数据查询接口", notes = "门店销售数据查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<StoreOrderSalesSummaryVO> queryStoreOrderSalesSummaryByDateTimePeriod(@RequestBody StoreOrderSalesSummaryCondition storeOrderSalesSummaryCondition) {
        String logTitle = "/order/4055/v1/queryStoreOrderSalesSummaryByDateTimePeriod/ 门店销售数据接口查询";
        logger.info("{} 开始:confition:{}", logTitle, storeOrderSalesSummaryCondition);
        ResponseResult<StoreOrderSalesSummaryVO> result = new ResponseResult<>();
        try {
            if (storeOrderSalesSummaryCondition == null) {
                throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
            }
            if (storeOrderSalesSummaryCondition.getQueryPeriodType() == null || storeOrderSalesSummaryCondition.getQueryPeriodType() == StoreOrderSalesSummaryCondition
                    .INTRADAY_ORDER_SALES_QUERY_TYPE) {
                result.setData(orderQueryService.getStoreIntradayOrderSalesSummary(storeOrderSalesSummaryCondition.getStoreId()));
            } else if (storeOrderSalesSummaryCondition.getQueryPeriodType() == StoreOrderSalesSummaryCondition.TIME_PERIOD_ORDER_SALES_QUERY_TYPE) {
                result.setData(orderQueryService.calculateStoreOrderSalesSummary(storeOrderSalesSummaryCondition.getStoreId(), storeOrderSalesSummaryCondition.getStartDateTime(),
                        storeOrderSalesSummaryCondition.getEndDateTime()));
            } else {
                result.setData(orderQueryService.getStoreMonthOrderSalesSummary(storeOrderSalesSummaryCondition.getStoreId()));
            }
        } catch (BusinessException e) {
            logger.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            logger.error(logTitle + " =--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 结束:confition:{};ret={}", logTitle, storeOrderSalesSummaryCondition, result.getData());
        return result;
    }

    @Override
    @ApiOperation(value = "订单列表查询", notes = "订单列表查询")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<List<OrderInfoDetailVO>> listOrderWithNoPage(
            OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        String logTitle = "/order/4056/v1/listOrderWithNoPage/";
        logger.info("{} 后台订单列表查询开始", logTitle);
        ResponseResult<List<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            result.setData(orderQueryService.listOrder4ManagementWithNoPage(infoQuery4ManagementCondition));
        } catch (Exception e) {
            logger.error(logTitle + " 后台订单列表接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 后台订单列表查询结束", logTitle);
        return result;
    }

    /**
     * 申请退款回调（设置订单状态为退款中）
     *
     * @param orderRefundCallbackCondition 入参
     * @return 是否成功
     */
    @Override
    public ResponseResult<Boolean> updateOrderRefundCallback(@RequestBody OrderRefundCallbackCondition orderRefundCallbackCondition) {
        String logTitle = "/order/4057/v1/updateOrderRefundCallback/";
        logger.info("{} 后台订单列表查询开始", logTitle);
        if (StringUtils.isBlank(orderRefundCallbackCondition.getOrderNo())) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        ResponseResult<Boolean> result = new ResponseResult<>();
        try {
            result.setData(orderService.updateOrderRefundCallback(orderRefundCallbackCondition));
        } catch (Exception e) {
            logger.error(logTitle + " 后台订单列表接口查询=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        logger.info("{} 后台订单列表查询结束", logTitle);
        return result;
    }

    @Override
    @ApiOperation(value = "订单支付成功回调", notes = "订单支付成功回调")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    public ResponseResult<Void> orderPaySuccessNotify(@PathVariable(value = "orderNo") String orderNo, @PathVariable(value = "paymentSerialNum") String paymentSerialNum) {
        String logTitle = "/order/4060/v1/orderPaySuccessNotify/";
        logger.info("{} 后台订单支付成功通知回调开始:orderNo={},paymentSerialNum={}", logTitle, orderNo, paymentSerialNum);
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单支付通知orderNo不能为空");
        }
        if (StringUtils.isBlank(paymentSerialNum)) {
            throw new NullPointerException("订单支付流水号paymentSerialNum不能为空");
        }
        orderService.orderPaySuccessNotify(orderNo, paymentSerialNum);
        logger.info("{} 后台订单支付成功通知回调结束:orderNo={},paymentSerialNum={}", logTitle, orderNo, paymentSerialNum);
        return new ResponseResult<>();
    }

    /**
     * 退款失败状态更新
     *
     * @param condition 入参
     * @return 是否成功
     */
    @Override
    public ResponseResult<Boolean> updateOrderRefundFail(@RequestBody OrderRefundFailCondition condition) {
        String logTitle = "/order/4061/v1/orderPaySuccessNotify/";
        logger.info("{} 退款失败状态更新开始:condition={}", logTitle, condition);
        ResponseResult<Boolean> result = new ResponseResult<>();
        boolean updateResult = orderService.updateOrderRefundFailStatus(condition);
        result.setData(updateResult);
        logger.info("{} 退款失败状态更新结束:orderNo={},result={}", logTitle, updateResult);
        return result;
    }

    /**
     * 手工退款
     *
     * @param condition
     * @return
     */
    @Override
    public ResponseResult<Integer> artificialRefund(@RequestBody OrderArtificialRefundCondition condition) {
        String logTitle = "/order/4062/v1/orderPaySuccessNotify/";
        logger.info("{} 手工退款开始:orderNo={}", logTitle, condition);
        ResponseResult<Integer> result = new ResponseResult<>();
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        int updateResult = orderService.artificialRefund(adminUser,condition);
        result.setData(updateResult);
        logger.info("{} 手工退款结束:orderNo={},result={}", logTitle, updateResult);
        return result;
    }

    /**
     * @Author: zhoufenglong
     * @Description: 订单列表导出 EXCEL
     * @param: [condition]
     * @return： com.winhxd.b2c.common.domain.ResponseResult<java.util.List<com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO>>
     * @Date: 2018/11/29 16:51
     */
    @Override
    public ResponseResult<List<OrderInfoDetailVO>> orderListExport(@RequestBody OrderInfoQuery4ManagementCondition condition) {
        String logTitle = "/order/4063/v1/orderListExport/";
        logger.info("{} 订单列表导出 EXCEL开始,入参 {}", logTitle,condition);
        ResponseResult<List<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            List<OrderInfoDetailVO> list = orderQueryService.orderListExport(condition);
            result.setData(list);
        } catch (BusinessException e) {
            result.setCode(e.getErrorCode());
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(logTitle + " 订单列表导出 EXCEL=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("{} 订单列表导出 EXCEL结束", logTitle);
        return result;
    }

    /**
     * @Author: zhoufenglong
     * @Description: 订单商品详情列表导出 EXCEL
     * @param: [condition]
     * @return： com.winhxd.b2c.common.domain.ResponseResult<java.util.List<com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailListVO>>
     * @Date: 2018/11/29 18:10
     */
    @Override
    public ResponseResult<List<OrderInfoDetailListVO>> orderDetialListExport(@RequestBody OrderInfoQuery4ManagementCondition condition) {
        String logTitle = "/order/4064/v1/orderDetialListExport/";
        logger.info("{} 订单商品详情列表导出 EXCEL开始,入参 {}", logTitle,condition);
        ResponseResult<List<OrderInfoDetailListVO>> result = new ResponseResult<>();
        try {
            List<OrderInfoDetailListVO> list = orderQueryService.orderDetialListExport(condition);
            result.setData(list);
        }catch (BusinessException e) {
            result.setCode(e.getErrorCode());
            result.setMessage(e.getMessage());
        } catch (Exception e) {
            logger.error(logTitle + " 订单商品详情列表导出 EXCEL=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
        }
        logger.info("{} 订单商品详情列表导出 EXCEL结束", logTitle);
        return result;
    }
}
