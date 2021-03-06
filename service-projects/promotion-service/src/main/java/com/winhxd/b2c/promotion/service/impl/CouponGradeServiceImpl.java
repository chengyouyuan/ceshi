package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.model.CouponGradeDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponGradeDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponGradeMapper;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author wl
 * @Date 2018/8/8 18:30
 * @Description
 **/
@Service
public class CouponGradeServiceImpl implements CouponGradeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponGradeServiceImpl.class);
     @Autowired
     private CouponGradeMapper couponGradeMapper;
    @Autowired
    private CouponGradeDetailMapper couponGradeDetailMapper;

    @Override
    public CouponGradeVO viewCouponGradeDetail(String id) {
        CouponGradeVO vo = couponGradeMapper.viewCouponGradeDetail(Long.parseLong(id));
        return vo;
    }

    @Override
    public int updateCouponGradeValid(Long id,Long userId,String userName) {
        int count = couponGradeMapper.updateCouponGradeValid(id,userId,userName);
        if(count<=0){
            throw  new BusinessException(BusinessCode.CODE_1001,"设置失败");
        }
        return count;
    }

    @Override
    @Transactional
    public int addCouponGrade(CouponGradeCondition condition) {
        LOGGER.info("添加坎级规则参数:{}",condition);
        int flag = 0;
        CouponGrade couponGrade = new CouponGrade();
        couponGrade.setCode(condition.getCode());
        couponGrade.setName(condition.getName());
        couponGrade.setRemarks(condition.getRemarks());
        couponGrade.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
        couponGrade.setType(condition.getType());
        couponGrade.setCreatedBy(Long.parseLong(condition.getUserId()));
        couponGrade.setCreatedByName(condition.getUserName());
        couponGrade.setCreated(new Date());
        //先插入主表
        long IdKey = couponGradeMapper.insertCouponGrade(couponGrade);
        if (IdKey == 0) {
            throw new BusinessException(BusinessCode.CODE_500005, "坎级规则新建失败");
        }

        if (IdKey != 0) {
            //根据主表主键插入详情表
            CouponGradeDetail couponGradeDetail = new CouponGradeDetail();
            couponGradeDetail.setGradeId(couponGrade.getId());
            couponGradeDetail.setCost(condition.getCost());
            couponGradeDetail.setCount(condition.getCount());
            couponGradeDetail.setDiscounted(condition.getDiscounted());
            couponGradeDetail.setDiscountedAmt(condition.getDiscountedAmt());
            couponGradeDetail.setDiscountedMaxAmt(condition.getDiscountedMaxAmt());
            couponGradeDetail.setReducedAmt(condition.getReducedAmt());
            couponGradeDetail.setReducedType(condition.getReducedType());
            couponGradeDetail.setRemarks(condition.getRemarks());
            couponGradeDetail.setTimes(condition.getTimes());
            couponGradeDetail.setFullGiveAmt(condition.getFullGiveAmt());
            couponGradeDetail.setIncreaseAmt(condition.getIncreaseAmt());
            int n = couponGradeDetailMapper.insert(couponGradeDetail);
            if (n == 0) {
                throw new BusinessException(BusinessCode.CODE_500005, "坎级规则新建失败");
            }
        }
        return flag;
    }

    @Override
    public PagedList<CouponGradeVO> getCouponGradePage(CouponGradeCondition condition) {
        ResponseResult<PagedList<CouponGradeVO>> result= new ResponseResult<PagedList<CouponGradeVO>>();
        PagedList<CouponGradeVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponGradeVO> couponGradeList = couponGradeMapper.getCouponGradePage(condition);

        List<Map<Long, Object>> list = null;
        if (!CollectionUtils.isEmpty(couponGradeList)) {
            List<Long> ids = couponGradeList.stream().map(CouponGradeVO::getId).collect(Collectors.toList());
            list = couponGradeMapper.getRelationCouponGradeCountMap(ids);
        }

        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, Object> resultMap = getCouponGradeMap(list);
            for (CouponGradeVO couponGradeVO : couponGradeList) {
                if (resultMap.containsKey(couponGradeVO.getId())) {
                    Long relTempleteCount = (Long) resultMap.get(couponGradeVO.getId());
                    String templeteCount = relTempleteCount == null ? "0" : String.valueOf(relTempleteCount);
                    couponGradeVO.setRelTempleteCount(templeteCount);
                }
            }
        }

        PageInfo<CouponGradeVO> pageInfo = new PageInfo<>(couponGradeList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    /**
     * 封装返回的数据，组装成所要的数据
     *
     * @param list
     * @return
     */
    public Map<Long, Object> getCouponGradeMap(List<Map<Long, Object>> list) {
        Map<Long, Object> resultMap = new HashMap<>(list.size());
        for (Map<Long, Object> map : list) {
            Long id = (Long) map.get("id");
            Long relTempleteCount = (Long) map.get("relTempleteCount");
            resultMap.put(id, relTempleteCount);
        }
        return resultMap;
    }

    @Override
    public PagedList<GradeTempleteCountVO> findGradeTempleteCountPage(RuleRealationCountCondition condition) {
        ResponseResult<PagedList<GradeTempleteCountVO>> result= new ResponseResult<PagedList<GradeTempleteCountVO>>();
        PagedList<GradeTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<GradeTempleteCountVO> couponGradeCountList = couponGradeMapper.getGradeTempleteCountPage(condition.getId());
        PageInfo<GradeTempleteCountVO> pageInfo = new PageInfo<>(couponGradeCountList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }
}
