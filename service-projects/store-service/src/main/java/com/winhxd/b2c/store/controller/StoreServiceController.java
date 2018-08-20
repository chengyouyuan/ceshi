package com.winhxd.b2c.store.controller;


import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoSimpleCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreListByKeywordsCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
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
//    @Autowired
//    private ProductServiceClient productServiceClient;

    @Override
    public ResponseResult<Integer> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId) {
        ResponseResult<Integer> result = new ResponseResult<>();
        if(customerId == null) {
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (storeUserId == null) {
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        //检查用户id和storeuserId有效
		StoreUserInfoVO storeUserInfoVO = storeService.findStoreUserInfo(storeUserId);
        if(storeUserInfoVO == null){
        	throw new BusinessException(BusinessCode.CODE_200004);
		}

		if(!checkCustomerExist(customerId)){
        	throw new BusinessException(BusinessCode.CODE_200010);
		}
        int status = storeService.bindCustomer(customerId,storeUserId);
		result.setData(status);
        return result;
    }

    public boolean checkCustomerExist(Long customerId){
		List<Long> ids = new ArrayList<>();
		ids.add(customerId);
		List<CustomerUserInfoVO>  list = customerServiceClient.findCustomerUserByIds(ids).getData();
		if(list != null && list.size() > 0){
			return true;
		}
		return false;
	}


	@Override
	public ResponseResult<List<ShopCartProdVO>> findShopCarProd(@RequestParam("skuCodes")List<String> skuCodes, @RequestParam("storeId")Long storeId) {
		ResponseResult<List<ShopCartProdVO>> result = new ResponseResult<>();
		
		//参数检验
		if(storeId==null||CollectionUtils.isEmpty(skuCodes)){
			 logger.error("StoreServiceController -> findShopCarProd获取的参数异常！");
			 result= new ResponseResult<>(BusinessCode.CODE_1007);
		}
		logger.info("StoreServiceClient-->findShopCarProd 入参：skuCodes="+skuCodes+",storeId="+storeId);
		String skuCodeArray[]=new String[skuCodes.size()];
		skuCodeArray=skuCodes.toArray(skuCodeArray);
		//查询门店下商品信息集合--判断数据权限
		//查询该用户sku是否上架
		List<StoreProductManage> storeProds=this.storeProductManageService.findPutawayProdBySkuCodes(storeId, skuCodeArray);
		
		//查询结果不为空
		if(CollectionUtils.isNotEmpty(storeProds)){
			if(storeProds.size()!=skuCodes.size()){
				result= new ResponseResult<>(BusinessCode.CODE_200012);
				return result;
			}
			
			ProductCondition prodCondition=new ProductCondition();
			prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
			prodCondition.setProductSkus(skuCodes);
			
			ResponseResult<List<ProductSkuVO>> prodResult=productServiceClient.getProductSkus(prodCondition);
			if(prodResult==null||prodResult.getCode()!=0){
			    result= new ResponseResult<>(BusinessCode.CODE_1001);
				return result;
			}
			//调用商品feigin查询商品基本信息
			List<ProductSkuVO> prodList=prodResult.getData();
			if(prodList==null||(prodList.size()!=skuCodes.size())){
			    result= new ResponseResult<>(BusinessCode.CODE_200012);
				return result;
			}
			
			//productServiceClient.
			List<ShopCartProdVO> shopCarProdList=new ArrayList<>();
			for(int i=0;i<prodList.size();i++){
				ShopCartProdVO spVO=new ShopCartProdVO();
				//sku信息
				ProductSkuVO current=prodList.get(i);
				//门店与sku关系
				StoreProductManage spManage=null;
				for(StoreProductManage spm:storeProds){
				    if(current.getSkuCode().equals(spm.getSkuCode())){
				        spManage=spm;
				    }
				}
				spVO.setSkuCode(current.getSkuCode());
				spVO.setSkuImage(current.getSkuImage());
				spVO.setProdStatus(spManage.getProdStatus());
				spVO.setSellMoney(spManage.getSellMoney());
				spVO.setProdName(current.getSkuName()==null? "":current.getSkuName());
				spVO.setBrandCode(current.getBrandCode());
				spVO.setSkuAttributeOption(current.getSkuAttributeOption());
				spVO.setCompanyCode(current.getCompanyCode());
				shopCarProdList.add(spVO);
			}
			
			result.setData(shopCarProdList);
		}
		logger.info("StoreServiceClient-->findShopCarProd 返参：result="+result);
		return result;
	}

	@Override
	public ResponseResult<StoreUserInfoVO> findStoreUserInfoByCustomerId(@RequestParam("customerUserId")Long customerUserId) {
    	ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(customerUserId == null) {
			throw new BusinessException(BusinessCode.CODE_200001);
		}
		StoreUserInfoVO storeInfo = storeService.findStoreUserInfoByCustomerId(customerUserId);
		if(storeInfo == null){
			responseResult.setCode(BusinessCode.CODE_200009);
		}
		responseResult.setData(storeInfo);
		return responseResult;
	}

	@Override
	public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@RequestParam("id")Long id) {
		ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(id == null){
			throw new BusinessException(BusinessCode.CODE_200002);
		}
		StoreUserInfoVO data = storeService.findStoreUserInfo(id);
		if(data == null){
			responseResult.setCode(BusinessCode.CODE_200004);
		}
		responseResult.setData(data);
		return responseResult;
	}

	@Override
	public ResponseResult<List<StoreUserInfoVO>> findStoreUserInfoList(@RequestBody Set<Long> ids) {
		ResponseResult<List<StoreUserInfoVO>> responseResult = new ResponseResult<>();
    	if(ids == null || ids.size() == 0){
    		throw new BusinessException(BusinessCode.CODE_200001);
		}
		List<StoreUserInfoVO> storeInofs = storeService.findStoreUserInfoList(ids);
    	responseResult.setData(storeInofs);
		return responseResult;
	}

	@Override
	public ResponseResult<Void> saveStoreProductStatistics(@RequestBody List<StoreProductStatisticsCondition> conditions) {
		ResponseResult<Void> result=new ResponseResult<>();
		logger.info("StoreServiceClient-->findShopCarProd 入参：conditions="+conditions);
		if(conditions==null||conditions.size()<=0){
		    result=new ResponseResult<>(BusinessCode.CODE_1007);
		    return result;
		}
		List<StoreProductStatistics> beanList=new ArrayList<>();
		
		for(StoreProductStatisticsCondition condition:conditions){
			StoreProductStatistics bean=new StoreProductStatistics();
			if(condition.getStoreId()==null||StringUtils.isEmpty(condition.getSkuCode())){
			    result=new ResponseResult<>(BusinessCode.CODE_1007);
				return result;
			}
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
		storeRegionService.removeStoreRegion(id);
		return new ResponseResult<>();
	}

	@Override
	public ResponseResult<Void> saveStoreRegion(@RequestBody StoreRegionCondition conditions) {
	 	storeRegionService.saveStoreRegion(conditions);
		return new ResponseResult<>();
	}

	@Override
	public ResponseResult<PagedList<StoreUserInfoVO>> queryStorePageInfo(@RequestBody BackStageStoreInfoSimpleCondition condition) {
		ResponseResult<PagedList<StoreUserInfoVO>> responseResult = new ResponseResult<PagedList<StoreUserInfoVO>>();
		try {
			PagedList<StoreUserInfoVO> page = storeService.findStorePageInfo(condition);
			responseResult.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			responseResult.setCode(BusinessCode.CODE_1001);
		}
		return responseResult;
	}

	@Override
	public ResponseResult<Boolean> saveStoreCodeUrl(@RequestBody StoreUserInfoCondition condition) {
		ResponseResult<Boolean> responseResult = new ResponseResult<Boolean>();
    	if(condition.getId() == null){
    		throw new BusinessException(BusinessCode.CODE_200002);
		}
		if(StringUtils.isEmpty(condition.getMiniProgramCodeUrl())){
    		throw new BusinessException(BusinessCode.CODE_200017);
		}
		boolean result = storeService.updateStoreCodeUrl(condition);
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
	
	


}
