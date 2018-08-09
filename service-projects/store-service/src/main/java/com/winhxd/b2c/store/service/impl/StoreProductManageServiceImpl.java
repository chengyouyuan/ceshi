package com.winhxd.b2c.store.service.impl;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.dao.StoreProductManageMapper;
import com.winhxd.b2c.store.dao.StoreProductStatisticsMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreProductManageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
		
		return (int)storeProductManageMapper.countSkusByConditon(condition);
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
						logger.error("StoreProductManageService ->batchPutawayStoreProductManage查询不到skuCode:"+skuCode+"的商品信息");
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
					//规格
					spManage.setSkuAttributeOption(prodSku.getSkuAttributeOption());
					spManage.setCreated(new Date());
					spManage.setCreatedBy(storeId);
					//店主名称
					spManage.setCreatedByName(storeUserInfo.getShopkeeper());
					//保存门店商品管理信息
					storeProductManageMapper.insert(spManage);
				
				}else{
					//存在，可能是下架了或则删除
					spManage.setProdStatus((byte)StoreProductStatusEnum.PUTAWAY.getStatusCode());
					spManage.setUpdated(new Date());
					spManage.setUpdatedBy(storeId);
					//上架日期
					spManage.setPutawayTime(new Date());
					//售价
					spManage.setSellMoney(putaway.getSellMoney());
					//是否推荐
					spManage.setRecommend(putaway.getRecommend());
					//店主名称
					spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
					//更新
					storeProductManageMapper.updateByPrimaryKeySelective(spManage);
				}
			}
			
		}else{
			logger.error("StoreProductManageService ->batchPutawayStoreProductManage参数异常,storeId:"+storeId+",putawayInfo:"
								+putawayInfo+",prodSkuInfo:"+prodSkuInfo);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	@Transactional
	public void removeStoreProductManage(Long storeId, String... skuCodes) {
		if(storeId!=null&&skuCodes!=null&&skuCodes.length>0){
			//查询门店用户信息
			StoreUserInfo storeUserInfo=storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo==null){
	            logger.error("StoreProductManageService ->removeStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			for(String skuCode:skuCodes){
				StoreProductManage  spManage=storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if(spManage==null){
					logger.error("StoreProductManageService ->removeStoreProductManage异常,查询不到storeId:"+storeId+",skuCode:"+skuCode+"的门店商品管理信息！");
					throw new BusinessException(BusinessCode.CODE_1001);
				}
				spManage.setProdStatus((byte)StoreProductStatusEnum.DELETED.getStatusCode());
				spManage.setUpdated(new Date());
				spManage.setUpdatedBy(storeId);
				//店主名称
				spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
				//更新
				storeProductManageMapper.updateByPrimaryKeySelective(spManage);
			}
		}else{
			logger.error("StoreProductManageService ->removeStoreProductManage参数异常,storeId:"+storeId+",skuCodes:"+skuCodes);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public void unPutawayStoreProductManage(Long storeId, String... skuCodes) {
		if(storeId!=null&&skuCodes!=null&&skuCodes.length>0){
			//查询门店用户信息
			StoreUserInfo storeUserInfo=storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo==null){
	            logger.error("StoreProductManageService ->unPutawayStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			for(String skuCode:skuCodes){
				StoreProductManage  spManage=storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if(spManage==null){
					logger.error("StoreProductManageService ->unPutawayStoreProductManage异常,查询不到storeId:"+storeId+",skuCode:"+skuCode+"的门店商品管理信息！");
					throw new BusinessException(BusinessCode.CODE_1001);
				}
				spManage.setProdStatus((byte)StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
				spManage.setUpdated(new Date());
				spManage.setUpdatedBy(storeId);
				//店主名称
				spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
				//更新
				storeProductManageMapper.updateByPrimaryKeySelective(spManage);
			}
		}else{
			logger.error("StoreProductManageService ->unPutawayStoreProductManage参数异常,storeId:"+storeId+",skuCodes:"+skuCodes);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public void modifyStoreProductManage(Long storeId, ProdOperateInfoCondition prodOperateInfo) {
		if(storeId!=null&&prodOperateInfo!=null){
			//查询门店用户信息
			StoreUserInfo storeUserInfo=storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo==null){
	            logger.error("StoreProductManageService ->modifyStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			StoreProductManage  spManage=storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, prodOperateInfo.getSkuCode());
			if(spManage==null){
				logger.error("StoreProductManageService ->modifyStoreProductManage异常,查询不到storeId:"+storeId+",skuCode:"+prodOperateInfo.getSkuCode()+"的门店商品管理信息！");
				throw new BusinessException(BusinessCode.CODE_1001);
			}
			spManage.setUpdated(new Date());
			spManage.setUpdatedBy(storeId);
			//店主名称
			spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
			spManage.setSellMoney(prodOperateInfo.getSellMoney());
			spManage.setRecommend(prodOperateInfo.getRecommend());
			//更新
			storeProductManageMapper.updateByPrimaryKeySelective(spManage);
		}else{
			logger.error("StoreProductManageService ->modifyStoreProductManage参数异常,storeId:"+storeId+",ProdOperateInfoCondition:"+prodOperateInfo);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public PagedList<StoreProdSimpleVO> findSimpelVOByCondition(StoreProductManageCondition condition) {
		PagedList<StoreProdSimpleVO> list=null;
		if(condition!=null){
			Page<StoreProdSimpleVO> page=storeProductManageMapper.selectVoByCondition(condition);
			list=new PagedList<>();
			list.setPageNo(condition.getPageNo());
			list.setPageSize(condition.getPageSize());
			list.setData(page.getResult());
			list.setTotalRows(page.getTotal());
			return list;
		}else{
			logger.error("StoreProductManageService ->findSimpelVOByCondition参数异常,condition:"+condition);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public Boolean queryRecommendFlag(Long storeId) {
		Integer count = storeProductManageMapper.queryRecommendFlag(storeId);
		if (count != null && count > 0){
			return true;
		}
		return false;
	}

}
