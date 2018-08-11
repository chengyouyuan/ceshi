package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "地理区域管理")
@RestController
@RequestMapping("/region")
public class RegionController {
    private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

    //private static final String MODULE_NAME = "地理区域管理";
    @Resource
    private RegionServiceClient userServiceClient;

    @ApiOperation("获取地理区域列表")
    @PostMapping(value = "/list")
    public ResponseResult<List<SysRegion>> findRegionList(@RequestBody SysRegionCondition condition) {
        return userServiceClient.findRegionList(condition);
    }
}
