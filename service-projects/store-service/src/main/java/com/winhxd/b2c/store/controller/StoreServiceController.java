package com.winhxd.b2c.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backStage.store.condition.StoreInfoCondition;
import com.winhxd.b2c.common.domain.backStage.store.vo.StoreVO;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.store.vo.ShopCarProdVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
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
    private Logger logger = LoggerFactory.getLogger(StoreService.class);
    @Override
    public ResponseResult<Void> bindCustomer(Long customerId,Long storeUserId) {
        ResponseResult<Void> result = new ResponseResult<>();
        if(customerId == null) {
            logger.error("StoreServiceController ->bindCustomer获取的用户id参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        if (storeUserId == null) {
            logger.error("StoreServiceController -> bindCustomer获取的门店id参数为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        int status = storeService.bindCustomer(customerId,storeUserId);
        result.setCode(status == 1 ? BusinessCode.CODE_OK : BusinessCode.CODE_200003);
        return result;
    }

    @Override
    public ResponseResult<StoreUserInfoVO> findStoreUserInfo(@PathVariable("storeUserId")Long storeUserId) {
        ResponseResult<StoreUserInfoVO> result = new ResponseResult<>();
        if(storeUserId == null){
            logger.error("StoreServiceController -> findStoreUserInfo获取的参数storeUserId为空");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        StoreUserInfoVO data = storeService.findStoreUserInfo(storeUserId);
        if(data == null){
            result.setCode(BusinessCode.CODE_200004);
        }
        result.setData(data);
        return result;
    }


    @Override
    public ResponseResult<PagedList<StoreVO>> storeList(StoreInfoCondition storeCondition) {
        ResponseResult<PagedList<StoreVO>> responseResult = new ResponseResult<>();
        PagedList<StoreVO> storeVOPagedList = storeService.findStoreUserInfo(storeCondition);
        responseResult.setData(storeVOPagedList);
        return responseResult;
    }

	@Override
	public ResponseResult<List<ShopCarProdVO>> findShopCarProd(List<String> skuCodes, Long storeId) {
		ResponseResult<List<ShopCarProdVO>> result = new ResponseResult<>();
		//参数检验
		if(storeId==null||CollectionUtils.isEmpty(skuCodes)){
			 logger.error("StoreServiceController -> findShopCarProd获取的参数异常！");
	          throw new BusinessException(BusinessCode.CODE_1007);
		}
		//查询门店下商品信息集合--判断数据权限
		StringBuilder skus=new StringBuilder();
		for(int i=0;i<skuCodes.size();i++){
			skus.append(skuCodes.get(i));
			if(i!=skuCodes.size()-1){
				skus.append(",");
			}	
		}
		logger.info("StoreServiceController -> findShopCarProd查询商品skus："+skus.toString());
		//查询有权限的数据
		List<StoreProductManage> storeProds=null;
		
		//查询结果不为空
		if(CollectionUtils.isEmpty(storeProds)){
			List<String> authSkuCode=new ArrayList<>();
			for(StoreProductManage spm:storeProds){
				authSkuCode.add(spm.getSkuCode());
			}
			//调用商品feigin查询商品基本信息
			
		}
		//storeProductManageService.selectSkusByConditon(condition)
		return result;
	}

	@Override
	public ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(Long storeId) {
		ResponseResult<LoginCheckSellMoneyVO> result = new ResponseResult<>();
		//参数检验
		if(storeId==null){
			logger.error("StoreServiceController -> findShopCarProd获取的参数异常！");
	         throw new BusinessException(BusinessCode.CODE_1007);
		}
		return null;
	}

}
