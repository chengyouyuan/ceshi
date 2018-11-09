package com.winhxd.b2c.system.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.AppVersionCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.system.service.SysDictItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "系统管理")
@RestController
@RequestMapping(value = "/api-system/system", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiSystemController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiSystemController.class);

    @Autowired
    private SysDictItemService sysDictItemService;

    @Autowired
    private RegionServiceClient regionServiceClient;



    @ApiOperation(value = "检查版本号", notes = "检查版本号")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
                   @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @RequestMapping(value = "/security/3040/v1/appSubmitCheckedVersion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<Integer> appSubmitCheckedVersion(@RequestBody AppVersionCondition appVersionCondition) {
        LOGGER.info("/api-system/3040/v1/appSubmitCheckedVersion接口开始--{}", appVersionCondition);
        if (appVersionCondition == null || StringUtils.isEmpty(appVersionCondition.getAppVersion())) {
            LOGGER.info("app提交的版本号为空");
            throw new BusinessException(BusinessCode.CODE_304001);
        }
        ResponseResult<Integer> result = new ResponseResult<Integer>();
        Integer res = sysDictItemService.checkDictItem(appVersionCondition);
        result.setData(res);
        return result;
    }


    @ApiOperation(value = "API接口-查询地理区域列表")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")})
    @RequestMapping(value = "/security/3041/v1/regionRangeList", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult<List<SysRegion>> findRegionList(@RequestBody SysRegionCondition condition) {
       return regionServiceClient.findRegionList(condition);
    }
}
