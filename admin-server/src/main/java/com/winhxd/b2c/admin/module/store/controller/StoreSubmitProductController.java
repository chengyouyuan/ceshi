package com.winhxd.b2c.admin.module.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 后台提报商品controller
 * @ClassName: StoreSubmitProductController 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月14日 下午3:48:27
 */
public class StoreSubmitProductController {
	private static final Logger logger = LoggerFactory.getLogger(StoreSubmitProductController.class);
	@Autowired
	private BackStageStoreServiceClient backStageStoreServiceClient;
	
	@ApiOperation("查询门店提报商品信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1052/v1/findStoreSubmitProdList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PagedList<BackStageStoreSubmitProdVO>> findStoreSubmitProdList(
			@RequestBody BackStageStoreSubmitProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreSubmitProdVO>> result=new ResponseResult<>();
		result=backStageStoreServiceClient.findStoreSubmitProdList(condition);
		return result;
	}
	
	@ApiOperation("查询门店提报商品单个信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1053/v1/findStoreSubmitProd/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<BackStageStoreSubmitProdVO> findStoreSubmitProd(@PathVariable("id") Long id) {
		BackStageStoreSubmitProdCondition condition=new BackStageStoreSubmitProdCondition();
		condition.setId(id);
		ResponseResult<BackStageStoreSubmitProdVO> result=new ResponseResult<>();
		result=backStageStoreServiceClient.findStoreSubmitProd(condition);
		return result;
	}
	
	


}
