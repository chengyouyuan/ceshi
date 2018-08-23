package com.winhxd.b2c.store.service.impl;

import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCartProductCondition;
import com.winhxd.b2c.common.domain.order.vo.ShopCartProductVO;
import com.winhxd.b2c.common.domain.product.condition.CustomerSearchProductCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductConditionByPage;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.feign.order.ShopCartServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.store.service.ProductService;
import com.winhxd.b2c.store.service.StoreProductManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caiyulong
 */
@Service
public class ProductServiceImpl implements ProductService {
 
    @Autowired
    private StoreProductManageService storeProductManageService;

    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private ShopCartServiceClient shopCartServiceClient;

    private ProductConditionByPage buildProductConditionByPage(boolean bool, CustomerSearchProductCondition condition, List<StoreProductManage> storeProductManages) {
        ProductConditionByPage productConditionByPage = new ProductConditionByPage();
        productConditionByPage.setPageNo(condition.getPageNo());
        productConditionByPage.setPageSize(condition.getPageSize());
        productConditionByPage.setProductName(condition.getProductName());
        productConditionByPage.setBrandCodes(condition.getBrandCodes());
        productConditionByPage.setCategoryCodes(condition.getCategoryCodes());
        productConditionByPage.setCategoryCode(condition.getCategoryCode());
        productConditionByPage.setProductSkus(storeProductManages.stream()
                .map(storeProductManage -> storeProductManage.getSkuCode()).collect(Collectors.toList()));
        //初始化页面不需要判断推荐状态
        if (bool){
            productConditionByPage.setRecommendSkus(storeProductManages.stream()
                    .filter(storeProductManage -> storeProductManage.getRecommend() == 1)
                    .map(storeProductManage -> storeProductManage.getSkuCode()).collect(Collectors.toList()));
        }else{
            productConditionByPage.setRecommendSkus(condition.getRecommend() != null && condition.getRecommend() == 1 ?storeProductManages.stream()
                    .filter(storeProductManage -> storeProductManage.getRecommend() == 1)
                    .map(storeProductManage -> storeProductManage.getSkuCode()).collect(Collectors.toList()):null);
        }
        productConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        return productConditionByPage;
    }

    private StoreProductManageCondition biuldStoreProductManageCondition(CustomerSearchProductCondition condition) {
        StoreProductManageCondition storeProductManageCondition = new StoreProductManageCondition();
        storeProductManageCondition.setPageSize(condition.getPageSize());
        storeProductManageCondition.setPageNo(condition.getPageNo());
        storeProductManageCondition.setStoreId(condition.getStoreId());
        storeProductManageCondition.setProdStatus(Arrays.asList(StoreProductStatusEnum.PUTAWAY.getStatusCode()));
        storeProductManageCondition.setRecommend(condition.getRecommend() == null || condition.getRecommend() != null && condition.getRecommend() != 1 ? null : (short)1);
        storeProductManageCondition.setOrderBy(condition.getProductSortType());
        storeProductManageCondition.setDescAsc(condition.getProductSortType() != null && condition.getProductSortType().equals(1)
                ? (byte)0 : (byte)1);
        return  storeProductManageCondition;
    }

    private void assignSkuNum(List<ProductSkuVO> productSkuVOS, CustomerUser currentCustomerUser, CustomerSearchProductCondition condition) {
        List<String> collect = productSkuVOS.stream().map(productSkuVO -> productSkuVO.getSkuCode()).collect(Collectors.toList());
        ShopCartProductCondition shopCartProductCondition = buildShopCartProductCondition(condition,collect,currentCustomerUser);
        List<ShopCartProductVO> shopCartProductVOS = shopCartServiceClient.queryShopCartBySelective(shopCartProductCondition).getData();

        for (ProductSkuVO datum : productSkuVOS) {
            for (ShopCartProductVO shopCartProductVO : shopCartProductVOS) {
                if (datum.getSkuCode().equals(shopCartProductVO.getSkuCode())){
                    datum.setAmount(shopCartProductVO.getAmount());
                    break;
                }
            }
        }
    }

    private void assignSellMoney(List<ProductSkuVO> data, List<StoreProductManage> storeProductManages) {
        for (ProductSkuVO datum : data) {
            datum.setAmount(0);
            for (StoreProductManage storeProductManage : storeProductManages) {
                if (datum.getSkuCode().equals(storeProductManage.getSkuCode())){
                    datum.setSellMoney(storeProductManage.getSellMoney());
                    break;
                }
            }
        }
    }

