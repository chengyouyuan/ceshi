package com.winhxd.b2c.admin.module.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * 后台门店商品管理Controller
 * 
 * @ClassName: StoreProdManageController
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月13日 上午11:19:49
 */
@Api(tags = "门店商品管理")
@RestController
public class StoreProdManageController {
	private static final Logger logger = LoggerFactory.getLogger(StoreProdManageController.class);
	@Autowired
	private BackStageStoreServiceClient backStageStoreServiceClient;
	@ApiOperation("查询门店商品管理信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1042/v1/findStoreProdManageList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreProdVO>> result=new ResponseResult<>();
		condition.setDeleted(1);
		result=backStageStoreServiceClient.findStoreProdManageList(condition);
		return result;
	}

	@ApiOperation("查询门店商品管理单个信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1043/v1/findStoreProdManage/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<BackStageStoreProdVO> findStoreProdManage(@PathVariable("id") Long id) {
		BackStageStoreProdCondition condition=new BackStageStoreProdCondition();
		condition.setId(id);
		condition.setDeleted(1);
		ResponseResult<BackStageStoreProdVO> result=new ResponseResult<>();
		result=backStageStoreServiceClient.findStoreProdManage(condition);
		return result;
	}

	@ApiOperation("操作门店商品管理单个信息（上下架）")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1044/v1/operateStoreProdManage",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseResult<Void> operateStoreProdManage(BackStageStoreProdCondition condition) {
		ResponseResult<Void> result=new ResponseResult<>();
		result=backStageStoreServiceClient.operateStoreProdManage(condition);
		return result;
	}
}
