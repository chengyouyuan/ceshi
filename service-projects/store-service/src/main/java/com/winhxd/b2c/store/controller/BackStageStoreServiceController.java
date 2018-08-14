package com.winhxd.b2c.store.controller;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageModifyStoreCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caiyulong on 2018/8/6.
 */
@RestController
public class BackStageStoreServiceController implements BackStageStoreServiceClient {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreProductManageService storeProductManageService;
	@Autowired
	private ProductServiceClient productServiceClient;
    @Override
    public ResponseResult<PagedList<BackStageStoreVO>> findStoreList(@RequestBody BackStageStoreInfoCondition storeCondition) {
        ResponseResult<PagedList<BackStageStoreVO>> responseResult = new ResponseResult<>();
        PagedList<BackStageStoreVO> storeVOPagedList = storeService.findStoreUserInfo(storeCondition);
        responseResult.setData(storeVOPagedList);
        return responseResult;
    }

    @Override
    public ResponseResult<BackStageStoreVO> getStoreInfoById(Long id) {
        ResponseResult<BackStageStoreVO> responseResult = new ResponseResult<>();
        BackStageStoreVO backStageStoreVO = storeService.findByIdForBackStage(id);
        responseResult.setData(backStageStoreVO);
        return responseResult;
    }

    @Override
    public ResponseResult<Integer> modifyStoreInfo(@RequestBody BackStageModifyStoreCondition condition) {
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        BeanUtils.copyProperties(condition, storeUserInfo);
        storeService.updateByPrimaryKeySelective(storeUserInfo);
        return new ResponseResult<>();
    }

    @Override
    public ResponseResult<Integer> modifyStoreInfoReginCode(BackStageModifyStoreCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        BeanUtils.copyProperties(condition, storeUserInfo);
        storeService.updateReginCodeByCustomerId(storeUserInfo);
        return  responseResult;
    }

    @Override
    public ResponseResult<List<String>> findStoreIdListByReginCodes(@RequestBody BackStageStoreInfoCondition condition) {
        ResponseResult<List<String>> responseResult = new ResponseResult<>();
        List<String> ids = storeService.findByReginCodes(condition.getRegionCodeList());
        responseResult.setData(ids);
        return responseResult;
    }

	@Override
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreProdVO>> responseResult=new ResponseResult<>();
		if(condition!=null&&condition.getStoreId()!=null){
			Long storeId=condition.getStoreId();

			//商品名称
			if(StringUtils.isNotEmpty(condition.getProdName())){
				StoreProductManageCondition spmCondition=new StoreProductManageCondition();
				spmCondition.setStoreId(storeId);
				//获取该门店用户下所有的sku
				List<String> skuCodeList=storeProductManageService.findSkusByConditon(spmCondition);
				if(skuCodeList!=null){
					ProductCondition prodCondition=new ProductCondition();
					prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
					prodCondition.setProductSkus(skuCodeList);
					prodCondition.setProductName(condition.getProdName());
					//调取商品接口查询商品名称相似的sku（在改门店所有sku集合当中）
					ResponseResult<List<ProductSkuVO>>  prodResult=productServiceClient.getProductSkus(prodCondition);
					if(prodResult.getCode()!=0){
						//异常
						responseResult.setCode(prodResult.getCode());
						responseResult.setMessage(prodResult.getMessage());
						return responseResult;
					}else{
						List<ProductSkuVO> skuVOList=prodResult.getData();
						if(skuVOList==null||skuVOList.size()<=0){
							responseResult.setMessage("查询不到数据！");
							return responseResult;
						}else{
							//查询到名称一样的商品SKU
							List<String> resultSkus=new ArrayList<>();
							for(ProductSkuVO psVO:skuVOList){
								resultSkus.add(psVO.getSkuCode());
							}

							condition.setSkuCodeList(resultSkus);
						}
					}
				}
			}
			PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
			//最终查询
			PagedList<BackStageStoreProdVO> resultVO=storeProductManageService.findStoreProdManageList(condition);
			List<String> finalSkuCodes=new ArrayList<>(resultVO.getData().size());
			for(BackStageStoreProdVO vo:resultVO.getData()){
				finalSkuCodes.add(vo.getSkuCode());
			}
			ProductCondition prodCondition=new ProductCondition();
			prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
			prodCondition.setProductSkus(finalSkuCodes);
			ResponseResult<List<ProductSkuVO>>  prodResult=productServiceClient.getProductSkus(prodCondition);
			if(prodResult!=null&&prodResult.getCode()==0
					&&prodResult.getData()!=null&&prodResult.getData().size()==finalSkuCodes.size()){
				for(int i=0;i<prodResult.getData().size();i++){
					resultVO.getData().get(i).setProdName(prodResult.getData().get(i).getSkuName());
				}
			}
			responseResult.setData(resultVO);
		}else{
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数无效");
		}
		return responseResult;
	}

	@Override
	public ResponseResult<BackStageStoreProdVO> findStoreProdManage(@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<BackStageStoreProdVO> responseResult=new ResponseResult<>();
		PagedList<BackStageStoreProdVO> resultVO=storeProductManageService.findStoreProdManageList(condition);
		if(resultVO!=null&&resultVO.getData()!=null&&resultVO.getData().size()>0){
			responseResult.setData(resultVO.getData().get(0));
		}
		return responseResult;
	}

	@Override
	public ResponseResult<Void> operateStoreProdManage(@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<Void> responseResult=new ResponseResult<>();
		if(condition==null||condition.getProdStatus()==null||condition.getId()==null){
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数无效！");
		}
		
		storeProductManageService.modifyStoreProdManageByBackStage(condition);
		
		return responseResult;
	}

}
