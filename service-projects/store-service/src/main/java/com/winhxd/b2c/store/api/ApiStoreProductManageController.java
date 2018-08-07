package com.winhxd.b2c.store.api;

import java.util.ArrayList;
import java.util.List;

import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.condition.AllowPutawayProdCondition;
import com.winhxd.b2c.common.domain.store.condition.ProdOperateCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreProductManageCondition;
import com.winhxd.b2c.common.domain.store.condition.StoreSubmitProductCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreProdOperateEnum;
import com.winhxd.b2c.common.domain.store.enums.StoreProductStatusEnum;
import com.winhxd.b2c.common.domain.store.model.StoreProductManage;
import com.winhxd.b2c.common.domain.store.vo.StoreSubmitProductVO;
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
@RequestMapping(value = "api/store/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiStoreProductManageController {
	 private static final Logger logger = LoggerFactory.getLogger(ApiStoreProductManageController.class);
	 @Autowired
	 private StoreProductManageService storeProductManageService;
	 @Autowired
	 private ProductServiceClient productServiceClient;
	 /**
	  * 惠下单商品
	  */
	 private static final Byte HXD_PROD_TYPE = 0;
	 /**
	  * 普通商品
	  */
	 private static final Byte COMMON_PROD_TYPE=1;
	 
	    @ApiOperation(value = "B端获取可上架商品数据接口", response = ResponseResult.class, notes = "B端获取可上架商品数据接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1012/allowPutawayProdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<ProductMsgVO> allowPutawayProdList(@RequestBody AllowPutawayProdCondition condition) {
	        ResponseResult<ProductMsgVO> responseResult = new ResponseResult<>();
	        try {
	            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
	            //参数校验
	            if(!checkParam(condition)){
	            	responseResult.setCode(BusinessCode.CODE_1007);
	            	responseResult.setMessage("参数错误");
	            	return responseResult;
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
//	            responseResult=productServiceClient.getProductMsg(prodCondition);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return responseResult;
	    }
	    
	    @ApiOperation(value = "B端商品操作接口(上架(包括批量)，下架，删除，编辑)", response = ResponseResult.class, notes = "B端商品操作接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1013/storeProdOperate", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<Void> storeProdOperate(@RequestBody ProdOperateCondition condition) {
	    	 ResponseResult<Void> responseResult = new ResponseResult<>();
	    	 try{
	    		 logger.info("B端商品操作接口入参为：{}", condition);
	    		 //参数校验
		         if(!checkParam(condition)){
		            responseResult.setCode(BusinessCode.CODE_1007);
		            responseResult.setMessage("参数错误");
		            return responseResult;
		         }
		         //获取当前门店用户
		         StoreUser storeUser=UserContext.getCurrentStoreUser();
		         //操作类型
		         Byte operateType=condition.getOperateType();
		         //门店id
		         Long storeId=condition.getStoreId();
		         //skuCode数组
		         String skuCodes[]=null;
		         //获取sku信息
		         List<StoreProductManage> spms=storeProductManageService.findProdBySkuCodes(storeId,skuCodes);
		         //判断数据权限
		         if(CollectionUtils.isEmpty(spms)&&spms.size()==skuCodes.length){
		        	 //判断操作类型
			         if(StoreProdOperateEnum.PUTAWAY.getOperateCode()==operateType){
			        	 //上架操作
			        	 //表示还没上架过
			        	 if(spms==null||spms.size()==0){
			        		 //调用商品接口获取商品相关信息
			        		 List<ProductSkuVO> prodSkuList=null;
			        		 //保存StoreProductManage
			        		 //初始化门店商品对应统计信息
			        		 
			        		 
			        		 
			        		 
			        	 }else{
			        		 //表示商品中有部分已经上架过了
			        		 
			        		 return responseResult;
			        	 }
			        	 
			         }else if(StoreProdOperateEnum.UNPUTAWAY.getOperateCode()==operateType){
			        	//下架操作
			        	 
			         }else if(StoreProdOperateEnum.DELETE.getOperateCode()==operateType){
			        	 //删除操作
			        	 
			         }else if(StoreProdOperateEnum.EDIT.getOperateCode()==operateType){
			        	//编辑
			        	 
			         }
		         }else{
			            responseResult.setCode(BusinessCode.CODE_1003);
			            responseResult.setMessage("没有权限");
			            return responseResult; 
		         }
		        
	    		 
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	    	 
	    	 return responseResult;
	    }
	    
	    @ApiOperation(value = "B端添加门店提报商品接口", response = ResponseResult.class, notes = "B端添加门店提报商品接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1014/saveStoreSubmitProduct", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<Void> saveStoreSubmitProduct(@RequestBody StoreSubmitProductCondition condition) {
	    	 ResponseResult<Void> responseResult = new ResponseResult<>();
	    	 try{
	    		 
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	    	 
	    	 return responseResult;
	    }
	    
	    @ApiOperation(value = "B端门店提报商品列表接口", response = ResponseResult.class, notes = "B端门店提报商品列表接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1015/findStoreSubmitProductList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<PagedList<StoreSubmitProductVO>> findStoreSubmitProductList(@RequestBody StoreSubmitProductCondition condition) {
	    	 ResponseResult<PagedList<StoreSubmitProductVO>> responseResult = new ResponseResult<>();
	    	 try{
	    		 
	    	 }catch(Exception e){
	    		 e.printStackTrace();
	    	 }
	    	 
	    	 return responseResult;
	    }
	    
	    @PostMapping(value = "1111/test", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<Object> test(@RequestBody StoreProductManageCondition condition) {
	        ResponseResult<Object> responseResult = new ResponseResult<>();
	        try {
	            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
	            List<String> list=new ArrayList<>();
	            list.add("987654321");
	            list.add("123456789");
	            responseResult.setData(storeProductManageService.findProdBySkuCodes(1L, (String[])list.toArray()));
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return responseResult;
	    }
	    
	    private boolean checkParam(Object condition){
	    	boolean flag=false;
	    	if(condition!=null){
	    		//AllowPutawayProdCondition 必须传参数校验
	    		if(condition instanceof AllowPutawayProdCondition){
		    		AllowPutawayProdCondition c=(AllowPutawayProdCondition)condition;
			    		if(c.getStoreId()!=null&&c.getProdType()!=null){
			    			flag=true;
			    		}	
		    	}
	    		//AllowPutawayProdCondition 必须传参数校验
		    	if(condition instanceof ProdOperateCondition){
		    		ProdOperateCondition c=(ProdOperateCondition)condition;
		    		if(c.getStoreId()!=null&&c.getOperateType()!=null
		    				&&c.getProducts()!=null&&c.getProducts().size()>0){
		    			flag=true;
		    		}
		    	}
	    	}
	    	
	    	
	    	return flag;
	    }

	 
}
