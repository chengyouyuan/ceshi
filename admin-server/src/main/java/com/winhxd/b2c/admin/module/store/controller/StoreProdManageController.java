package com.winhxd.b2c.admin.module.store.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreProdCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreProdVO;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
	@PostMapping(value = "/store/findStoreProdManageList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CheckPermission({PermissionEnum.PROD_MANAGEMENT_STORE})
	public ResponseResult<PagedList<BackStageStoreProdVO>> findStoreProdManageList(
			@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<PagedList<BackStageStoreProdVO>> result=new ResponseResult<>();
		condition.setDeleted(1);
		result=backStageStoreServiceClient.findStoreProdManageList(condition);
		return result;
	}

	@ApiOperation(value = "门店商品管理导出Excel")
	@RequestMapping("/store/storeProdManageListExport")
	public ResponseEntity<byte[]> storeProdManageListExport(@RequestBody BackStageStoreProdCondition condition) {
		condition.setIsQueryAll(true);
		condition.setDeleted(1);
		ResponseResult<PagedList<BackStageStoreProdVO>> responseResult = backStageStoreServiceClient.findStoreProdManageList(condition);
		if (responseResult != null && responseResult.getCode() == 0) {
			List<BackStageStoreProdVO> list = responseResult.getData().getData();
			return ExcelUtils.exp(list, "门店提现申请");
		}
		return null;
	}

	@ApiOperation("查询门店商品管理单个信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/findStoreProdManage/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CheckPermission({PermissionEnum.PROD_MANAGEMENT_STORE})
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
	@PostMapping(value = "/store/operateStoreProdManage",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CheckPermission({PermissionEnum.PROD_MANAGEMENT_STORE_PUTAWAY})
	public ResponseResult<Void> operateStoreProdManage(@RequestBody BackStageStoreProdCondition condition) {
		ResponseResult<Void> result=new ResponseResult<>();
		result=backStageStoreServiceClient.operateStoreProdManage(condition);
		return result;
	}
}
