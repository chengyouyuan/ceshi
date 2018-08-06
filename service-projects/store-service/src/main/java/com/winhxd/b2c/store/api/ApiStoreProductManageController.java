package com.winhxd.b2c.store.api;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.store.condition.AllowPutawayProdCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.store.service.StoreProductManageService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
/**
 * 惠小店门店商品相关接口
 * @ClassName: ApiStoreProductManageController 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月4日 下午2:58:37
 */
@Api(value = "api storeProductManage", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
@RequestMapping(value = "api/storeProductManage/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreProductManageController {
	 private static final Logger logger = LoggerFactory.getLogger(ApiStoreProductManageController.class);
	 @Autowired
	 private StoreProductManageService storeProductManageService;
//	 @Autowired
//	 private ProductServiceClient productServiceClient;
	 /**
	  * 惠下单商品
	  */
	 private static final Byte HXD_PROD_TYPE=0;
	 /**
	  * 普通商品
	  */
	 private static final Byte COMMON_PROD_TYPE=1;
	 
	    @ApiOperation(value = "B端获取可上架商品数据接口", response = ResponseResult.class, notes = "B端获取可上架商品数据接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1000/allowPutawayProdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<ProductMsgVO> allowPutawayProdList(@RequestBody AllowPutawayProdCondition condition) {
	        ResponseResult<ProductMsgVO> responseResult = new ResponseResult<>();
	        try {
	            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
	            //参数校验
	            if(!checkParam(condition)){
	            	responseResult.setCode(BusinessCode.CODE_1007);
	            	responseResult.setMessage("参数错误");
	            }
	            //账号信息校验
	            
	            //判断查询可上架商品类型
	            Byte prodType=condition.getProdType();
	            Long storeId=condition.getStoreId();
	            //在惠下单下过单的sku
	            List<String> hxdProdSkus=null;
	            List<String> putawayProdSkus=null;
	            //惠下单商品
	            if(HXD_PROD_TYPE.equals(prodType)){
	            	
	            	//调用接口请求该用户惠下单商品sku	
	            }
	            //该门店上架sku集合
	            StoreProductManageCondition spmCondition=new StoreProductManageCondition();
	            spmCondition.setStoreId(storeId);
	            spmCondition.setProdStatus((byte)StoreProductStatusEnum.PUTAWAY.getStatusCode());
	            putawayProdSkus=storeProductManageService.selectSkusByConditon(spmCondition);
	            
	            //查询商品信息
	            ProductCondition prodCondition=new ProductCondition();
	            //首次请求，会返回类型，品牌等信息
//	            if(condition.getFirst()){
//	            	 productServiceClient.getProductMsg(prodCondition);
//	            }
//	            productServiceClient.getProducts(prodCondition);
	            responseResult.setData(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return responseResult;
	    }
	    
	    @PostMapping(value = "1111/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<List<String>> test(@RequestBody StoreProductManageCondition condition) {
	        ResponseResult<List<String>> responseResult = new ResponseResult<>();
	        try {
	            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
	            responseResult.setData(storeProductManageService.selectSkusByConditon(condition));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return responseResult;
	    }
	    
	    private boolean checkParam(AllowPutawayProdCondition condition){
	    	boolean flag=false;
	    	if(condition!=null){
	    		if(condition.getStoreId()!=null&&condition.getProdType()!=null){
	    			flag=true;
	    		}
	    	}
	    	return flag;
	    }

	 
}
