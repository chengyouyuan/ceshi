package com.winhxd.b2c.admin.module.backstage.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by caiyulong on 2018/8/4.
 */
@Api(value = "后台门店账户管理")
@RestController
@RequestMapping("/store")
public class BackStageStoreController {

    private static final Logger logger = LoggerFactory.getLogger(BackStageStoreController.class);

    private static final String MODULE_NAME = "后台-门店管理";

    @Autowired
    private BackStageStoreServiceClient backStageStoreServiceClient;

    @Autowired
    private RegionServiceClient regionServiceClient;

    @ApiOperation("门店账户列表")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @PostMapping(value = "/1000/v1/storeList")
    public ResponseResult<PagedList<BackStageStoreVO>> storeList(@RequestBody BackStageStoreInfoCondition storeInfoCondition) {
        ResponseResult<PagedList<BackStageStoreVO>> responseResult = new ResponseResult<>();
        try {
            logger.info("{} - #后台-门店##门店账户列表, 参数：storeInfoCondition={}", MODULE_NAME, storeInfoCondition.toString());
            responseResult = backStageStoreServiceClient.storeList(storeInfoCondition);
            logger.info("{} - #后台-门店##门店账户列表, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("#后台-门店##查看门店列表，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "查询门店账户详细信息接口", notes = "查询门店账户详细信息接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1000/v1/getStoreInfoById", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<BackStageStoreVO> getStoreInfoById(@RequestParam("id") Long id) {
        ResponseResult<BackStageStoreVO> responseResult = new ResponseResult<>();
        try {
            logger.info("查询门店账户详细信息接口入参为：{}", id);
            responseResult.setData(new BackStageStoreVO());
            logger.info("查询门店账户详细信息接口返参为：{}", JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("查询门店账户详细信息接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "编辑门店保存接口", response = ResponseResult.class, notes = "编辑门店保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200002, message = "storeId参数为空！", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_200004, message = "门店信息不存在！", response = ResponseResult.class)})
    @PostMapping(value = "1000/v1/modifyStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult modifyStoreInfo(@RequestBody BackStageStoreInfoCondition openStoreCondition) {
        if (openStoreCondition.getCustomerId() == null) {
            logger.error("编辑门店保存接口 modifyStoreInfo,customerId参数为空");
            throw new BusinessException(BusinessCode.CODE_200001);
        }
        ResponseResult responseResult = new ResponseResult();
        try {
            logger.info("编辑门店保存接口入参为：{}", JsonUtil.toJSONString(openStoreCondition));
        } catch (Exception e) {
            logger.error("编辑门店保存接口，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "获取地域列表", response = ResponseResult.class, notes = "获取地域列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功", response = ResponseResult.class),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！", response = ResponseResult.class)})
    @PostMapping(value = "1000/v1/modifyStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult reginCodeList(@RequestBody SysRegionCondition condition) {
        ResponseResult<List<SysRegion>> responseResult  = new ResponseResult<>();
        try {
            logger.info("{} - #后台-门店##地理区域列表, 参数：condition={}", MODULE_NAME, condition.toString());
            responseResult = regionServiceClient.getRegions(condition);
            logger.info("{} - #后台-门店##地理区域列表, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("#后台-门店##地理区域列表，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }


}
