package com.winhxd.b2c.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.dao.StoreProductManageMapper;
import com.winhxd.b2c.store.dao.StoreProductStatisticsMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreProductManageService;
/**
 * 门店商品管理Service实现类
 * @ClassName: StoreProductManageServiceImpl 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午3:10:38
 */
@Service
public class StoreProductManageServiceImpl implements StoreProductManageService {
	 private Logger logger = LoggerFactory.getLogger(StoreProductManageServiceImpl.class);
	@Autowired
	private StoreProductManageMapper storeProductManageMapper;
    @Autowired
    private StoreProductStatisticsMapper storeProductStatisticsMapper;
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;

	@Override
	public List<String> findSkusByConditon(StoreProductManageCondition condition) {
		
		return storeProductManageMapper.selectSkusByConditon(condition);
	}

	@Override
	public List<StoreProductManage> findPutawayProdBySkuCodes(Long storeId,String...skuCodes) {

		return storeProductManageMapper.selectPutawayProdBySkuCodes(storeId, skuCodes);
	}

	@Override
	public int countSkusByConditon(StoreProductManageCondition condition) {
		
		return storeProductManageMapper.countSkusByConditon(condition);
	}


	@Override
	@Transactional
	public void batchPutawayStoreProductManage(Long storeId, Map<String, ProdOperateInfoCondition> putawayInfo,
			Map<String, ProductSkuVO> prodSkuInfo) {
		//
		if(storeId!=null&&putawayInfo!=null&&prodSkuInfo!=null){
			//查询门店用户信息
			StoreUserInfo storeUserInfo=storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo==null){
	            logger.error("StoreProductManageService ->batchPutawayStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			for(String skuCode:putawayInfo.keySet()){
				//上架信息（价格，是否推荐（默认0），）
				ProdOperateInfoCondition putaway =putawayInfo.get(skuCode);
				
				StoreProductManage spManage=storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if(spManage==null){
					//产品的信息
					ProductSkuVO prodSku= prodSkuInfo.get(skuCode);
					if(prodSku==null){
						logger.error("查询不到skuCode:"+skuCode+"的商品信息");
						throw new BusinessException(BusinessCode.CODE_1001);
					}
					//不存在需要重新插入
					spManage=new StoreProductManage();
					spManage.setStoreId(storeId);
					spManage.setSkuCode(skuCode);
					spManage.setProdCode(prodSku.getProductCode());
					spManage.setProdStatus((byte)StoreProductStatusEnum.PUTAWAY.getStatusCode());
					spManage.setPutawayTime(new Date());
					//售价
					spManage.setSellMoney(putaway.getSellMoney());
					//是否推荐
					spManage.setRecommend(putaway.getRecommend());
					//prodSku.getSkuAttributeOption()规格
					spManage.setSkuAttributeOption(0);
					spManage.setCreated(new Date());
					spManage.setCreatedBy(storeId);
					//店主名称
					spManage.setCreatedByName(storeUserInfo.getShopkeeper());		
				}else{
					//存在，可能是下架了或则删除
					spManage.setProdStatus((byte)StoreProductStatusEnum.PUTAWAY.getStatusCode());
					spManage.setUpdated(new Date());
					spManage.setUpdatedBy(storeId);
					//店主名称
					spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
					//更新
					storeProductManageMapper.updateByPrimaryKeySelective(spManage);
				}
			}
			
		}else{
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}
}
