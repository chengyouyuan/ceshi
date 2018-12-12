package com.winhxd.b2c.admin.module.store.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreSubmitProdCondition;
import com.winhxd.b2c.common.domain.store.enums.StoreSubmitProductStatusEnum;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreSubmitProdVO;
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
 * 后台提报商品controller
 * @ClassName: StoreSubmitProductController 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月14日 下午3:48:27
 */
@Api(tags = "门店提报商品管理")
@RestController
public class StoreSubmitProductController {
	private static final Logger logger = LoggerFactory.getLogger(StoreSubmitProductController.class);
	@Autowired
	private BackStageStoreServiceClient backStageStoreServiceClient;
	
	@ApiOperation("查询门店提报商品信息")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
	@PostMapping(value = "/store/findStoreSubmitProdList",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CheckPermission({PermissionEnum.PROD_MANAGEMENT_SUBMIT})
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
	@PostMapping(value = "/store/findStoreSubmitProd/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@CheckPermission({PermissionEnum.PROD_MANAGEMENT_SUBMIT})
	public ResponseResult<BackStageStoreSubmitProdVO> findStoreSubmitProd(@PathVariable("id") Long id) {
		BackStageStoreSubmitProdCondition condition=new BackStageStoreSubmitProdCondition();
		condition.setId(id);
		ResponseResult<BackStageStoreSubmitProdVO> result=new ResponseResult<>();
		result=backStageStoreServiceClient.findStoreSubmitProd(condition);
		return result;
	}
	
	@ApiOperation("审核门店提报商品信息")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
    @PostMapping(value = "/store/auditStoreSubmitProd",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission({PermissionEnum.PROD_MANAGEMENT_SUBMIT})
    public ResponseResult<Void> auditStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition) {
        ResponseResult<Void> result=new ResponseResult<>();
        result=backStageStoreServiceClient.modifyStoreSubmitProd(condition);
        return result;
    }
	
    @ApiOperation("门店提报商品去添加商品")
    @ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效") })
    @PostMapping(value = "/store/addProdStoreSubmitProd", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @CheckPermission({ PermissionEnum.PROD_MANAGEMENT_SUBMIT })
    public ResponseResult<Void> addProdStoreSubmitProd(@RequestBody BackStageStoreSubmitProdCondition condition) {
        ResponseResult<Void> result = new ResponseResult<>();
        condition.setProdStatus(StoreSubmitProductStatusEnum.ADDPROD.getStatusCode());
        result=backStageStoreServiceClient.modifyStoreSubmitProd(condition);
        return result;
    }

	@ApiOperation(value = "门店提报商品管理导出Excel")
	@RequestMapping("/store/storeSubmitProdListExport")
	public ResponseEntity<byte[]> storeSubmitProdListExport(@RequestBody BackStageStoreSubmitProdCondition condition) {
		condition.setIsQueryAll(true);
		ResponseResult<PagedList<BackStageStoreSubmitProdVO>> responseResult = backStageStoreServiceClient.findStoreSubmitProdList(condition);
		if (responseResult != null && responseResult.getCode() == 0) {
			List<BackStageStoreSubmitProdVO> list = responseResult.getData().getData();
			return ExcelUtils.exp(list, "门店提报商品管理");
		}
		return null;
	}
	


}
