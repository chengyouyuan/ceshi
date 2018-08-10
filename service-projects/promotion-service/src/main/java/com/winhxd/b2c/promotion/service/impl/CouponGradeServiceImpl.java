package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.model.CouponGradeDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.promotion.dao.CouponGradeDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponGradeMapper;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/8 18:30
 * @Description
 **/
@Service
public class CouponGradeServiceImpl implements CouponGradeService {
     @Autowired
     private CouponGradeMapper couponGradeMapper;
    @Autowired
    private CouponGradeDetailMapper couponGradeDetailMapper;

    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(long id) {
        ResponseResult responseResult = new ResponseResult();
        CouponGradeVO vo = couponGradeMapper.viewCouponGradeDetail(id);
        responseResult.setData(vo);
        return responseResult;
    }

    @Override
    public int updateCouponGradeValid(long id,long userId,String userName) {
        int count = couponGradeMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }

    @Override
    public int addCouponGrade(CouponGradeCondition condition) {
        int flag = 0;
        try {
            CouponGrade couponGrade = new CouponGrade();
            couponGrade.setCode(condition.getCode());
            couponGrade.setName(condition.getName());
            couponGrade.setRemarks(condition.getRemarks());
            couponGrade.setStatus(condition.getStatus());
            couponGrade.setType(condition.getType());
            couponGrade.setCreatedBy(Long.parseLong(condition.getUserId()));
            couponGrade.setCreatedByName(condition.getUserName());
            couponGrade.setCreated(new Date());
            //先插入主表
            long IdKey = couponGradeMapper.insertCouponGrade(couponGrade);
            //根据主表主键插入详情表
            CouponGradeDetail couponGradeDetail = new CouponGradeDetail();
            couponGradeDetail.setGradeId(IdKey);
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
        }catch (Exception e){
            flag = 1;
         e.printStackTrace();
        }

        return flag;
    }

    @Override
    public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(CouponGradeCondition condition) {
        ResponseResult<PagedList<CouponGradeVO>> result= new ResponseResult<PagedList<CouponGradeVO>>();
        PagedList<CouponGradeVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponGradeVO> couponGradeList = couponGradeMapper.getCouponGradePage(condition);
        PageInfo<CouponGradeVO> pageInfo = new PageInfo<>(couponGradeList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    @Override
    public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(String gradeId, Integer pageNo, Integer pageSize) {
        ResponseResult<PagedList<GradeTempleteCountVO>> result= new ResponseResult<PagedList<GradeTempleteCountVO>>();
        PagedList<GradeTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(pageNo,pageSize);
        List<GradeTempleteCountVO> couponGradeCountList = couponGradeMapper.getGradeTempleteCountPage(Long.parseLong(gradeId));
        PageInfo<GradeTempleteCountVO> pageInfo = new PageInfo<>(couponGradeCountList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }
}
