package com.winhxd.b2c.admin.module.hxd.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.Retail2cCompanyCondition;
import com.winhxd.b2c.common.domain.product.vo.CompanyInfo;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.company.CompanyServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "后台小程序品牌商管理", tags = "后台小程序品牌商管理")
@RequestMapping("/company")
@CheckPermission(PermissionEnum.PROD_MANAGEMENT)
public class CompanyController {
    
    @Autowired
    private CompanyServiceClient companyServiceClient;
    
    @RequestMapping(value = "/getCompanyInfoByPage", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "根据条件查询品牌商信息.分页", notes = "根据条件查询品牌商信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询品牌商信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<CompanyInfo>> getCompanyInfoByPage(@RequestBody Retail2cCompanyCondition condition) {
		return companyServiceClient.getCompanyInfoByPage(condition);
    }
    
    @RequestMapping(value = "/getCompanyInfoByCodes", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "根据品牌商编码查询品牌商信息", notes = "根据品牌商编码查询品牌商信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询品牌商信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<List<CompanyInfo>> getCompanyInfoByCodes(@RequestBody List<String> codes) {
    	return companyServiceClient.getCompanyInfoByCodes(codes);
    }
    
}
