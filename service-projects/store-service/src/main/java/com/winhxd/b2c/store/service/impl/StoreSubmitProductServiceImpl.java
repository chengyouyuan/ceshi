package com.winhxd.b2c.store.service.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.store.dao.StoreSubmitProductMapper;
import com.winhxd.b2c.store.dao.StoreUserInfoMapper;
import com.winhxd.b2c.store.service.StoreSubmitProductService;
/**
 * 提报商品service实现类
 * @ClassName: StoreSubmitProductServiceImpl 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月8日 下午6:12:59
 */
@Service
public class StoreSubmitProductServiceImpl implements StoreSubmitProductService {
	private Logger logger = LoggerFactory.getLogger(StoreProductManageServiceImpl.class);
	@Autowired
	private StoreSubmitProductMapper storeSubmitProductMapper;
    @Autowired
    private StoreUserInfoMapper storeUserInfoMapper;
	
	@Override
	public void saveStoreSubmitProduct(Long storeId, StoreSubmitProduct storeSubmitProduct) {
		
		if(storeId!=null&&storeSubmitProduct!=null){
			//查询门店用户信息
			StoreUserInfo storeUserInfo=storeUserInfoMapper.selectByPrimaryKey(storeId);
			if(storeUserInfo==null){
	            logger.error("StoreSubmitProductService ->saveStoreSubmitProduct查询store用户信息不存在！");
	            throw new BusinessException(BusinessCode.CODE_200004);
			}
			storeSubmitProduct.setCreated(new Date());
			storeSubmitProductMapper.insertSelective(storeSubmitProduct);
		}else{
			logger.error("StoreSubmitProductService ->saveStoreSubmitProduct参数异常,storeId:"+storeId+",StoreSubmitProduct:"+storeSubmitProduct);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public void modifyStoreSubmitProductByStore(Long storeId, StoreSubmitProduct storeSubmitProduct) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void modifyStoreSubmitProductByAdmin(AdminUser adminUser, StoreSubmitProduct storeSubmitProduct) {
		if(adminUser!=null&&storeSubmitProduct!=null){
			if(storeSubmitProduct.getId()>0){
				storeSubmitProduct.setUpdated(new Date());
				storeSubmitProduct.setUpdatedBy(adminUser.getId());
				storeSubmitProduct.setUpdatedByName(adminUser.getUsername());
				storeSubmitProductMapper.updateByPrimaryKeySelective(storeSubmitProduct);
			}else{
				logger.error("StoreSubmitProductService ->modifyStoreSubmitProductByAdmin提报商品ID为空");
				throw new BusinessException(BusinessCode.CODE_1001);
			}
		
		}else{
			logger.error("StoreSubmitProductService ->modifyStoreSubmitProductByAdmin参数异常,adminUser:"+adminUser+",storeSubmitProduct:"+storeSubmitProduct);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public PagedList<StoreSubmitProductVO> findSimpelVOByCondition(StoreSubmitProductCondition condition) {
		if(condition!=null){
			Page<StoreSubmitProductVO> page=storeSubmitProductMapper.selectVoByCondition(condition);
			PagedList<StoreSubmitProductVO> list=new PagedList<>();
			list.setData(page.getResult());
			list.setTotalRows(page.getTotal());
			list.setPageNo(condition.getPageNo());
			list.setPageSize(condition.getPageSize());
			return list;
		}else{
			logger.error("StoreSubmitProductService ->findSimpelVOByCondition参数异常,condition:"+condition);
			throw new BusinessException(BusinessCode.CODE_1001);
		}
		
	}

	@Override
	public PagedList<BackStageStoreSubmitProdVO> findBackStageVOByCondition(
			BackStageStoreSubmitProdCondition condition) {
		PagedList<BackStageStoreSubmitProdVO> list=null;
		if(condition!=null){
	
			Page<BackStageStoreSubmitProdVO> page=storeSubmitProductMapper.selectBackStageVOByCondition(condition);
			list=new PagedList<>();
			list.setPageNo(condition.getPageNo());
			list.setPageSize(condition.getPageSize());
			list.setData(page.getResult());
			list.setTotalRows(page.getTotal());
			return list;
		}else{
			logger.error("StoreSubmitProductService ->findBackStageVOByCondition参数异常,condition:"+condition);
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		
	}

}
