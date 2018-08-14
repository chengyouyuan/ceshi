package com.winhxd.b2c.system.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionPagedCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import com.winhxd.b2c.system.service.SysRegionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum.*;

/**
 * @description: 地理区域控制器
 * @author: zhanglingke
 * @create: 2018-08-02 16:04
 **/
@Api(tags = "地理区域管理")
@RestController
@RequestMapping
public class SysRegionController implements RegionServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysRegionController.class);

    private static final String MODULE_NAME = "地理区域管理";

    @Resource
    private SysRegionService sysRegionService;

    @Override
    @ApiOperation(value = "查询地理区域列表")
    public ResponseResult<List<SysRegion>> findRegionList(@RequestBody SysRegionCondition condition) {
        logger.info("{} - 查询地理区域列表, 参数：condition={}", MODULE_NAME, condition);
        List<SysRegion> regionList=null;
        ResponseResult<List<SysRegion>> result = new ResponseResult<>();
        //空参数或者行政级别为1时，返回所有省
        if((StringUtils.isBlank(condition.getRegionCode()) && null == condition.getLevel())||PROVINCELEVEL.getCode().equals(condition.getLevel())){
            //查找省区域列表
             regionList=  sysRegionService.findRegionByLevel(PROVINCELEVEL);
        }else if(StringUtils.isNotBlank(condition.getRegionCode())){    //regioncode 不为空时
            SysRegion region=  sysRegionService.getRegionByCode(condition.getRegionCode());
            if(region==null){
                throw  new BusinessException(BusinessCode.CODE_3020001,"查询的地理区域不存在");
            }
            SysRegion queryRegion=new SysRegion();
            if(PROVINCELEVEL.getCode().equals(region.getLevel())){
                queryRegion.setProvinceCode(condition.getRegionCode());
            } else if(CITYLEVEL.getCode().equals(region.getLevel())){
                queryRegion.setCityCode(condition.getRegionCode());
            }else if(COUNTYLEVEL.getCode().equals(region.getLevel())){
                queryRegion.setCountyCode(condition.getRegionCode());
            } else if(TOWNLEVEL.getCode().equals(region.getLevel())){
                queryRegion.setTownCode(condition.getRegionCode());
            } else if(VILLAGELEVEL.getCode().equals(region.getLevel())){
                queryRegion.setVillageCode(condition.getRegionCode());
            }
            queryRegion.setLevel(condition.getLevel());
            regionList=sysRegionService.findRegionList(queryRegion);
        }
        result.setData(regionList);
        return result;
    }

    @Override
    @ApiOperation(value = "查询指定的地理区域列表")
    @ApiParam(name="地理区域编号数组",value="地理区域编号数组")
    public ResponseResult<List<SysRegion>> findRegionRangeList(@RequestBody List<String> regisonCodes) {
        logger.info("{} - 查询指定的地理区域列表, 参数：condition={}", MODULE_NAME, regisonCodes);
        ResponseResult<List<SysRegion>> result = new ResponseResult<>();
        if(null == regisonCodes || regisonCodes.size() ==0 ){
            return  result;  //返回空
        }else {
            List<SysRegion> regionList = sysRegionService.findRegionByCodes(regisonCodes);
            result.setData(regionList);
        }
        return result;
    }

    @Override
    @ApiOperation(value = "查询指定的地理区域")
    public ResponseResult<SysRegion> getRegionByCode(@PathVariable("regisonCode") String regisonCode) {
        logger.info("{} - 查询指定的地理区域, 参数：regisonCode={}", MODULE_NAME, regisonCode);
        ResponseResult<SysRegion> result = new ResponseResult<>();
        if(StringUtils.isNotBlank(regisonCode)){
            SysRegion sysRegion = sysRegionService.getRegionByCode(regisonCode);
            result.setData(sysRegion);
        }
        return  result;
    }

    @Override
    @ApiOperation(value = "模糊搜索所有地理区域")
    public ResponseResult<PagedList<SysRegion>> findRegionByPage(@RequestBody SysRegionPagedCondition condition) {
        logger.info("{} - 模糊搜索所有地理区域, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<PagedList<SysRegion>> result = new ResponseResult<>();
        PagedList<SysRegion> resData= sysRegionService.findRegionByPage(condition);
        result.setData(resData);  ;
        return  result;
    }
}
