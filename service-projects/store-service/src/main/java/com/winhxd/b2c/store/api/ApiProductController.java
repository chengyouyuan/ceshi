package com.winhxd.b2c.store.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.CustomerSearchProductCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.store.service.StoreProductManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author caiyulong
 * @date 2018/8/4
 */
@Api(value = "api - 小程序查询门店商品相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api-store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiProductController {

    private static final String MODULE_NAME = "C端门店商品查询";

    private static final Logger logger = LoggerFactory.getLogger(ApiProductController.class);

    @Autowired
    private StoreProductManageService storeProductManageService;

    @Autowired
    private ProductServiceClient productServiceClient;

    @ApiOperation(value = "小程序商品搜索接口", response = ResponseResult.class, notes = "小程序商品搜索接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id", response = ResponseResult.class)})
    @PostMapping(value = "product/security/2001/v1/searchProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductSkuVO>> searchProductList(@RequestBody CustomerSearchProductCondition condition) {
        ResponseResult<PagedList<ProductSkuVO>> responseResult = new ResponseResult<>();
        logger.info("{} - 搜索接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> searchProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }

        //获取门店下的商品
        StoreProductManageCondition storeProductManageCondition = new StoreProductManageCondition();
        storeProductManageCondition.setStoreId(condition.getStoreId());
        storeProductManageCondition.setProdStatus(Arrays.asList(StoreProductStatusEnum.PUTAWAY.getStatusCode()));
        storeProductManageCondition.setRecommend(condition.getRecommend());
        storeProductManageCondition.setOrderBy(condition.getProductSortType());
        storeProductManageCondition.setDescAsc(condition.getProductSortType() != null && condition.getProductSortType().equals(1)
                ? (byte)0 : (byte)1);
        List<String> skusByConditon = storeProductManageService.findSkusByConditon(storeProductManageCondition);

        //获取商品列表信息
        ProductConditionByPage productConditionByPage = new ProductConditionByPage();
        BeanUtils.copyProperties(condition,productConditionByPage);
        productConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        productConditionByPage.setProductSkus(skusByConditon);
        responseResult = productServiceClient.getProductSkusByPage(productConditionByPage);
        logger.info("{} - 搜索接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }


    @ApiOperation(value = "小程序商品筛选列表初始化接口", response = ResponseResult.class, notes = "小程序商品筛选列表初始化接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id", response = ResponseResult.class)})
    @PostMapping(value = "product/security/2002/v1/filtrateProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ProductSkuMsgVO> filtrateProductList(@RequestBody CustomerSearchProductCondition condition) {
        ResponseResult<ProductSkuMsgVO>  responseResult = new ResponseResult<>();
        logger.info("{} - 筛选列表初始化接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> filtrateProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }

        //查询门店下是否有推荐商品
        Boolean flag = storeProductManageService.queryRecommendFlag(condition.getStoreId());

        //获取门店下的商品
        StoreProductManageCondition storeProductManageCondition = new StoreProductManageCondition();
        storeProductManageCondition.setStoreId(condition.getStoreId());
        storeProductManageCondition.setProdStatus(Arrays.asList(StoreProductStatusEnum.PUTAWAY.getStatusCode()));
        storeProductManageCondition.setRecommend(flag ? (short)1 : (short)0);
        List<String> skusByConditon = storeProductManageService.findSkusByConditon(storeProductManageCondition);

        //获取分类信息 初始化商品信息
        ProductConditionByPage productConditionByPage = new ProductConditionByPage();
        productConditionByPage.setPageNo(condition.getPageNo());
        productConditionByPage.setPageSize(condition.getPageSize());
        productConditionByPage.setProductSkus(skusByConditon);
        productConditionByPage.setRecommend(flag ? 1 : null);
        productConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        responseResult = productServiceClient.getProductSkuMsg(productConditionByPage);
        logger.info("{} - 筛选列表初始化接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "店铺热销商品", response = ResponseResult.class, notes = "店铺热销商品")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002,message = "请求缺少参数门店id", response = ResponseResult.class)})
    @PostMapping(value = "product/security/2003/v1/hotProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductSkuVO>> hotProductList(@RequestBody CustomerSearchProductCondition condition) {
        ResponseResult<PagedList<ProductSkuVO>> responseResult = new ResponseResult<>();
        logger.info("{} - 店铺热销商品接口 入参：{}", MODULE_NAME, JsonUtil.toJSONString(condition));
        if (condition.getStoreId() == null){
            logger.error("ApiProductController -> hotProductList获取的参数storeId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        //获取门店下的商品
        StoreProductManageCondition storeProductManageCondition = new StoreProductManageCondition();
        storeProductManageCondition.setStoreId(condition.getStoreId());
        storeProductManageCondition.setProdStatus(Arrays.asList((short)1));
        storeProductManageCondition.setOrderBy(2);
        storeProductManageCondition.setDescAsc((byte)1);
        storeProductManageCondition.setPageNo(condition.getPageNo());
        storeProductManageCondition.setPageSize(condition.getPageSize());
        List<String> skusByConditon = storeProductManageService.findSkusByConditon(storeProductManageCondition);

        //获取商品列表信息
        ProductConditionByPage productConditionByPage = new ProductConditionByPage();
        productConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        productConditionByPage.setProductSkus(skusByConditon);
        productConditionByPage.setPageNo(condition.getPageNo());
        productConditionByPage.setPageSize(condition.getPageSize());
        responseResult = productServiceClient.getProductSkusByPage(productConditionByPage);
        logger.info("{} - 店铺热销商品接口 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }


}
