package com.winhxd.b2c.admin.module.order.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "后台小程序订单管理", tags = "后台小程序订单管理")
@RequestMapping("/order")
@CheckPermission(PermissionEnum.SYSTEM_MANAGEMENT)
public class OrderController {
    
    @Autowired
    private OrderServiceClient orderServiceClient;
    
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.ORDER_MANAGEMENT_LIST)
    @MenuAssign(MenuEnum.ORDER_MANAGEMENT_LIST)
    @ApiOperation(value = "根据条件查询订单的分页数据信息", notes = "根据条件查询订单的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询订单列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<OrderInfoDetailVO>> list(@RequestBody OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        return orderServiceClient.listOrder4Management(infoQuery4ManagementCondition);
    }
}
