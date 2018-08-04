package com.winhxd.b2c.store.api;

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
import com.winhxd.b2c.common.domain.product.vo.ProductMsgVO;
import com.winhxd.b2c.common.domain.store.condition.AllowPutawayProdCondition;
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
	 
	    @ApiOperation(value = "B端获取可上架商品数据接口", response = ResponseResult.class, notes = "B端获取可上架商品数据接口")
	    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
	            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
	    @PostMapping(value = "1000/allowPutwayProdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	    public ResponseResult<ProductMsgVO> allowPutwayProdList(@RequestBody AllowPutawayProdCondition condition) {
	        ResponseResult<ProductMsgVO> responseResult = new ResponseResult<>();
	        try {
	            logger.info("B端获取可上架商品数据接口入参为：{}", condition);
	            responseResult.setData(null);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return responseResult;
	    }

	 
}
