package com.winhxd.b2c.admin.module.store.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.utils.ExcelUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.StoreRegionCondition;
import com.winhxd.b2c.common.domain.store.vo.StoreRegionVO;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: wangbaokuo
 * @date: 2018/8/10 14:48
 */
@Api(tags = "门店管理、测试区域配置")
@RestController
@CheckPermission(PermissionEnum.STORE_MANAGEMENT_REGION)
public class StoreRegionController {

    private static final Logger logger = LoggerFactory.getLogger(StoreRegionController.class);

    @Autowired
    private StoreServiceClient storeServiceClient;

    @ApiOperation("查询测试区域配置")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")
    })
    @PostMapping(value = "/store/findStoreRegions")
    @CheckPermission(PermissionEnum.STORE_MANAGEMENT_REGION_LIST)
    public ResponseResult<PagedList<StoreRegionVO>> findStoreRegions(@RequestBody StoreRegionCondition condition){
        ResponseResult<PagedList<StoreRegionVO>> result = storeServiceClient.findStoreRegions(condition);
        return result;
    }

    @ApiOperation("门店测试区域导出")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")
    })
    @PostMapping(value = "/store/storeRegionsExport")
    @CheckPermission(PermissionEnum.STORE_MANAGEMENT_REGION_LIST)
    public ResponseEntity<byte[]> storeRegionsExport(@RequestBody StoreRegionCondition condition) {
        condition.setIsQueryAll(true);
        ResponseResult<PagedList<StoreRegionVO>> responseResult = storeServiceClient.findStoreRegions(condition);
        if (responseResult != null && responseResult.getCode() == 0) {
            List<StoreRegionVO> list = responseResult.getData().getData();
            return ExcelUtils.exp(list, "门店测试区域管理");
        }
        return null;
    }

    @ApiOperation("删除测试区域配置")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")
    })
    @GetMapping(value = "/store/removeStoreRegion/{id}")
    @CheckPermission(PermissionEnum.STORE_MANAGEMENT_REGION_DEL)
    public ResponseResult<Void> removeStoreRegion(@PathVariable("id") Long id){
        storeServiceClient.removeStoreRegion(id);
        return new ResponseResult<>();
    }
    @ApiOperation("保存测试区域配置")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")
    })
    @PostMapping(value = "/store/saveStoreRegion")
    @CheckPermission(PermissionEnum.STORE_MANAGEMENT_REGION_ADD)
    public ResponseResult<Void> saveStoreRegion(@RequestBody StoreRegionCondition condition){
        storeServiceClient.saveStoreRegion(condition);
        return new ResponseResult<>();
    }

}
