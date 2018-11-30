package com.winhxd.b2c.admin.module.order.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderArtificialRefundCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailListVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(value = "后台小程序订单管理", tags = "后台小程序订单管理")
@RequestMapping("/order")
@CheckPermission(PermissionEnum.ORDER_MANAGEMENT)
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @ApiOperation(value = "根据条件查询订单的分页数据信息", notes = "根据条件查询订单的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询订单列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<OrderInfoDetailVO>> list(@RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        return orderServiceClient.listOrder4Management(infoQuery4ManagementCondition);
    }
    
    @RequestMapping(value = "/detail/{orderNo}", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @ApiOperation(value = "根据订单编号orderNo查询订单详情", notes = "根据订单编号orderNo查询订单详情")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询订单列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<OrderInfoDetailVO4Management> detail(@PathVariable(value = "orderNo") String orderNo) {
        return orderServiceClient.getOrderDetail4Management(orderNo);
    }

    @RequestMapping(value = "/artificialRefund/", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @ApiOperation(value = "根据订单编号orderNo查询订单详情", notes = "根据订单编号orderNo查询订单详情")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询订单列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @ResponseBody
    public ResponseResult<Integer> artificialRefund(@RequestBody List<OrderArtificialRefundCondition.OrderList> list) {
        if(CollectionUtils.isEmpty(list)){
            throw new BusinessException(-1, "请至少选择一条记录");
        }
        OrderArtificialRefundCondition condition = new OrderArtificialRefundCondition();
        condition.setList(list);
        return orderServiceClient.artificialRefund(condition);
    }

    @ApiOperation(value = "订单列表导出Excel")
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @RequestMapping("/orderListExport")
    //应返回ResponseEntity<byte[]>暂定Object
    public Object orderListExport(@RequestBody OrderInfoQuery4ManagementCondition condition) {
        ResponseResult<List<OrderInfoDetailVO>> responseResult = orderServiceClient.orderListExport(condition);
        if (responseResult != null && responseResult.getCode() == 0) {
            List<OrderInfoDetailVO> list = responseResult.getData();
            return ExcelUtils.exp(list, "订单列表明细");
        }
        return responseResult;
    }

    @ApiOperation(value = "订单商品详情列表导出Excel")
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @RequestMapping("/orderDetialListExport")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询订单列表数据失败"),
            @ApiResponse(code = BusinessCode.CODE_406301, message = "请先限制条件查询后再导出！"),
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    //应返回ResponseEntity<byte[]>暂定Object
    public Object orderDetialListExport(@RequestBody OrderInfoQuery4ManagementCondition condition) {
        ResponseResult<List<OrderInfoDetailListVO>> responseResult = orderServiceClient.orderDetialListExport(condition);
        if (responseResult != null && responseResult.getCode() == 0) {
            List<OrderInfoDetailListVO> list = responseResult.getData();
            return ExcelUtils.exp(list, "订单商品列表明细");
        }
        return responseResult;
    }



}
