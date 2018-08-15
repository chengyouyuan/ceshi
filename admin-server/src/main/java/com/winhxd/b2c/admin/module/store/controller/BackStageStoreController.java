package com.winhxd.b2c.admin.module.store.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.store.condition.BackStageModifyStoreCondition;
import com.winhxd.b2c.common.domain.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.store.backstage.BackStageStoreServiceClient;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created by caiyulong on 2018/8/4.
 */
@Api(tags = "后台门店账户管理")
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
    @PostMapping(value = "/1020/v1/findStoreList")
    public ResponseResult<PagedList<BackStageStoreVO>> findStoreList(@RequestBody BackStageStoreInfoCondition storeInfoCondition) {
        logger.info("{} - 门店账户列表, 参数：storeInfoCondition={}", MODULE_NAME, storeInfoCondition.toString());
        ResponseResult<PagedList<BackStageStoreVO>> responseResult = backStageStoreServiceClient.findStoreList(storeInfoCondition);
        logger.info("{} - 门店账户列表, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "查询门店账户详细信息接口", notes = "查询门店账户详细信息接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @ApiParam()
    @PostMapping(value = "/1021/v1/getStoreInfoById/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<BackStageStoreVO> getStoreInfoById(@PathVariable("id") Long id) {
        ResponseResult<BackStageStoreVO> responseResult = new ResponseResult<>();
        logger.info("查询门店账户详细信息接口入参为：{}", id);
        BackStageStoreVO backStageStoreVO = backStageStoreServiceClient.getStoreInfoById(id).getData();
        responseResult.setData(backStageStoreVO);
        logger.info("查询门店账户详细信息接口返参为：{}", JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "编辑门店保存接口", notes = "编辑门店保存接口")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数错误！")})
    @PostMapping(value = "/1022/v1/modifyStoreInfo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> modifyStoreInfo(@RequestBody BackStageModifyStoreCondition condition) {
        if (condition.getId() == null || StringUtils.isBlank(condition.getStoreName()) ||
                StringUtils.isBlank(condition.getStoreAddress()) || StringUtils.isBlank(condition.getShopkeeper()) ||
                StringUtils.isBlank(condition.getContactMobile())) {
            logger.error("编辑门店保存接口，参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        ResponseResult<Integer> responseResult = new ResponseResult<>();
        logger.info("编辑门店保存接口入参为：{}", condition.toString());
        backStageStoreServiceClient.modifyStoreInfo(condition);
        return responseResult;
    }

    @ApiOperation(value = "获取地域列表", notes = "获取地域列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1023/v1/regionCodeList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult regionCodeList(@RequestBody SysRegionCondition condition) {
        ResponseResult<List<SysRegion>> responseResult = new ResponseResult<>();
        try {
            logger.info("{} - #后台-门店##地理区域列表, 参数：condition={}", MODULE_NAME, JsonUtil.toJSONString(condition));
            responseResult = regionServiceClient.findRegionList(condition);
            logger.info("{} - #后台-门店##地理区域列表, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        } catch (Exception e) {
            logger.error("#后台-门店##地理区域列表，服务器内部错误：{}", e);
            responseResult.setCode(BusinessCode.CODE_1001);
            responseResult.setMessage("服务器内部错误！");
        }
        return responseResult;
    }

    @ApiOperation(value = "更改门店区域信息", notes = "更改门店区域信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1024/v1/updateReginCode", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> updateReginCode(@RequestBody BackStageModifyStoreCondition condition) {
        logger.info("{} - 更改门店区域信息, 参数：condition={}", MODULE_NAME, JsonUtil.toJSONString(condition));
        ResponseResult<Integer> responseResult = backStageStoreServiceClient.modifyStoreInfoReginCode(condition);
        logger.info("{} - 更改门店区域信息, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }

    @ApiOperation(value = "根据区域集合查询门店", notes = "根据区域集合查询门店")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误！")})
    @PostMapping(value = "/1051/v1/findStoreIdList", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<String>> findStoreIdList(@RequestBody BackStageStoreInfoCondition condition) {
        logger.info("{} - 根据区域集合查询门店, 参数：condition={}", MODULE_NAME, JsonUtil.toJSONString(condition));
        ResponseResult<List<String>> responseResult = new ResponseResult<>();
        if (condition.getRegionCodeList() == null || condition.getRegionCodeList().isEmpty()) {
            return responseResult;
        }
        responseResult = backStageStoreServiceClient.findStoreIdListByReginCodes(condition);
        logger.info("{} - 根据区域集合查询门店, 返参：{}", MODULE_NAME, JsonUtil.toJSONString(responseResult));
        return responseResult;
    }


}
