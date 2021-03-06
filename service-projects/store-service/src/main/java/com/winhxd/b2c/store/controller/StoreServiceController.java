package com.winhxd.b2c.store.controller;


import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.*;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.condition.CustomerBindingStatusCondition;
import com.winhxd.b2c.common.domain.system.login.condition.StoreUserInfoCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreProductStatisticsService;
import com.winhxd.b2c.store.service.StoreRegionService;
import com.winhxd.b2c.store.service.StoreService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * @Description: 门店服务控制器
 * @author chengyy
 * @date 2018/8/3 10:46
 */
@RestController
public class StoreServiceController implements StoreServiceClient {
    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreProductManageService storeProductManageService;

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
	private StoreProductStatisticsService storeProductStatisticsService;
    
    @Autowired
    private ProductServiceClient productServiceClient;

    @Autowired
    private StoreRegionService storeRegionService;

    private Logger logger = LoggerFactory.getLogger(StoreService.class);

    @Override
    public ResponseResult<Integer> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId) {
        if (customerId == null) {
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (storeUserId == null) {
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        //检查用户id和storeuserId有效
        StoreUserInfoVO storeUserInfoVO = storeService.findStoreUserInfo(storeUserId);
        if (storeUserInfoVO == null) {
            throw new BusinessException(BusinessCode.CODE_200004);
        }
		if(!checkCustomerExist(customerId)){
        	throw new BusinessException(BusinessCode.CODE_200010);
		}
        int status = storeService.bindCustomer(customerId,storeUserId).getStatus();
		ResponseResult<Integer> result = new ResponseResult<>();
		result.setData(status);
        return result;
    }

    public boolean checkCustomerExist(Long customerId) {
		List<Long> ids = new ArrayList<>();
		ids.add(customerId);
		List<CustomerUserInfoVO>  list = customerServiceClient.findCustomerUserByIds(ids).getDataWithException();
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}


	@Override
	public ResponseResult<List<ShopCartProdVO>> findShopCarProd(@RequestParam("skuCodes")List<String> skuCodes, @RequestParam("storeId")Long storeId) {
		ResponseResult<List<ShopCartProdVO>> result = new ResponseResult<>();
		//参数检验
		if (storeId == null || CollectionUtils.isEmpty(skuCodes)) {
			 result = new ResponseResult<>(BusinessCode.CODE_103101);
		}
		logger.info("StoreServiceClient-->findShopCarProd 入参：skuCodes={},storeId={}",skuCodes,storeId);
		String []skuCodeArray = new String[skuCodes.size()];
		skuCodeArray = skuCodes.toArray(skuCodeArray);
		//查询门店下商品信息集合--判断数据权限
		//查询该用户sku是否上架
		List<StoreProductManage> storeProds = storeProductManageService.findPutawayProdBySkuCodes(storeId, skuCodeArray);
		//查询结果不为空
		if (CollectionUtils.isNotEmpty(storeProds)) {
			ProductCondition prodCondition = new ProductCondition();
			prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
			prodCondition.setProductSkus(skuCodes);
			ResponseResult<List<ProductSkuVO>> prodResult = productServiceClient.getProductSkus(prodCondition);
			//调用商品feigin查询商品基本信息
			List<ProductSkuVO> prodList = prodResult.getDataWithException();
			if(prodList == null){
				return new ResponseResult<>(BusinessCode.CODE_103102);
			}
			List<ShopCartProdVO> shopCarProdList = new ArrayList<>(prodList.size());
			for (int i = 0; i < prodList.size(); i++) {
				ShopCartProdVO spVO = new ShopCartProdVO();
				//sku信息
				ProductSkuVO current = prodList.get(i);
				//门店与sku关系
				StoreProductManage spManage = null;
				for (StoreProductManage spm : storeProds) {
				    if (current.getSkuCode().equals(spm.getSkuCode())) {
				        spManage = spm;
				    }
				}
				if (spManage == null) {
                    continue;
                }
				spVO.setSkuCode(current.getSkuCode());
				spVO.setSkuImage(current.getSkuImage());
				spVO.setProdStatus(spManage.getProdStatus());
				spVO.setSellMoney(spManage.getSellMoney());
				spVO.setProdName(current.getSkuName() == null ? "" : current.getSkuName());
				spVO.setBrandCode(current.getBrandCode());
				spVO.setSkuAttributeOption(current.getSkuAttributeOption());
				spVO.setCompanyCode(current.getCompanyCode());
				shopCarProdList.add(spVO);
			}
			result.setData(shopCarProdList);
		}
		logger.info("StoreServiceClient-->findShopCarProd 返参：result={}",result);
		return result;
	}

	@Override
	public ResponseResult<StoreUserInfoVO> findStoreUserInfoByCustomerId(@RequestParam("customerUserId")Long customerUserId) {
		if(customerUserId == null) {
			throw new BusinessException(BusinessCode.CODE_200001);
		}
		StoreUserInfoVO storeInfo = storeService.findStoreUserInfoByCustomerId(customerUserId);
		ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(storeInfo == null){
			responseResult.setCode(BusinessCode.CODE_200009);
		}
		responseResult.setData(storeInfo);
		return responseResult;
	}

	@Override
	public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@RequestParam("id")Long id) {
		if(id == null){
			throw new BusinessException(BusinessCode.CODE_200002);
		}
		StoreUserInfoVO data = storeService.findStoreUserInfo(id);
		ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(data == null){
			responseResult.setCode(BusinessCode.CODE_200004);
		}
		responseResult.setData(data);
		return responseResult;
	}

	@Override
	public ResponseResult<List<StoreUserInfoVO>> findStoreUserInfoList(@RequestBody Set<Long> ids) {
    	if(ids == null || ids.size() == 0){
    		throw new BusinessException(BusinessCode.CODE_200001);
		}
		List<StoreUserInfoVO> storeInofs = storeService.findStoreUserInfoList(ids);
		ResponseResult<List<StoreUserInfoVO>> responseResult = new ResponseResult<>();
    	responseResult.setData(storeInofs);
		return responseResult;
	}

	@Override
	public ResponseResult<Void> saveStoreProductStatistics(@RequestBody List<StoreProductStatisticsCondition> conditions) {
		logger.info("StoreServiceClient-->findShopCarProd 入参：conditions={}",conditions);
		if(conditions == null || conditions.size() <= 0){
		    return new ResponseResult<>(BusinessCode.CODE_102401);
		}
		ResponseResult<Void> result = new ResponseResult<>();
		List<StoreProductStatistics> beanList = new ArrayList<>();
		for(StoreProductStatisticsCondition condition : conditions){
			StoreProductStatistics bean = new StoreProductStatistics();
			BeanUtils.copyProperties(condition, bean);
			bean.setCreated(new Date());
			beanList.add(bean);
		}
		storeProductStatisticsService.bathSaveStoreProductStatistics(beanList);
		logger.info("StoreServiceClient-->saveStoreProductStatistics 返参：result="+result);
		return result;
	}

	@Override
	public ResponseResult<PagedList<StoreRegionVO>> findStoreRegions(@RequestBody StoreRegionCondition conditions) {
		ResponseResult<PagedList<StoreRegionVO>> result = new ResponseResult<>();
		PagedList<StoreRegionVO> data = storeRegionService.findStoreRegions(conditions);
		result.setData(data);
		return result;
	}

	@Override
	public ResponseResult<Void> removeStoreRegion(@RequestParam("id") Long id) {
		if (null == id) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		AdminUser adminUser = UserContext.getCurrentAdminUser();
		storeRegionService.removeStoreRegion(adminUser,id);
		return new ResponseResult<>();
	}

	@Override
	public ResponseResult<Void> saveStoreRegion(@RequestBody StoreRegionCondition condition) {
		if (null == condition || StringUtils.isBlank(condition.getAreaCode())) {
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		AdminUser adminUser = UserContext.getCurrentAdminUser();
	 	storeRegionService.saveStoreRegion(adminUser,condition);
		return new ResponseResult<>();
	}

	@Override
	public ResponseResult<PagedList<StoreUserInfoVO>> queryStorePageInfo(@RequestBody BackStageStoreInfoSimpleCondition condition) {
		ResponseResult<PagedList<StoreUserInfoVO>> responseResult = new ResponseResult<PagedList<StoreUserInfoVO>>();
		if (null == condition) {
			condition = new BackStageStoreInfoSimpleCondition();
		}
		PagedList<StoreUserInfoVO> page = storeService.findStorePageInfo(condition);
		responseResult.setData(page);
		return responseResult;
	}

	@Override
	public ResponseResult<Boolean> saveStoreCodeUrl(@RequestBody StoreUserInfoCondition condition) {
    	if(null == condition || null == condition.getId()){
    		throw new BusinessException(BusinessCode.CODE_200002);
		}
		if(StringUtils.isEmpty(condition.getMiniProgramCodeUrl())){
    		throw new BusinessException(BusinessCode.CODE_200017);
		}
		boolean result = storeService.updateStoreCodeUrl(condition);
		ResponseResult<Boolean> responseResult = new ResponseResult<Boolean>();
		responseResult.setData(result);
		return responseResult;
	}
	@Override
	public ResponseResult<List<StoreUserInfoVO>> getStoreListByKeywords(@RequestBody StoreListByKeywordsCondition condition) {
		ResponseResult<List<StoreUserInfoVO>> responseResult = new ResponseResult<>();
		List<StoreUserInfoVO> result = storeService.getStoreListByKeywords(condition);
		responseResult.setData(result);
		return responseResult;
	}

	@Override
	public ResponseResult<List<Long>> findStoreCustomerRegions(@RequestBody StoreCustomerRegionCondition conditions) {
		logger.info("StoreServiceClient-->findStoreCustomerRegions 入参：conditions= {}",conditions);
    	ResponseResult<List<Long>> responseResult = new ResponseResult<>();
    	List<Long> result = storeService.findStoreCustomerRegions(conditions);
    	responseResult.setData(result);
		logger.info("StoreServiceClient-->findStoreCustomerRegions 返参：result={}",result);
		return responseResult;
	}

	@Override
	public ResponseResult<StoreUserInfoVO> queryStoreUserInfoByCustomerId(Long customerUserId) {
		ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		StoreUserInfoVO storeInfo = storeService.findStoreUserInfoByCustomerId(customerUserId);
		responseResult.setData(storeInfo);
		return responseResult;
	}

    @Override
    public ResponseResult<Boolean> unBundling(@RequestBody List<CustomerBindingStatusCondition> condition) {
		if (condition == null) {
			throw new BusinessException(BusinessCode.CODE_200001);
		}
        ResponseResult<Boolean> responseResult = new ResponseResult<>();
        int i = storeService.unBundling(condition);
		responseResult.setData(true);
        return responseResult;
    }

    @Override
    public ResponseResult<Boolean> changeBind(@RequestBody List<CustomerBindingStatusCondition> condition) {
		if (condition == null) {
			throw new BusinessException(BusinessCode.CODE_200001);
		}
        ResponseResult<Boolean> responseResult = new ResponseResult<>();
        int i = storeService.changeBind(condition);
		responseResult.setData(true);
        return responseResult;
    }
}
