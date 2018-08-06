package com.winhxd.b2c.admin.module.customer.controller;

import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerUserInfoCondition1;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author chengyy
 * @Description: 用户信息控制器
 * @date 2018/8/4 16:08
 */
@Api(value = "后台用户管理控制器", tags = "后台用户管理涉及到的接口")
@RestController
@RequestMapping(value = "customer/user")
public class CustomerUserController {

    private Logger logger = LoggerFactory.getLogger(CustomerUserController.class);

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @ApiOperation(value = "根据条件查询用户的分页数据信息", response = ResponseResult.class, notes = "根据条件查询用户的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询用户列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/2002/v1/findCustomerPageInfo")
    public ResponseResult<PagedList<CustomerUserInfoVO1>> findCustomerPageInfo(CustomerUserInfoCondition1 condition) {
        ResponseResult<PagedList<CustomerUserInfoVO1>> responseResult = customerServiceClient.queryCustomerPageInfo(condition);
        return responseResult;
    }

    @ApiOperation(value = "导出根据条件查询用户的分页数据信息", response = ResponseEntity.class, notes = "导出根据条件查询用户的分页数据信息")
    @GetMapping(value = "/2003/v1/customer/export")
    public ResponseEntity<byte[]> customerExport(CustomerUserInfoCondition1 condition) {
        ResponseResult<PagedList<CustomerUserInfoVO1>> result = customerServiceClient.queryCustomerPageInfo(condition);
        List<CustomerUserInfoVO1> list = result.getData().getData();
        if (CollectionUtils.isEmpty(list)) {
            logger.info("导出数据为空");
            return null;
        }
        return ExcelUtils.exp(list, "用户信息");
    }
}
