package com.winhxd.b2c.admin.module.customer.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerOrderInfoVO;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.system.login.condition.BackStageCustomerInfoCondition;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.feign.promotion.CouponServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chengyy
 * @Description: 小程序用户信息控制器
 * @date 2018/8/4 16:08
 */
@Api(value = "后台小程序用户管理控制器", tags = "后台小程序用户信息管理接口")
@RestController
@RequestMapping(value = "customer/user")
@CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT)
public class CustomerUserController {

    private Logger logger = LoggerFactory.getLogger(CustomerUserController.class);

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private CouponServiceClient couponServiceClient;

    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_LIST)
    @ApiOperation(value = "根据条件查询用户的分页数据信息", notes = "根据条件查询用户的分页数据信息")
    @ApiResponses({
                    @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询用户列表数据失败"),
                    @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")
    })

    @PostMapping(value = "/findCustomerPageInfo")
    public ResponseResult<PagedList<CustomerUserInfoVO>> findCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition) {
        ResponseResult<PagedList<CustomerUserInfoVO>> responseResult = customerServiceClient.queryCustomerPageInfo(condition);
        return responseResult;
    }

    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_LIST)
    @PostMapping(value = "/findAvabileCustomerPageInfo")
    @ApiOperation(value = "根据条件查询有门店绑定关系用户的分页数据信息", notes = "根据条件查询用户的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询用户列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<CustomerUserInfoVO>> findAvabileCustomerPageInfo(@RequestBody BackStageCustomerInfoCondition condition){
        ResponseResult<PagedList<CustomerUserInfoVO>> responseResult = customerServiceClient.findAvabileCustomerPageInfo(condition);
        return responseResult;
    }

    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_EXPORT)
    @ApiOperation(value = "导出根据条件查询用户的分页数据信息", notes = "导出根据条件查询用户的分页数据信息")
    @GetMapping(value = "/customerExport")
    public ResponseEntity<byte[]> customerExport(BackStageCustomerInfoCondition condition) {
        condition.setIsQueryAll(true);
        ResponseResult<PagedList<CustomerUserInfoVO>> result = customerServiceClient.queryCustomerPageInfo(condition);
        List<CustomerUserInfoVO> list = result.getData().getData();
        if (CollectionUtils.isEmpty(list)) {
            logger.error("CustomerUserController ->customerExport导出数据为空");
            return null;
        }
        return ExcelUtils.exp(list, "用户信息");
    }

    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_BLACK)
    @ApiOperation(value = "添加黑名单", notes = "添加黑名单")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "添加黑名单成功"), @ApiResponse(code = BusinessCode.CODE_200001, message = "用户id为空")})
    @PostMapping(value = "/addBlackList")
    public ResponseResult<Void> addBlackList(@RequestBody BackStageCustomerInfoCondition condition) {
        if (condition.getCustomerId() == null) {
            logger.error("CustomerUserController ->updateStatus方法参数customerId为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        condition.setStatus(0);
        return customerServiceClient.updateStatus(condition);
    }
    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_BLACK)
    @ApiOperation(value = "移出黑名单", notes = "移出黑名单")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "移出黑名单成功"), @ApiResponse(code = BusinessCode.CODE_200001, message = "用户id为空")})
    @PostMapping(value = "/removeBlackList")
    public ResponseResult<Void> removeBlackList(@RequestBody BackStageCustomerInfoCondition condition) {
        if (condition.getCustomerId() == null) {
            logger.error("CustomerUserController ->updateStatus方法参数customerId为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        condition.setStatus(1);
        return customerServiceClient.updateStatus(condition);
    }
    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_DETAIL)
    @ApiOperation(value = "根据用户id查询当前用户的信息以及订单信息", notes = "点击用户详情页查询用户已经订单详情列表信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_200001, message = "用户id参数为空*"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/queryCustomerUserInfoDeatil")
    public ResponseResult<CustomerOrderInfoVO> queryCustomerUserInfoDeatil(@RequestParam("customerUserId") Long customerUserId,
                                                                           @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo, @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        ResponseResult<CustomerOrderInfoVO> responseResult = new ResponseResult<>();
        CustomerOrderInfoVO result = new CustomerOrderInfoVO();
        if (customerUserId == null) {
            logger.error("CustomerUserController ->queryCustomerUserInfoDeatil方法参数customerId为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        //查询设置用户信息
        result.setCustomer(queryCustomerById(customerUserId));
        //查询优惠券领取的总次数
        Integer count = Integer.valueOf(couponServiceClient.getCouponNumsByCustomerForStore(customerUserId).getData());
        result.setCouponCount(count == null ? 0 : count);
        //调用Fegin查询订单信息
        populateOrderList(result, customerUserId, pageNo, pageSize);
        responseResult.setData(result);
        return responseResult;
    }

    private void populateOrderList(CustomerOrderInfoVO result, Long customerUserId, Integer pageNo, Integer pageSize) {
        OrderInfoQuery4ManagementCondition condition1 = new OrderInfoQuery4ManagementCondition();
        condition1.setCustomerId(customerUserId);
        condition1.setPageNo(pageNo);
        condition1.setPageSize(pageSize);
        ResponseResult<PagedList<OrderInfoDetailVO>> responseResult1 = orderServiceClient.listOrder4Management(condition1);
        PagedList<OrderInfoDetailVO> pagedList = responseResult1.getData();
        if (pagedList != null) {
            result.setOrderInfoDetailVOList(pagedList.getData());
            result.setOrderCount(pagedList.getTotalRows());
        }
    }

    @CheckPermission(PermissionEnum.CUSTOMER_MANAGEMENT_ORDER_DETAIL)
    @ApiOperation(value = "查询订单详情信息", notes = "根据订单查询订单详情已经订单状态信息")
    @GetMapping(value = "/queryOderInfoDetail/{orderNum}")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"), @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误")})
    public ResponseResult<OrderInfoDetailVO4Management> queryOderInfoDetail(@PathVariable("orderNum") String orderNum) {
        if (StringUtils.isEmpty(orderNum)) {
            logger.error("CustomerUserController ->queryOderInfoDetail方法参数orderNum为空");
            throw new BusinessException(BusinessCode.CODE_402013);
        }
        ResponseResult<OrderInfoDetailVO4Management> responseResult = orderServiceClient.getOrderDetail4Management(orderNum);
        return responseResult;
    }

    /**
     * @param customerUserId 用户id
     * @return 用户信息
     * @author chengyy
     * @date 2018/8/7 9:55
     * @Description 通过id查询用户信息
     */
    public CustomerUserInfoVO queryCustomerById(Long customerUserId) {
        //根据customerId查询用户信息
        BackStageCustomerInfoCondition condition = new BackStageCustomerInfoCondition();
        condition.setCustomerId(customerUserId);
        ResponseResult<PagedList<CustomerUserInfoVO>> pageListResult = customerServiceClient.queryCustomerPageInfo(condition);
        List<CustomerUserInfoVO> customers = pageListResult.getData().getData();
        if (customers != null && customers.size() > 0) {
            return customers.get(0);
        }
        return null;
    }
}