    private ShopCartProductCondition buildShopCartProductCondition(CustomerSearchProductCondition condition, List<String> skus, CustomerUser currentCustomerUser) {
        ShopCartProductCondition shopCartProductCondition = new ShopCartProductCondition();
        shopCartProductCondition.setCustomerId(currentCustomerUser.getCustomerId());
        shopCartProductCondition.setStoreId(condition.getStoreId());
        shopCartProductCondition.setSkuCodes(skus);
        return shopCartProductCondition;
    }

    @Override
    public ResponseResult<ProductSkuMsgVO> filtrateProductList(CustomerSearchProductCondition condition, CustomerUser currentCustomerUser) {
        ResponseResult<ProductSkuMsgVO> responseResult = new ResponseResult<>();
        //获取门店下的商品
        StoreProductManageCondition storeProductManageCondition = biuldStoreProductManageCondition(condition);
        List<StoreProductManage> storeProductManages = storeProductManageService.findProductBySelective(storeProductManageCondition);
        if (storeProductManages.isEmpty()){
            return responseResult;
        }
        //获取分类信息 初始化商品信息
        ProductConditionByPage productConditionByPage = buildProductConditionByPage(true, condition, storeProductManages);
        responseResult = productServiceClient.getProductSkuMsg(productConditionByPage);

        //判断是否有店主推荐
        responseResult.getData().setRecommendFlag(0);
        if (productConditionByPage.getRecommendSkus() != null && productConditionByPage.getRecommendSkus().size() > 0){
            responseResult.getData().setRecommendFlag(1);
        }

        PagedList<ProductSkuVO> productSkus = responseResult.getData().getProductSkus();
        if (productSkus == null || productSkus.getData().isEmpty()){
            return responseResult;
        }
        //赋值商品价格
        assignSellMoney(productSkus.getData(), storeProductManages);

        if (currentCustomerUser != null){
            //查询商品购物车数量
            assignSkuNum(productSkus.getData(), currentCustomerUser, condition);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<ProductSkuVO>> searchProductList(CustomerSearchProductCondition condition, CustomerUser currentCustomerUser) {
        ResponseResult<PagedList<ProductSkuVO>> responseResult = new ResponseResult<>();

        StoreProductManageCondition storeProductManageCondition = biuldStoreProductManageCondition(condition);
        List<StoreProductManage> storeProductManages = storeProductManageService.findProductBySelective(storeProductManageCondition);
        if (storeProductManages.isEmpty()){
            return responseResult;
        }

        //获取商品列表信息
        ProductConditionByPage productConditionByPage = buildProductConditionByPage(false, condition, storeProductManages);
        responseResult = productServiceClient.getProductSkusByPage(productConditionByPage);

        PagedList<ProductSkuVO> productSkus = responseResult.getData();
        if (productSkus == null || productSkus.getData().isEmpty()){
            return responseResult;
        }
        //价格赋值
        assignSellMoney(productSkus.getData(),storeProductManages);
        if (currentCustomerUser != null){
            //查询商品购物车数量
            assignSkuNum(productSkus.getData(),currentCustomerUser,condition);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<ProductSkuVO>> hotProductList(CustomerSearchProductCondition condition) {
        ResponseResult<PagedList<ProductSkuVO>> responseResult = new ResponseResult<>();
        //获取门店下的商品
        condition.setProductSortType(2);
        StoreProductManageCondition storeProductManageCondition = biuldStoreProductManageCondition(condition);
        List<StoreProductManage> storeProductManages = storeProductManageService.findProductBySelective(storeProductManageCondition);
        if (storeProductManages.isEmpty()){
            return responseResult;
        }

        //获取商品列表信息
        ProductConditionByPage productConditionByPage = new ProductConditionByPage();
        productConditionByPage.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
        productConditionByPage.setProductSkus(storeProductManages.stream()
                .map(storeProductManage -> storeProductManage.getSkuCode()).collect(Collectors.toList()));
        productConditionByPage.setPageNo(condition.getPageNo());
        productConditionByPage.setPageSize(condition.getPageSize());
        responseResult = productServiceClient.getProductSkusByPage(productConditionByPage);

        //价格赋值
        assignSellMoney(responseResult.getData().getData(), storeProductManages);
        return responseResult;
    }

}
