package com.winhxd.b2c.admin.module.store.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreProdVO;

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

	@ApiOperation("查询门店商品管理信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1042/v1/findStoreProdManageList")
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			BackStageStoreProdCondition condition) {

		return null;
	}

	@ApiOperation("查询门店商品管理单个信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1043/v1/findStoreProdManage")
	public ResponseResult<BackStageStoreProdVO> findStoreProdManage(BackStageStoreProdCondition condition) {

		return null;
	}

	@ApiOperation("操作门店商品管理单个信息（上下架）")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/1044/v1/operateStoreProdManage")
	public ResponseResult<Void> operateStoreProdManage(BackStageStoreProdCondition condition) {

		return null;
	}
}
