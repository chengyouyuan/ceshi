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
import com.winhxd.b2c.common.domain.store.enums.StoreSubmitProductStatusEnum;
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
		//查询门店用户信息
		StoreUserInfo storeUserInfo = storeUserInfoMapper.selectByPrimaryKey(storeId);
		storeSubmitProduct.setCreated(new Date());
		storeSubmitProduct.setStoreName(storeUserInfo.getStoreName());
		storeSubmitProductMapper.insertSelective(storeSubmitProduct);
	}

	@Override
	public void modifyStoreSubmitProductByAdmin(AdminUser adminUser, StoreSubmitProduct storeSubmitProduct) {
		if(storeSubmitProduct.getId()>0){
			storeSubmitProduct.setUpdated(new Date());
			storeSubmitProduct.setUpdatedBy(adminUser.getId());
			storeSubmitProduct.setUpdatedByName(adminUser.getUsername());
			storeSubmitProductMapper.updateByPrimaryKeySelective(storeSubmitProduct);
		}else{
			logger.error("StoreSubmitProductService ->modifyStoreSubmitProductByAdmin提报商品ID为空");
			throw new BusinessException(BusinessCode.CODE_1001);
		}
	}

	@Override
	public PagedList<StoreSubmitProductVO> findSimpelVOByCondition(StoreSubmitProductCondition condition) {
		Page<StoreSubmitProductVO> page = storeSubmitProductMapper.selectVoByCondition(condition);
		PagedList<StoreSubmitProductVO> list = new PagedList<>();
		list.setData(page.getResult());
		list.setTotalRows(page.getTotal());
		list.setPageNo(condition.getPageNo());
		list.setPageSize(condition.getPageSize());
		return list;
	}

	@Override
	public PagedList<BackStageStoreSubmitProdVO> findBackStageVOByCondition(
			BackStageStoreSubmitProdCondition condition) {
		PagedList<BackStageStoreSubmitProdVO> list = null;
		Page<BackStageStoreSubmitProdVO> page = storeSubmitProductMapper.selectBackStageVOByCondition(condition);
		if(page.getResult() != null){
			for(BackStageStoreSubmitProdVO p : page.getResult()){
				short status=p.getProdStatus();
				if (StoreSubmitProductStatusEnum.CREATE.getStatusCode() == status) {
					p.setProdStatusStr(StoreSubmitProductStatusEnum.CREATE.getStatusDes());
				} else if (StoreSubmitProductStatusEnum.PASS.getStatusCode() == status) {
					p.setProdStatusStr(StoreSubmitProductStatusEnum.PASS.getStatusDes());
				} else if (StoreSubmitProductStatusEnum.NOTPASS.getStatusCode() == status) {
					p.setProdStatusStr(StoreSubmitProductStatusEnum.NOTPASS.getStatusDes());
				} else if(StoreSubmitProductStatusEnum.ADDPROD.getStatusCode() == status) {
					p.setProdStatusStr(StoreSubmitProductStatusEnum.ADDPROD.getStatusDes());
				}
			}
		}
		list = new PagedList<>();
		list.setPageNo(condition.getPageNo());
		list.setPageSize(condition.getPageSize());
		list.setData(page.getResult());
		list.setTotalRows(page.getTotal());
		return list;
	}

    @Override
    public StoreSubmitProduct findById(Long id) {
        return storeSubmitProductMapper.selectByPrimaryKey(id);
    }

}
