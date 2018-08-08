package com.winhxd.b2c.store.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.winhxd.b2c.common.cache.Cache;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.product.vo.ProductVO;
import com.winhxd.b2c.common.domain.store.condition.AllowPutawayProdCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProdOperateEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreSubmitProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;
import com.winhxd.b2c.common.feign.hxd.StoreHxdServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreSubmitProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 惠小店门店商品相关接口
 *
 * @ClassName: ApiStoreProductManageController
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午2:58:37
 */
@Api(value = "api storeProductManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api/store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreProductManageController {
    private static final Logger logger = LoggerFactory.getLogger(ApiStoreProductManageController.class);
    @Autowired
    private StoreProductManageService storeProductManageService;
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private StoreHxdServiceClient storeHxdServiceClient;
    /**
     * 惠下单商品
     */
    private static final Byte HXD_PROD_TYPE = 0;
    /**
     * 普通商品
     */
    private static final Byte COMMON_PROD_TYPE = 1;

    @ApiOperation(value = "B端获取可上架商品数据接口", response = ResponseResult.class, notes = "B端获取可上架商品数据接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1006, message = "账号未启用！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效！", response = ResponseResult.class)})
    @PostMapping(value = "1012/allowPutawayProdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<ProductMsgVO> allowPutawayProdList(@RequestBody AllowPutawayProdCondition condition) {
        ResponseResult<ProductMsgVO> responseResult = new ResponseResult<>();
        try {
            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
            //参数校验
            if (!checkParam(condition)) {
                responseResult.setCode(BusinessCode.CODE_1007);
                return responseResult;
            }
            //账号信息校验
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null) {
                responseResult.setCode(BusinessCode.CODE_1002);
                return responseResult;
            }

            //判断查询可上架商品类型
            Byte prodType = condition.getProdType();

            Long storeId = condition.getStoreId();

            //已上架的商品sku
            List<String> putawayProdSkus = null;

            //查询商品信息
            ProductConditionByPage prodConditionByPage = new ProductConditionByPage();
            prodConditionByPage.setPageNo(condition.getPageNo());
            prodConditionByPage.setPageSize(condition.getPageSize());
            prodConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.NOT_IN_SKU_CODE);


            //惠下单商品
            if (HXD_PROD_TYPE.equals(prodType)) {
                //在惠下单下过单的sku
                List<String> hxdBuyedProdSkuList = null;
                Long customerId = storeUser.getStoreCustomerId();
                //调用接口请求该用户惠下单商品sku
                ResponseResult<List<String>> storehxdResult = storeHxdServiceClient.getStoreBuyedProdSku(customerId);
                if (storehxdResult.getCode() == 0 && storehxdResult.getData() != null
                        && storehxdResult.getData().size() > 0) {
                    hxdBuyedProdSkuList = storehxdResult.getData();
                    //设置惠下单下过单的商品
                    prodConditionByPage.setHxdProductSkus(hxdBuyedProdSkuList);
                } else {
                    //没有下过单
                    responseResult.setCode(BusinessCode.CODE_1006);
                    return responseResult;
                }
            }
            //查询该门店上架sku集合
            StoreProductManageCondition spmCondition = new StoreProductManageCondition();
            spmCondition.setStoreId(storeId);
            //设置状态
            List<Byte> prodStatus = new ArrayList<>();
            prodStatus.add((byte) StoreProductStatusEnum.PUTAWAY.getStatusCode());
            spmCondition.setProdStatus(prodStatus);
            putawayProdSkus = storeProductManageService.findSkusByConditon(spmCondition);
            //放入已上架skus
            prodConditionByPage.setProductSkus(putawayProdSkus);

            //首次请求，会返回类型，品牌等信息
            if (condition.getFirst()) {//ProductBaseMsgVO

                responseResult = productServiceClient.getProductMsg(prodConditionByPage);
            } else {
                //需要带一级分类编码跟品牌编码（如果有品牌的话）

                //一级分类
                String categoryCode = condition.getCategoryCode();
                if (StringUtils.isNotEmpty(categoryCode)) {

                    prodConditionByPage.setCategoryCode(categoryCode);
                }
                //品牌
                String brandCode = condition.getBrandCode();

                if (StringUtils.isNotEmpty(brandCode)) {
                    List<String> brandCodeList = new ArrayList<>();
                    brandCodeList.add(brandCode);
                    prodConditionByPage.setBrandCodes(brandCodeList);
                }
                responseResult = productServiceClient.getProductMsg(prodConditionByPage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "B端商品操作接口(上架(包括批量)，下架，删除，编辑)", response = ResponseResult.class, notes = "B端商品操作接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1013/storeProdOperate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> storeProdOperate(@RequestBody ProdOperateCondition condition) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        try {
            logger.info("B端商品操作接口入参为：{}", condition);
            //参数校验
            if (!checkParam(condition)) {
                responseResult.setCode(BusinessCode.CODE_1007);
                responseResult.setMessage("参数错误");
                return responseResult;
            }
            //获取当前门店用户
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null) {
                responseResult.setCode(BusinessCode.CODE_1002);
                return responseResult;
            }
            //操作类型
            Byte operateType = condition.getOperateType();
            //门店id
            Long storeId = condition.getStoreId();
            //skuCode数组
            List<ProdOperateInfoCondition> prodInfo = condition.getProducts();

            //判断操作类型
            if (StoreProdOperateEnum.PUTAWAY.getOperateCode() == operateType) {
                List<String> skucodes = new ArrayList<>();
                for (int i = 0; i < prodInfo.size(); i++) {
                    skucodes.add(prodInfo.get(i).getSkuCode());
                }
                //查询过来的sku是否有的已经上架
                StoreProductManageCondition spmCondition = new StoreProductManageCondition();
                spmCondition.setStoreId(storeId);
                List<Byte> prodStatus = new ArrayList<>();
                prodStatus.add((byte) StoreProductStatusEnum.PUTAWAY.getStatusCode());
                spmCondition.setProdStatus(prodStatus);

                List<StoreProductManage> spms = storeProductManageService.findPutawayProdBySkuCodes(storeId, (String[]) skucodes.toArray());
                //上架操作
                //表示还没上架过
                if (spms == null || spms.size() == 0) {
                    //调用商品接口获取商品相关信息
                    List<ProductSkuVO> prodSkuList = new ArrayList<>();
                    ProductCondition prodCondition = new ProductCondition();
                    prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                    prodCondition.setProductSkus(skucodes);
                    //查询商品详情
                    ResponseResult<List<ProductSkuVO>> productResult = productServiceClient.getProductSkus(prodCondition);
                    if (productResult.getCode() == 0) {

                        List<ProductSkuVO> productList = productResult.getData();
                        //查询信息一一对应
                        if (productList != null && productList.size() == prodInfo.size()) {
                            Map<String, ProdOperateInfoCondition> putawayInfo = new HashMap<>();
                            for (ProdOperateInfoCondition p : prodInfo) {
                                putawayInfo.put(p.getSkuCode(), p);
                            }
                            Map<String, ProductSkuVO> prodSkuInfo = new HashMap<>();
                            for (ProductSkuVO prod : productList) {
                                prodSkuInfo.put(prod.getSkuCode(), prod);
                            }
                            //商品上架
                        }
                    }
                    //保存StoreProductManage
                    //初始化门店商品对应统计信息

                } else {
                    //表示商品中有部分已经上架过了
                    //responseResult.setCode(BusinessCode.);
                    return responseResult;
                }

            } else if (StoreProdOperateEnum.UNPUTAWAY.getOperateCode() == operateType) {
                //下架操作

            } else if (StoreProdOperateEnum.DELETE.getOperateCode() == operateType) {
                //删除操作

            } else if (StoreProdOperateEnum.EDIT.getOperateCode() == operateType) {
                //编辑

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseResult;
    }

    @ApiOperation(value = "B端添加门店提报商品接口", response = ResponseResult.class, notes = "B端添加门店提报商品接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1014/saveStoreSubmitProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Void> saveStoreSubmitProduct(@RequestBody StoreSubmitProductCondition condition) {
        ResponseResult<Void> responseResult = new ResponseResult<>();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseResult;
    }

    @ApiOperation(value = "B端门店提报商品列表接口", response = ResponseResult.class, notes = "B端门店提报商品列表接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1015/findStoreSubmitProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<StoreSubmitProductVO>> findStoreSubmitProductList(@RequestBody StoreSubmitProductCondition condition) {
        ResponseResult<PagedList<StoreSubmitProductVO>> responseResult = new ResponseResult<>();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }

        return responseResult;
    }

    @ApiOperation(value = "B端搜索商品接口", response = ResponseResult.class, notes = "B端搜索商品接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数异常！", response = ResponseResult.class)})
    @PostMapping(value = "1018/searchProductByKey", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<ProductVO>> searchProductByKey(@RequestBody AllowPutawayProdCondition condition) {
        ResponseResult<PagedList<ProductVO>> responseResult = new ResponseResult<>();
        try {
            //UserContext.getCurrentStoreUser().getStoreCustomerId();
            logger.info("B端搜索商品接口入参为：{}", condition);
            //参数校验
            if (StringUtils.isBlank(condition.getProdName())) {
                logger.error("B端搜索商品接口:参数ProdName为空");
                return new ResponseResult<>(BusinessCode.CODE_1007);
            }
            if (null == condition.getProdType()) {
                logger.error("B端搜索商品接口:参数ProdType为空");
                return new ResponseResult<>(BusinessCode.CODE_1007);
            }
            if (null == condition.getPageNo()) {
                logger.error("B端搜索商品接口:参数PageNo为空");
                return new ResponseResult<>(BusinessCode.CODE_1007);
            }
            if (null == condition.getPageSize()) {
                logger.error("B端搜索商品接口:参数PageSize为空");
                return new ResponseResult<>(BusinessCode.CODE_1007);
            }
            StoreProductManageCondition manageCondition = new StoreProductManageCondition();
            manageCondition.setStoreId(UserContext.getCurrentStoreUser().getBusinessId());
            List<Byte> prodStatus = new ArrayList<>();
            prodStatus.add((byte) StoreProductStatusEnum.PUTAWAY.getStatusCode());
            prodStatus.add((byte) StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
            manageCondition.setProdStatus(prodStatus);
            List<String> skusByConditon = storeProductManageService.findSkusByConditon(manageCondition);
            ProductConditionByPage productCondition = new ProductConditionByPage();
            productCondition.setProductName(condition.getProdName());
            productCondition.setProductSkus(skusByConditon);
            if (HXD_PROD_TYPE.equals(condition.getProdType())) {
                productCondition.setHxdProductSkus(getStoreBuyedHxdProdSkus(UserContext.getCurrentStoreUser().getStoreCustomerId()));
            }
            productCondition.setPageNo(condition.getPageNo());
            productCondition.setPageSize(condition.getPageSize());
            ResponseResult<PagedList<ProductVO>> productVo = productServiceClient.getProductsByPage(productCondition);
            responseResult.setData(productVo.getData());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseResult;
    }

    @ApiOperation(value = "B端我的商品管理接口", response = ResponseResult.class, notes = "B端我的商品管理接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1019/myStoreProdManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<PagedList<StoreProdSimpleVO>> myStoreProdManage(@RequestBody StoreProductManageCondition condition) {
        ResponseResult<PagedList<StoreProdSimpleVO>> responseResult = new ResponseResult<>();
        try {
            logger.info("B端我的商品管理接口入参为：{}", condition);
            //参数校验
            if (!checkParam(condition)) {
                responseResult.setCode(BusinessCode.CODE_1007);
                responseResult.setMessage("参数错误");
                return responseResult;
            }
            //获取当前门店用户
            StoreUser storeUser = UserContext.getCurrentStoreUser();
            if (storeUser == null) {
                responseResult.setCode(BusinessCode.CODE_1002);
                return responseResult;
            }
            Long storeId = storeUser.getBusinessId();
            condition.setStoreId(storeId);
            PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
            PagedList<StoreProdSimpleVO> list = storeProductManageService.findSimpelVOByCondition(condition);
            responseResult.setData(list);

        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误");
        }
        return responseResult;
    }

    private List<String> getStoreBuyedHxdProdSkus(Long storeCustomerId) {
        if (null == storeCustomerId) {
            return null;
        }
        ResponseResult<List<String>> storeBuyedProdSku = storeHxdServiceClient.getStoreBuyedProdSku(storeCustomerId);
        List<String> skuData = storeBuyedProdSku.getData();
        //cache.set("",skuData);
        //cache.lpush()
        return skuData;
    }

    private boolean checkParam(Object condition) {
        boolean flag = false;
        if (condition != null) {
            //AllowPutawayProdCondition 必须传参数校验
            if (condition instanceof AllowPutawayProdCondition) {
                AllowPutawayProdCondition c = (AllowPutawayProdCondition) condition;
                if (c.getProdType() != null) {
                    flag = true;
                }
            }
            //ProdOperateCondition 必须传参数校验
            if (condition instanceof ProdOperateCondition) {
                ProdOperateCondition c = (ProdOperateCondition) condition;
                if (c.getOperateType() != null
                        && c.getProducts() != null && c.getProducts().size() > 0) {
                    flag = true;
                }
            }
            //StoreSubmitProductCondition 必须传参数校验
            if (condition instanceof StoreSubmitProductCondition) {
                StoreSubmitProductCondition c = (StoreSubmitProductCondition) condition;
                if (c.getProdImage1() != null) {
                    flag = true;
                }
            }
            //StoreProductManageCondition 必须传参数校验
            if (condition instanceof StoreProductManageCondition) {
                StoreProductManageCondition c = (StoreProductManageCondition) condition;
                if (c.getProdStatus() != null && c.getProdStatus().size() > 0) {
                    flag = true;
                }
            }
        }
        return flag;
    }


}
