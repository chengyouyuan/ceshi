package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionCondition;
import com.winhxd.b2c.common.domain.system.region.condition.SysRegionPagedCondition;
import com.winhxd.b2c.common.domain.system.region.model.SysRegion;
import com.winhxd.b2c.common.domain.system.region.vo.SysRegionVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RegionServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.constant.ErrorConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.lang.model.type.ErrorType;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum.*;
import static com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum.VILLAGELEVEL;

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
    public ResponseResult<List<SysRegionVO>> findRegionList(@RequestBody SysRegionCondition condition) {
        ResponseResult<List<SysRegionVO>> result=new ResponseResult<>();
        List<SysRegionVO>  list= new ArrayList<>();

        List<SysRegion> regionList = userServiceClient.findRegionList(condition).getData();
        for (SysRegion region:regionList) {
            SysRegionVO regionVO = new SysRegionVO();
            BeanUtils.copyProperties(region, regionVO);
            regionVO.setFullName(String.format("%s%s%s%s%s",regionVO.getProvince(),regionVO.getCity(),region.getCounty(),region.getTown(),region.getVillage()));
            list.add(regionVO);
        }
        result.setData(list);
        return result;
    }

    @ApiOperation("根据条件筛选所有地理区域，支持分页，模糊查询")
    @PostMapping(value = "/filterlist")
    public ResponseResult<PagedList<SysRegionVO>>  findRegionByPage(@RequestBody SysRegionPagedCondition condition) {
        ResponseResult<PagedList<SysRegionVO>> result=new ResponseResult<>();
        PagedList<SysRegionVO> pagedList = new PagedList<>();
        List<SysRegionVO> list = new ArrayList<>();

        PagedList<SysRegion> regionResult = userServiceClient.findRegionByPage(condition).getData();
        if(regionResult == null){
            throw new BusinessException(BusinessCode.CODE_3020001,"查询的地理区域不存在");
        }
        List<SysRegion> regionList = regionResult.getData();
        for (SysRegion region:regionList) {
            SysRegionVO regionVO=new SysRegionVO();
            BeanUtils.copyProperties(region, regionVO);
            regionVO.setFullName(String.format("%s%s%s%s%s",regionVO.getProvince(),regionVO.getCity(),region.getCounty(),region.getTown(),region.getVillage()));
            list.add(regionVO);
        }
        pagedList.setPageSize(regionResult.getPageSize());
        pagedList.setPageNo(regionResult.getPageNo());
        pagedList.setTotalRows(regionResult.getTotalRows());
        pagedList.setData(list);
        result.setData(pagedList);
        return result;
    }
}
