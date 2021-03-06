package com.winhxd.b2c.store.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreProdSimpleVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.dao.StoreProductManageMapper;
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
	public List<String> countSkusByConditon(StoreProductManageCondition condition) {
		return storeProductManageMapper.countSkusByConditon(condition);
	}

	@Override
	@Transactional(rollbackFor=BusinessException.class)
	public void batchPutawayStoreProductManage(Long storeId, Map<String, ProdOperateInfoCondition> putawayInfo,
											   Map<String, ProductSkuVO> prodSkuInfo) {
		if (storeId != null && putawayInfo != null && prodSkuInfo != null) {
			//查询门店用户信息
			StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
			if (storeUserInfo == null) {
				logger.error("StoreProductManageService ->batchPutawayStoreProductManage查询store用户信息不存在！");
				throw new BusinessException(BusinessCode.CODE_200004);
			}
			for (String skuCode : putawayInfo.keySet()) {
				//上架信息（价格，是否推荐（默认0），）
				ProdOperateInfoCondition putaway = putawayInfo.get(skuCode);
				StoreProductManage spManage = storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if (spManage == null) {
					//产品的信息
					ProductSkuVO prodSku = prodSkuInfo.get(skuCode);
					if (prodSku == null) {
						logger.error("StoreProductManageService ->batchPutawayStoreProductManage查询不到skuCode:{}的商品信息",skuCode);
						throw new BusinessException(BusinessCode.CODE_1001);
					}
					//不存在，需要重新插入
					spManage = new StoreProductManage();
					spManage.setStoreId(storeId);
					spManage.setSkuCode(skuCode);
					spManage.setProdCode(prodSku.getProductCode());
					spManage.setProdStatus(StoreProductStatusEnum.PUTAWAY.getStatusCode());
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
					spManage.setUpdated(new Date());
					spManage.setUpdatedBy(storeId);
					spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
					//保存门店商品管理信息
					storeProductManageMapper.insert(spManage);
				} else {
					//存在，可能是重复上架，下架了或则删除
					spManage.setProdStatus(StoreProductStatusEnum.PUTAWAY.getStatusCode());
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
					storeProductManageMapper.updateByPrimaryKey(spManage);
				}
			}
		} else {
			logger.error("StoreProductManageService ->batchPutawayStoreProductManage参数异常,storeId:{},putawayInfo:{},prodSkuInfo:{}",storeId,putawayInfo,prodSkuInfo);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
	}

	@Override
	@Transactional(rollbackFor=BusinessException.class)
	public void removeStoreProductManage(Long storeId, String... skuCodes) {
		if(storeId != null && skuCodes != null && skuCodes.length > 0){
			//查询门店用户信息
			StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo == null){
	            logger.error("StoreProductManageService ->removeStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			for(String skuCode : skuCodes){
				StoreProductManage spManage = storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if(spManage == null){
					logger.error("StoreProductManageService ->removeStoreProductManage异常,查询不到storeId:{},skuCode:{}的门店商品管理信息！",storeId,skuCode);
					throw new BusinessException(BusinessCode.CODE_1001);
				}
				spManage.setProdStatus(StoreProductStatusEnum.DELETED.getStatusCode());
				spManage.setUpdated(new Date());
				spManage.setUpdatedBy(storeId);
				//店主名称
				spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
				//更新
				storeProductManageMapper.updateByPrimaryKeySelective(spManage);
			}
		}else{
			logger.error("StoreProductManageService ->removeStoreProductManage参数异常,storeId:{},skuCodes:{}",storeId,skuCodes);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
	}

	@Override
	public void unPutawayStoreProductManage(Long storeId, String... skuCodes) {
		if (storeId != null && skuCodes != null && skuCodes.length > 0) {
			//查询门店用户信息
			StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo == null){
	            logger.error("StoreProductManageService ->unPutawayStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			for(String skuCode : skuCodes){
				StoreProductManage  spManage = storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, skuCode);
				if(spManage == null){
					logger.error("StoreProductManageService ->unPutawayStoreProductManage异常,查询不到storeId:{},skuCode:{}的门店商品管理信息！",storeId,skuCode);
					throw new BusinessException(BusinessCode.CODE_1001);
				}
				spManage.setProdStatus(StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
				spManage.setUpdated(new Date());
				spManage.setUpdatedBy(storeId);
				//店主名称
				spManage.setUpdatedByName(storeUserInfo.getShopkeeper());
				//更新
				storeProductManageMapper.updateByPrimaryKeySelective(spManage);
			}
		}else{
			logger.error("StoreProductManageService ->unPutawayStoreProductManage参数异常,storeId:{},skuCodes:{}",storeId,skuCodes);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
	}

	@Override
	public void modifyStoreProductManage(Long storeId, ProdOperateInfoCondition prodOperateInfo) {
		if(storeId != null && prodOperateInfo != null){
			//查询门店用户信息
			StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo == null){
	            logger.error("StoreProductManageService ->modifyStoreProductManage查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			StoreProductManage  spManage = storeProductManageMapper.selectBySkuCodeAndStoreId(storeId, prodOperateInfo.getSkuCode());
			if (spManage == null) {
				logger.error("StoreProductManageService ->modifyStoreProductManage异常,查询不到storeId:{},skuCode:{}的门店商品管理信息！",storeId,prodOperateInfo.getSkuCode());
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
		} else {
			logger.error("StoreProductManageService ->modifyStoreProductManage参数异常,storeId:{},ProdOperateInfoCondition:{}",storeId,prodOperateInfo);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
	}

	@Override
	public PagedList<StoreProdSimpleVO> findSimpelVOByCondition(StoreProductManageCondition condition) {
		PagedList<StoreProdSimpleVO> list = null;
		if (condition != null) {
			Page<StoreProdSimpleVO> page = storeProductManageMapper.selectVoByCondition(condition);
			list = new PagedList<>();
			list.setPageNo(condition.getPageNo());
			list.setPageSize(condition.getPageSize());
			list.setData(page.getResult());
			list.setTotalRows(page.getTotal());
			return list;
		}else{
			logger.error("StoreProductManageService ->findSimpelVOByCondition参数异常,condition:{}",condition);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public List<StoreProductManage> findProductBySelective(StoreProductManageCondition storeProductManageCondition) {
		return storeProductManageMapper.selectProductBySelective(storeProductManageCondition);
	}

	@Override
	public PagedList<BackStageStoreProdVO> findStoreProdManageList(BackStageStoreProdCondition condition) {
		PagedList<BackStageStoreProdVO> list = null;
		if(condition != null){
			Page<BackStageStoreProdVO> page = storeProductManageMapper.selectBackStageVoByCondition(condition);
			for(BackStageStoreProdVO vo : page.getResult()){
			    if(StoreProductStatusEnum.PUTAWAY.getStatusCode().equals(vo.getProdStatus())){
			        vo.setProdStatusStr(StoreProductStatusEnum.PUTAWAY.getStatusDes());
			    }else if(StoreProductStatusEnum.UNPUTAWAY.getStatusCode().equals(vo.getProdStatus())){
			        vo.setProdStatusStr(StoreProductStatusEnum.UNPUTAWAY.getStatusDes());
			    }else if(StoreProductStatusEnum.DELETED.getStatusCode().equals(vo.getProdStatus())){
			        vo.setProdStatusStr(StoreProductStatusEnum.DELETED.getStatusDes());
                }
			}
			list = new PagedList<>();
			list.setPageNo(condition.getPageNo());
			list.setPageSize(condition.getPageSize());
			list.setData(page.getResult());
			list.setTotalRows(page.getTotal());
			return list;
		}else{
			logger.error("StoreProductManageService ->findStoreProdManageList参数异常,condition:{}",condition);
			throw new BusinessException(BusinessCode.CODE_1007);
		}
	}

	@Override
	public void modifyStoreProdManageByBackStage(AdminUser adminUser,BackStageStoreProdCondition condition) {
		short status = condition.getProdStatus();
		if (StoreProductStatusEnum.DELETED.getStatusCode().equals(status)) {
			logger.error("StoreProductManageService ->modifyStoreProdManageByBackStage删除门店商品信息操作无效！没有权限");
			throw new BusinessException(BusinessCode.CODE_1003);
		}
		//主键
		Long id=condition.getId();
		//查询门店商品信息
		StoreProductManage spm = this.storeProductManageMapper.selectByPrimaryKey(id);
		if (spm == null) {
			logger.error("StoreProductManageService ->modifyStoreProdManageByBackStage查询不到id为：{}的门店商品信息",id);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		//传过来如果状态是下架状态表示：下架--》上架（是个上架操作）与之相反
		if (StoreProductStatusEnum.UNPUTAWAY.getStatusCode().equals(status)) {
			//上架
			spm.setPutawayTime(new Date());
			spm.setProdStatus(StoreProductStatusEnum.PUTAWAY.getStatusCode());
		} else {
			spm.setProdStatus(StoreProductStatusEnum.UNPUTAWAY.getStatusCode());
		}
		spm.setUpdated(new Date());
		spm.setUpdatedBy(adminUser.getId());
		spm.setUpdatedByName(adminUser.getUsername());
		storeProductManageMapper.updateByPrimaryKeySelective(spm);
	}

}
