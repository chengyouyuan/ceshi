package com.winhxd.b2c.admin.module.product.controller;

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
import com.winhxd.b2c.common.domain.product.condition.BrandCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.product.vo.ProductVO;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value = "后台小程序商品管理", tags = "后台小程序商品管理")
@RequestMapping("/product")
@CheckPermission(PermissionEnum.PROD_MANAGEMENT)
public class ProductController {
    
    @Autowired
    private ProductServiceClient productServiceClient;
    
    @RequestMapping(value = "/getProductMsg", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "获取分类、 品牌、 商品spu信息.分页", notes = "获取分类、 品牌、 商品spu信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<ProductMsgVO> getProductMsg(@RequestBody ProductConditionByPage condition) {
		return productServiceClient.getProductMsg(condition);
    }
	
    @RequestMapping(value = "/getProductSkuMsg", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "获取一级分类、二级分类、商品sku信息.分页", notes = "获取一级分类、二级分类、商品sku信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<ProductSkuMsgVO> getProductSkuMsg(@RequestBody ProductConditionByPage condition) {
    	return productServiceClient.getProductSkuMsg(condition);
    }
    
    @RequestMapping(value = "/getProductsByPage", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "获取商品spu信息.分页", notes = "获取商品spu信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<ProductVO>> getProductsByPage(@RequestBody ProductConditionByPage condition) {
    	return productServiceClient.getProductsByPage(condition);
    }
    
    @RequestMapping(value = "/getProductSkusByPage", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "获取商品sku信息.分页", notes = "获取商品sku信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<ProductSkuVO>> getProductSkusByPage(@RequestBody ProductConditionByPage condition) {
    	return productServiceClient.getProductSkusByPage(condition);
    }
    
    @RequestMapping(value = "/getProductSkus", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "获取商品sku信息", notes = "获取商品sku信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<List<ProductSkuVO>> getProductSkus(@RequestBody ProductConditionByPage condition) {
    	return productServiceClient.getProductSkus(condition);
    }
    
    @RequestMapping(value = "/getBrandInfo", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "根据brandCode获取品牌信息", notes = "根据brandCode获取品牌信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<List<BrandVO>> getBrandInfo(@RequestBody List<String> brandCodes) {
    	return productServiceClient.getBrandInfo(brandCodes);
    }
    
    @RequestMapping(value = "/getBrandInfoByPage", method = RequestMethod.POST)
    @CheckPermission(PermissionEnum.PROD_MANAGEMENT)
    @ApiOperation(value = "根据条件获取品牌信息.分页", notes = "根据条件获取品牌信息.分页")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询商品信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<BrandVO>> getBrandInfoByPage(@RequestBody BrandCondition condition) {
    	return productServiceClient.getBrandInfoByPage(condition);
    }
    
}
