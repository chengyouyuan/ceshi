package com.winhxd.b2c.store.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductStatisticsCondition;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;
import com.winhxd.b2c.common.domain.store.vo.ShopCartProdVO;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreProductStatisticsService;
import com.winhxd.b2c.store.service.StoreService;

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

    private Logger logger = LoggerFactory.getLogger(StoreService.class);
//    @Autowired
//    private ProductServiceClient productServiceClient;
    @Override
    public ResponseResult<Void> bindCustomer(@RequestParam("customerId") Long customerId, @RequestParam("storeUserId") Long storeUserId) {
        ResponseResult<Void> result = new ResponseResult<>();
        if(customerId == null) {
            logger.error("StoreServiceController ->bindCustomer获取的用户id参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (storeUserId == null) {
            logger.error("StoreServiceController -> bindCustomer获取的门店id参数为空");
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
        if(status == 0){
			result.setCode(BusinessCode.CODE_1001);
		}else if(status == -1){
        	result.setCode(BusinessCode.CODE_200011);
		}else if(status == -2){
        	result.setCode(BusinessCode.CODE_200003);
		}
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
			 result.setCode(BusinessCode.CODE_1007);
			 result.setMessage("参数无效！");
		}
		String skuCodeArray[]=new String[skuCodes.size()];
		skuCodeArray=skuCodes.toArray(skuCodeArray);
		//查询门店下商品信息集合--判断数据权限
		//查询该用户sku是否上架
		List<StoreProductManage> storeProds=this.storeProductManageService.findPutawayProdBySkuCodes(storeId, skuCodeArray);
		
		//查询结果不为空
		if(CollectionUtils.isNotEmpty(storeProds)){
			if(storeProds.size()!=skuCodes.size()){
				result.setCode(BusinessCode.CODE_200012);
				result.setMessage("部分skuCode已经下架");
				return result;
			}
			
			ProductCondition prodCondition=new ProductCondition();
			prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
			prodCondition.setProductSkus(skuCodes);
			
			ResponseResult<List<ProductSkuVO>> prodResult=productServiceClient.getProductSkus(prodCondition);
			if(prodResult==null||prodResult.getCode()!=0){
				result.setCode(BusinessCode.CODE_1001);
				result.setMessage("查询商品信息异常！");
				return result;
			}
			//调用商品feigin查询商品基本信息
			List<ProductSkuVO> prodList=prodResult.getData();
			if(prodList==null||(prodList.size()!=skuCodes.size())){
				result.setCode(BusinessCode.CODE_200012);
				result.setMessage("部分skuCode失效");
				return result;
			}
			
			//productServiceClient.
			List<ShopCartProdVO> shopCarProdList=new ArrayList<>();
			for(int i=0;i<prodList.size();i++){
				ShopCartProdVO spVO=new ShopCartProdVO();
				//sku信息
				ProductSkuVO current=prodList.get(i);
				//门店与sku关系
				StoreProductManage spManage=storeProds.get(i);
				spVO.setSkuCode(current.getSkuCode());
				spVO.setProdImage(current.getSkuImage());
				spVO.setProdStatus(spManage.getProdStatus());
				spVO.setSellMoney(spManage.getSellMoney());
				spVO.setProdName(current.getProdName()==null? "":current.getProdName());
				shopCarProdList.add(spVO);
			}
			
			result.setData(shopCarProdList);
		}
		
		return result;
	}

	@Override
	public void statisticsStoreProdInfo(StoreProductManageCondition condition) {
		if (condition != null) {
			logger.error("StoreServiceController -> statisticsStoreProdInfo获取的参数异常！");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		if (null != condition.getStoreId() || StringUtils.isNotBlank(condition.getProdId()) || null != condition.getUpdatedBy()) {
			logger.error("StoreServiceController -> statisticsStoreProdInfo获取的参数异常！");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		storeProductStatisticsService.modifyQuantitySoldOutByStoreIdAndProdId(condition);
	}

	@Override
	public ResponseResult<StoreUserInfoVO> findStoreUserInfoByCustomerId(@RequestParam("customerUserId")Long customerUserId) {
    	ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(customerUserId == null) {
			logger.error("StoreServiceController ->bindCustomer获取的用户id参数为空");
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
	public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("id")Long id) {
		ResponseResult<StoreUserInfoVO> responseResult = new ResponseResult<>();
		if(id == null){
			logger.error("StoreServiceController -> findStoreUserInfo获取门店的id为空");
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
		if(conditions==null||conditions.size()<=0){
			result.setCode(BusinessCode.CODE_1007);
			result.setMessage("参数异常！");
		}
		List<StoreProductStatistics> beanList=new ArrayList<>();
		
		for(StoreProductStatisticsCondition condition:conditions){
			StoreProductStatistics bean=new StoreProductStatistics();
			if(condition.getStoreId()==null||StringUtils.isEmpty(condition.getSkuCode())){
				result.setCode(BusinessCode.CODE_1007);
				result.setMessage("参数异常！");
				return result;
			}
			BeanUtils.copyProperties(condition, bean);
			bean.setCreated(new Date());
			beanList.add(bean);
		}
		storeProductStatisticsService.bathSaveStoreProductStatistics(beanList);
		return result;
	}

}
