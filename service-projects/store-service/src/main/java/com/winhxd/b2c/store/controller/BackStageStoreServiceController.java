package com.winhxd.b2c.store.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.AdminUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.BackStageModifyStoreCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreSubmitProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreSubmitProduct;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import com.winhxd.b2c.store.service.StoreProductManageService;
import com.winhxd.b2c.store.service.StoreService;
import com.winhxd.b2c.store.service.StoreSubmitProductService;

/**
 * Created by caiyulong on 2018/8/6.
 * @author caiyulong
 */
@RestController
public class BackStageStoreServiceController implements BackStageStoreServiceClient {

    @Autowired
    private StoreService storeService;
    @Autowired
    private StoreProductManageService storeProductManageService;
	@Autowired
	private ProductServiceClient productServiceClient;
	@Autowired
	private StoreSubmitProductService storeSubmitProductService;
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
    public ResponseResult<Integer> modifyStoreInfoRegionCode(@RequestBody BackStageModifyStoreCondition condition) {
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        StoreUserInfo storeUserInfo = new StoreUserInfo();
        BeanUtils.copyProperties(condition, storeUserInfo);
        storeService.updateRegionCodeByCustomerId(storeUserInfo);
        return  responseResult;
    }

    @Override
    public ResponseResult<List<String>> findStoreIdListByRegionCodes(@RequestBody BackStageStoreInfoCondition condition) {
        ResponseResult<List<String>> responseResult = new ResponseResult<>();
        List<String> ids = storeService.findByRegionCodes(condition.getRegionCodeList());
        responseResult.setData(ids);
        return responseResult;
    }

	@Override
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreProdVO>> responseResult=new ResponseResult<>();
		 AdminUser adminUser=UserContext.getCurrentAdminUser();
		 
