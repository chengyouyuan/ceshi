package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCodeCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 地理区域服务
 * @author: zhanglingke
 * @create: 2018-08-06 11:26
 **/
@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = RegionServiceClientFallback.class)
public interface RegionServiceClient {

    /**
     * 功能描述: 获取地理区域列表
     * @auther: zhanglingke
     * @date: 2018-08-06 11:46
     * @param: SysRegionCondition
     * @return:
     */
    @RequestMapping(value = "/api/region/310/v1/list", method = RequestMethod.POST)
    ResponseResult<List<SysRegion>> getRegions(@RequestBody SysRegionCondition condition);
    /**
     * 功能描述: 根据指定地理区域编码获取地理区域列表
     * @auther: zhanglingke
     * @date: 2018-08-06 11:46
     * @param: SysRegionCodeCondition
     * @return:
     */
    @RequestMapping(value = "/api/region/311/v1/rangeList", method = RequestMethod.POST)
    @ResponseBody
    ResponseResult<List<SysRegion>> getRegionsByRange(@RequestBody List<SysRegionCodeCondition> condition);

    /**
     * 功能描述: 根据指定地理区域编码获取单个地理区域
     * @auther: zhanglingke
     * @date: 2018-08-06 11:46
     * @param: SysRegionCodeCondition
     * @return:
     */
    @RequestMapping(value = "/api/region/312/v1/rangeList", method = RequestMethod.GET)
    ResponseResult<SysRegion> getRegion(@RequestParam String regisonCode);
}

@Component
class RegionServiceClientFallback implements RegionServiceClient, FallbackFactory<RegionServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(RegionServiceClientFallback.class);
    private Throwable throwable;

    @Override
    public RegionServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new RegionServiceClientFallback();
    }

    @Override
    public ResponseResult<List<SysRegion>> getRegions(SysRegionCondition condition) {
        logger.error("RegionServiceClientFallback -> getRegions，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<SysRegion>> getRegionsByRange(List<SysRegionCodeCondition> condition) {
        logger.error("RegionServiceClientFallback -> getRegionsByRange，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysRegion> getRegion(String regisonCode) {
        logger.error("RegionServiceClientFallback -> getRegion，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
