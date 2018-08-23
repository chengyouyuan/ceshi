package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.CustomerSearchProductCondition;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.model.CustomerBrowseLog;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.ProductService;
import com.winhxd.b2c.store.service.StoreBrowseLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *
 * @author caiyulong
 * @date 2018/8/4
 */
@Api(value = "api - 小程序查询门店商品相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api-store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiCustomerProductController {

    private static final String MODULE_NAME = "C端门店商品查询";

    private static final Logger logger = LoggerFactory.getLogger(ApiCustomerProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private StoreBrowseLogService storeBrowseLogService;

    @ApiOperation(value = "[未登录]小程序商品搜索接口", notes = "[未登录]小程序商品搜索接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id")})
    @PostMapping(value = "product/security/2001/v1/searchProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductSkuVO>> searchProductList(@RequestBody CustomerSearchProductCondition condition) {
        logger.info("{} - [未登录]搜索接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("{} - ApiProductController -> searchProductList获取的参数storeId为空", MODULE_NAME);
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<PagedList<ProductSkuVO>> responseResult = productService.searchProductList(condition, null);
        logger.info("{} - [未登录]搜索接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "[已登录]小程序商品搜索接口", notes = "[已登录]小程序商品搜索接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id")})
    @PostMapping(value = "product/2004/v1/searchStoreProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductSkuVO>> searchStoreProductList(@RequestBody CustomerSearchProductCondition condition) {
        CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();
        logger.info("{} - [已登录]搜索接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("{} - ApiProductController -> searchStoreProductList获取的参数storeId为空", MODULE_NAME);
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<PagedList<ProductSkuVO>> responseResult = productService.searchProductList(condition, currentCustomerUser);
        logger.info("{} - [已登录]搜索接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }


    @ApiOperation(value = "[未登录]小程序商品筛选列表初始化接口", notes = "[未登录]小程序商品筛选列表初始化接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id")})
    @PostMapping(value = "product/security/2002/v1/filtrateProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ProductSkuMsgVO> filtrateProductList(@RequestBody CustomerSearchProductCondition condition) {
        logger.info("{} - [未登录]筛选列表初始化接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> filtrateProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<ProductSkuMsgVO> responseResult = productService.filtrateProductList(condition, null);
        logger.info("{} - [未登录]筛选列表初始化接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "[已登录]小程序商品筛选列表初始化接口", notes = "[已登录]小程序商品筛选列表初始化接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id")})
    @PostMapping(value = "product/2005/v1/filtrateStoreProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ProductSkuMsgVO> filtrateStoreProductList(@RequestBody CustomerSearchProductCondition condition) {
        CustomerUser currentCustomerUser = UserContext.getCurrentCustomerUser();
        logger.info("{} - [已登录]筛选列表初始化接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> filtrateStoreProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }

        ResponseResult<ProductSkuMsgVO> responseResult = productService.filtrateProductList(condition, currentCustomerUser);
        logger.info("{} - [已登录]筛选列表初始化接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "店铺热销商品", notes = "店铺热销商品")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id")})
    @PostMapping(value = "product/2003/v1/hotProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductSkuVO>> hotProductList(@RequestBody CustomerSearchProductCondition condition) {
        logger.info("{} - 店铺热销商品接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> hotProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        ResponseResult<PagedList<ProductSkuVO>> responseResult = productService.hotProductList(condition);
        logger.info("{} - 店铺热销商品接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }


}