		 if(adminUser==null){
			 responseResult=new ResponseResult<>(BusinessCode.CODE_1002);
			 return responseResult;
		 }
		if(condition!=null){
			//商品名称
			if(StringUtils.isNotEmpty(condition.getProdName())){
				StoreProductManageCondition spmCondition=new StoreProductManageCondition();
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
					&&prodResult.getData()!=null){
			    List<ProductSkuVO> finalProdVoList=prodResult.getData();
			    List<BackStageStoreProdVO> finalBSSPVoList=resultVO.getData();
				for(int i=0;i<finalProdVoList.size();i++){
				    System.err.println("skuCode:"+finalProdVoList.get(i).getSkuCode());
				    for(int j=0;j<finalBSSPVoList.size();j++){
				        if(finalBSSPVoList.get(j).getSkuCode().equals(finalProdVoList.get(i).getSkuCode())){
				            finalBSSPVoList.get(j).setProdName(finalProdVoList.get(i).getSkuName());
				            finalBSSPVoList.get(j).setSkuImage(finalProdVoList.get(i).getSkuImage());
		                   
				        }
				    }
					
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
		 AdminUser adminUser=UserContext.getCurrentAdminUser();
		 
		 if(adminUser==null){
			 responseResult=new ResponseResult<>(BusinessCode.CODE_1002);
			 return responseResult;
		 }
		 
		PagedList<BackStageStoreProdVO> resultVO=storeProductManageService.findStoreProdManageList(condition);
		if(resultVO!=null&&resultVO.getData()!=null&&resultVO.getData().size()>0){
			BackStageStoreProdVO vo=resultVO.getData().get(0);
			ProductCondition prodCondition=new ProductCondition();
			prodCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
			List<String> finalSkuCodes=new ArrayList<>(1);
			finalSkuCodes.add(vo.getSkuCode());
			prodCondition.setProductSkus(finalSkuCodes);
			ResponseResult<List<ProductSkuVO>>  prodResult=productServiceClient.getProductSkus(prodCondition);
			if(prodResult!=null&&prodResult.getCode()==0
					&&prodResult.getData()!=null&&prodResult.getData().size()==finalSkuCodes.size()){
				vo.setProdName(prodResult.getData().get(0).getSkuName());
				vo.setSkuImage(prodResult.getData().get(0).getSkuImage());
			}
			responseResult.setData(vo);
		}
		return responseResult;
	}

	@Override
	public ResponseResult<Void> operateStoreProdManage(@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<Void> responseResult = new ResponseResult<>();
		AdminUser adminUser = UserContext.getCurrentAdminUser();

		if (adminUser == null) {
			responseResult = new ResponseResult<>(BusinessCode.CODE_1002);
			return responseResult;
		}
		if (condition == null || condition.getProdStatus() == null || condition.getId() == null) {
			responseResult.setCode(BusinessCode.CODE_1007);
			responseResult.setMessage("参数无效！");
		}

		storeProductManageService.modifyStoreProdManageByBackStage(condition);

		return responseResult;
	}

	@Override
	public ResponseResult<PagedList<BackStageStoreSubmitProdVO>> findStoreSubmitProdList(
	        @RequestBody BackStageStoreSubmitProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreSubmitProdVO>> responseResult = null;
		AdminUser adminUser = UserContext.getCurrentAdminUser();

		if (adminUser == null) {
			responseResult = new ResponseResult<>(BusinessCode.CODE_1002);
			return responseResult;
		}
		if (condition != null) {
			responseResult = new ResponseResult<>();
			PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
			PagedList<BackStageStoreSubmitProdVO> list = this.storeSubmitProductService
					.findBackStageVOByCondition(condition);
			responseResult.setData(list);
		} else {
			responseResult = new ResponseResult<>(BusinessCode.CODE_1007);
		}
		return responseResult;
	}

	@Override
	public ResponseResult<BackStageStoreSubmitProdVO> findStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition) {
		ResponseResult<BackStageStoreSubmitProdVO> responseResult = null;
		AdminUser adminUser = UserContext.getCurrentAdminUser();

		if (adminUser == null) {
			responseResult = new ResponseResult<>(BusinessCode.CODE_1002);
			return responseResult;
		}
		if (condition != null) {
			responseResult = new ResponseResult<>();
			PagedList<BackStageStoreSubmitProdVO> list = this.storeSubmitProductService
					.findBackStageVOByCondition(condition);
			if (list != null && list.getData() != null && list.getData().size() > 0) {
				responseResult.setData(list.getData().get(0));
			}

		} else {
			responseResult = new ResponseResult<>(BusinessCode.CODE_1007);
		}
		return responseResult;
	}

    @Override
    public ResponseResult<Void> modifyStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition) {
        ResponseResult<Void> responseResult = null;
        AdminUser adminUser = UserContext.getCurrentAdminUser();
        //校验权限
        if (adminUser == null) {
            responseResult = new ResponseResult<>(BusinessCode.CODE_1002);
            return responseResult;
        }
        
        if(condition!=null&&condition.getProdStatus()!=null&&condition.getId()!=null){
            //主键
            Long id=condition.getId();
            //状态
            Short status=condition.getProdStatus();
            //提报商品信息
            StoreSubmitProduct ssp=storeSubmitProductService.findById(id);
            if(ssp==null){
                responseResult = new ResponseResult<>(BusinessCode.CODE_1001);
                return responseResult;
            }
            if(StoreSubmitProductStatusEnum.NOTPASS.getStatusCode()==status||
                    StoreSubmitProductStatusEnum.PASS.getStatusCode()==status){
                ssp.setAuditRemark(condition.getAuditRemark());  
            }else if(StoreSubmitProductStatusEnum.ADDPROD.getStatusCode()==status){
                String skuCode=condition.getSkuCode(); 
                if(StringUtils.isBlank(skuCode)){
                    responseResult = new ResponseResult<>(BusinessCode.CODE_1001);
                    return responseResult;
                }else{
                    ProductCondition pCondition=new ProductCondition();
                    pCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                    List<String> skuCodes=new ArrayList<>();
                    skuCodes.add(skuCode);
                    pCondition.setProductSkus(skuCodes);
                    ResponseResult<List<ProductSkuVO>> pResult= productServiceClient.getProductSkus(pCondition);
                    if(pResult!=null&&pResult.getCode()==0&&pResult.getData()!=null
                            &&pResult.getData().size()>0){
                        //查询对应的sku信息
                        ProductSkuVO skuVO=pResult.getData().get(0);
                        ssp.setSkuCode(skuVO.getSkuCode());
                        ssp.setProdCode(skuVO.getProductCode());
                        ssp.setSkuAttributeOption(skuVO.getSkuAttributeOption());
                        ssp.setProdName(skuVO.getSkuName());
                    }else{
                        responseResult = new ResponseResult<>(BusinessCode.CODE_1001);
                        return responseResult;  
                    }
                }
            }else{
                //无该操作权限
                responseResult = new ResponseResult<>(BusinessCode.CODE_1001);
                return responseResult;  
            }
            
            ssp.setProdStatus(status);
            
            storeSubmitProductService.modifyStoreSubmitProductByAdmin(adminUser, ssp);
            //this.storeSubmitProductService.modifyStoreSubmitProductByAdmin(adminUser, storeSubmitProduct);
        }
        return responseResult;
    }

}
