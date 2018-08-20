package com.winhxd.b2c.order.api;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderPayInfoCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByStoreCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderCountByStatus4StoreVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderItemVO;
import com.winhxd.b2c.common.domain.order.vo.OrderListForCustomerVO;
import com.winhxd.b2c.common.domain.order.vo.OrderListItemForCustomerVO;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.domain.pay.vo.PayPreOrderVO;
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

    @ApiOperation(value = "C端订单列表查询接口", notes = "C端订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_4010001, message = "用户不存在")
    })
    @RequestMapping(value = "/4010/v1/orderListByCustomer", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderListForCustomerVO>> orderListByCustomer(@RequestBody AllOrderQueryByCustomerCondition condition) {
        String logTitle = "=/api-order/order/4010/v1/orderListByCustomer-C端订单列表查询接口=";
        LOGGER.info(logTitle + "--开始--{}", condition);
        ResponseResult<PagedList<OrderListForCustomerVO>> result = new ResponseResult<>();
        try {
            PagedList<OrderListForCustomerVO> data = new PagedList<>();
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.findOrderListByCustomerId(condition);
            List<OrderInfoDetailVO> orderInfoDetailVOS = list.getData();
            List<OrderListForCustomerVO> orderListForCustomerVOList = new ArrayList<>();
            for (OrderInfoDetailVO orderInfoDetailVO : orderInfoDetailVOS) {
                OrderListForCustomerVO orderListForCustomerVO = new OrderListForCustomerVO();
                List<OrderItemVO> orderItemVoList = orderInfoDetailVO.getOrderItemVoList();
                List<OrderListItemForCustomerVO> orderListItemForCustomerVOList = new ArrayList<>();
                for (OrderItemVO orderItemVO : orderItemVoList) {
                    OrderListItemForCustomerVO orderListItemForCustomerVO = new OrderListItemForCustomerVO();
                    BeanUtils.copyProperties(orderItemVO, orderListItemForCustomerVO);
                    orderListItemForCustomerVOList.add(orderListItemForCustomerVO);
                }
                BeanUtils.copyProperties(orderInfoDetailVO, orderListForCustomerVO);
                orderListForCustomerVO.setOrderItemVoList(orderListItemForCustomerVOList);
                orderListForCustomerVOList.add(orderListForCustomerVO);
            }
            data.setTotalRows(list.getTotalRows());
            data.setPageNo(list.getPageNo());
            data.setPageSize(list.getPageSize());
            data.setData(orderListForCustomerVOList);
            result.setData(data);
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info(logTitle + "--结束");
        return result;
    }

    @ApiOperation(value = "C端订单详情查询接口", notes = "C端订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_4011001, message = "参数异常")
    })
    @RequestMapping(value = "/4011/v1/getOrderDetailByOrderNo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderInfoDetailVO> getOrderDetailByOrderNo(@RequestBody OrderQueryByCustomerCondition orderQueryByCustomerCondition) {
        String logTitle = "=/api-order/order/4011/v1/getOrderDetailByOrderNo-C端订单详情查询接口=";
        LOGGER.info("{}--开始--condition={}", logTitle, orderQueryByCustomerCondition);
        ResponseResult<OrderInfoDetailVO> result = new ResponseResult<>();
        try {
            OrderInfoDetailVO orderVO = this.orderQueryService.findOrderByCustomerId(orderQueryByCustomerCondition);
            result.setData(orderVO);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "--业务异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}--结束 result={}", result);
        return result;
    }


    @ApiOperation(value = "B端订单详情查询接口", notes = "B端订单详情查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_4011001, message = "参数异常")
    })
    @RequestMapping(value = "/4014/v1/getOrderDetailForStoreByOrderNo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderInfoDetailVO> getOrderDetailForStoreByOrderNo(@RequestBody OrderQueryByStoreCondition condition) {
        String logTitle = "=/api-order/order/4014/v1/getOrderDetailForStoreByOrderNo-B端订单详情查询接口=";
        LOGGER.info("{}--开始--condition={}", logTitle, condition);
        ResponseResult<OrderInfoDetailVO> result = new ResponseResult<>();
        try {
            OrderInfoDetailVO orderVO = this.orderQueryService.findOrderForStore(condition);
            result.setData(orderVO);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "--业务异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}--结束", logTitle);
        return result;
    }

    @ApiOperation(value = "B端订单列表查询接口", notes = "B端订单列表查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/4012/v1/listOrder4Store", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<OrderInfoDetailVO>> listOrder4Store(@RequestBody OrderQuery4StoreCondition condition) {
        String logTitle = "/api-order/order/4012/v1/listOrder4Store-B端订单列表查询接口";
        LOGGER.info("{}=--开始--{}", logTitle, condition);
        ResponseResult<PagedList<OrderInfoDetailVO>> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null || storeUser.getBusinessId() == null) {
                throw new BusinessException(BusinessCode.CODE_1002);
            }
            PagedList<OrderInfoDetailVO> list = this.orderQueryService.listOrder4Store(condition, storeUser.getBusinessId());
            result.setData(list);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            if (BusinessCode.STORE_ID_EMPTY == e.getErrorCode()) {
                result.setCode(BusinessCode.CODE_1002);
            }
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}=--结束", logTitle);
        return result;
    }

    @ApiOperation(value = "B端订单各状态数量查询接口", notes = "B端订单各状态数量查询接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效")
    })
    @RequestMapping(value = "/4013/v1/getOrderCountByStatus", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderCountByStatus4StoreVO> getOrderCountByStatus(@RequestBody ApiCondition apiCondition) {
        String logTitle = "/api-order/order/4013/v1/getOrderCountByStatus-B端订单各状态数量查询接口";
        ResponseResult<OrderCountByStatus4StoreVO> result = new ResponseResult<>();
        try {
            //获取当前登录门店Id
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null || storeUser.getBusinessId() == null) {
                throw new BusinessException(BusinessCode.CODE_1002);
            }
            LOGGER.info("{}=--开始--storeId={}", logTitle, storeUser.getBusinessId());
            OrderCountByStatus4StoreVO orderCountByStatus4StoreVO = this.orderQueryService.getOrderCountByStatus(storeUser.getBusinessId());
            result.setData(orderCountByStatus4StoreVO);
        } catch (BusinessException e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(e.getErrorCode());
            if (BusinessCode.STORE_ID_EMPTY == e.getErrorCode()) {
                result.setCode(BusinessCode.CODE_1002);
            }
            throw e;
        } catch (Exception e) {
            LOGGER.error(logTitle + "=--异常" + e.getMessage(), e);
            result.setCode(BusinessCode.CODE_1001);
            throw e;
        }
        LOGGER.info("{}=--结束", logTitle);
        return result;
    }
    

    @ApiOperation(value = "C端获取支付信息", notes = "C端获取支付信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.ORDER_NO_EMPTY, message = "订单号为空"),
            @ApiResponse(code = BusinessCode.WRONG_ORDERNO, message = "订单号错误"),
            @ApiResponse(code = BusinessCode.WRONG_ORDER_STATUS, message = "订单状态错误"),
            @ApiResponse(code = BusinessCode.ORDER_GET_PAY_INFO_ERROR, message = "订单获取支付信息失败"),
            @ApiResponse(code = BusinessCode.ORDER_IS_BEING_PAID, message = "订单已经支付")
    })
    @RequestMapping(value = "/4015/v1/getOrderPayInfo", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<OrderPayVO> getOrderPayInfo(@RequestBody OrderPayInfoCondition condition) {
        String logTitle = "/api-order/order/4015/v1/getOrderPayInfo-C端获取支付信息";
        LOGGER.info("{}=--开始--{}", logTitle, condition);
        ResponseResult<OrderPayVO> result = new ResponseResult<>();
        //获取当前登录门店Id
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null || customerUser.getCustomerId() == null || StringUtils.isBlank(customerUser.getOpenid())) {
            throw new BusinessException(BusinessCode.CODE_1002);
        }
        result.setData(orderQueryService.getOrderPayInfo(condition.getOrderNo(), condition.getSpbillCreateIp(), condition.getDeviceInfo(), customerUser.getCustomerId(), customerUser.getOpenid()));
        LOGGER.info("{}=--结束 result={}", logTitle, result);
        return result;
    }
    
}
