package com.winhxd.b2c.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backStage.store.condition.StoreInfoCondition;
import com.winhxd.b2c.common.domain.backStage.store.vo.StoreVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.LoginCheckSellMoneyVO;
import com.winhxd.b2c.common.domain.store.vo.ShopCarProdVO;
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
//    @Autowired
//    private ProductServiceClient productServiceClient;
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
		//查询有权限的数据
		List<StoreProductManage> storeProds=this.storeProductManageService.findProdBySkuCodes(storeId, (String[])skuCodes.toArray());
		
		//查询结果不为空
		if(CollectionUtils.isEmpty(storeProds)){
			List<String> authSkuCode=new ArrayList<>();
			for(StoreProductManage spm:storeProds){
				authSkuCode.add(spm.getSkuCode());
			}
			//调用商品feigin查询商品基本信息
			List<ProductSkuVO> productSkuList=null;
			//productServiceClient.
			List<ShopCarProdVO> shopCarProdList=new ArrayList<>();
			if(CollectionUtils.isEmpty(productSkuList)){
				 logger.error("StoreServiceController -> findShopCarProd 调用ProductServiceClient 异常！");
		         throw new BusinessException(BusinessCode.CODE_1001);
			}
			for(int i=0;i<productSkuList.size();i++){
				ShopCarProdVO spVO=new ShopCarProdVO();
				//sku信息
				ProductSkuVO current=productSkuList.get(i);
				//门店与sku关系
				StoreProductManage spManage=storeProds.get(i);
				spVO.setSkuCode(current.getSkuCode());
				spVO.setProdImage(current.getSkuImage());
				spVO.setProdStatus(spManage.getProdStatus());
				spVO.setSellMoney(spManage.getSellMoney());
				shopCarProdList.add(spVO);
			}
			
			result.setData(shopCarProdList);
		}
		
		return result;
	}

	@Override
	public ResponseResult<LoginCheckSellMoneyVO> loginCheckSellMoney(Long storeId) {
		ResponseResult<LoginCheckSellMoneyVO> result = new ResponseResult<>();
		LoginCheckSellMoneyVO vo=new LoginCheckSellMoneyVO();
		//参数检验
		if(storeId==null){
			logger.error("StoreServiceController -> findShopCarProd获取的参数异常！");
	         throw new BusinessException(BusinessCode.CODE_1007);
		}
		vo.setStoreId(storeId);
		//查询上架未设置价格商品
		StoreProductManageCondition condition=new StoreProductManageCondition();
		condition.setStoreId(storeId);
		//未设置价格
		condition.setPriceStatus((byte)0);
		//上架商品
		condition.setProdStatus((byte)StoreProductStatusEnum.PUTAWAY.getStatusCode());
		int count=storeProductManageService.countSkusByConditon(condition);
		//设置是否有未设置价格的商品
		if(count>0){
			vo.setCheckResult(true);	
		}else{
			vo.setCheckResult(false);
		}
		//设置为设置价格商品的数量
		vo.setNoSetPriceCount(count);
		
		result.setData(vo);
		return result;
	}

}
