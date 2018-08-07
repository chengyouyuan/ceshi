package com.winhxd.b2c.system.region.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCodeCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.system.region.service.SysRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 地理区域控制器
 * @author: zhanglingke
 * @create: 2018-08-02 16:04
 **/
@Api(tags = "地理区域管理")
@RestController
public class SysRegionController implements RegionServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysRegionController.class);

    private static final String MODULE_NAME = "地理区域管理";

    /**
     * 地域级别
     * 1.	省
     * 2.	市
     * 3.	区
     * 4.	县/镇（街道）
     * 5.	村（居委会）
     */
    private static final int PROVINCELEVEL  = 1;
    private static final int CITYLEVEL  = 2;
    private static final int COUNTYLEVEL  = 3;
    private static final int TOWNLEVEL  = 4;
    private static final int VILLAGELEVEL  = 5;

    @Resource
    private SysRegionService sysRegionService;

    @Override
    @ApiOperation(value = "查询地理区域列表")
    public ResponseResult<List<SysRegion>> getRegions(@RequestBody SysRegionCondition condition) {
        logger.info("{} - 查询地理区域列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult result = new ResponseResult<>();
        try {
            //空参数或者行政级别为1时，返回所有省
            if((StringUtils.isBlank(condition.getRegionCode()) && null == condition.getLevel())||condition.getLevel()==1 ){
                //返回省列表
                List<SysRegion> regionList=  sysRegionService.findRegionByLevel(1);//查找省区域列表
                result.setData(regionList);
            }
            //regioncode 不为空时
            if(StringUtils.isNotBlank(condition.getRegionCode())){
                SysRegion region=  sysRegionService.getRegionByCode(condition.getRegionCode());
                SysRegion queryRegion=new SysRegion();
                switch (region.getLevel()) {
                    case PROVINCELEVEL:
                        queryRegion.setProvinceCode(condition.getRegionCode());
                        break;
                    case CITYLEVEL:
                        queryRegion.setCityCode(condition.getRegionCode());
                        break;
                    case COUNTYLEVEL:
                        queryRegion.setCountyCode(condition.getRegionCode());
                        break;
                    case TOWNLEVEL:
                        queryRegion.setTownCode(condition.getRegionCode());
                        break;
                    case VILLAGELEVEL:
                        queryRegion.setVillageCode(condition.getRegionCode());
                        break;
                    default:
                }
                queryRegion.setLevel(condition.getLevel());
                result.setData(sysRegionService.findRegionList(queryRegion));
            }
        } catch (BusinessException e){
            logger.error("{} - 查询地理区域列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 查询地理区域列表失败, 参数：condition={}", MODULE_NAME, condition);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    @ApiOperation(value = "查询指定的地理区域列表")
    public ResponseResult<List<SysRegion>> getRegionsByRange(@RequestBody List<SysRegionCodeCondition> condition) {
        logger.info("{} - 查询指定的地理区域列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<List<SysRegion>> result = new ResponseResult<>();
        try {
             if(null == condition || condition.size() ==0 ){
               return  result;  //返回空  
             }else {
                 List<SysRegion> regionList = sysRegionService.findRegionByCodes(condition);
                 result.setData(regionList);
             }
        } catch (BusinessException e){
            logger.error("{} - 查询指定的地理区域列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 查询指定的地理区域列表失败, 参数：condition={}", MODULE_NAME, condition);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
        return result;
    }

    @Override
    @ApiOperation(value = "查询指定的地理区域")
    public ResponseResult<SysRegion> getRegion(@PathVariable("regisonCode") String regisonCode) {
        logger.info("{} - 查询指定的地理区域, 参数：regisonCode={}", MODULE_NAME, regisonCode);
        ResponseResult<SysRegion> result = new ResponseResult<>();
        SysRegion sysRegion = sysRegionService.getRegionByCode(regisonCode);
        result.setData(sysRegion);
        return  result;
    }
}
