package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponGradeEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponGrade;
import com.winhxd.b2c.common.domain.promotion.model.CouponGradeDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.TempleteRelationCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponGradeDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponGradeMapper;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
        LOGGER.info("查看详情参数id:"+id);
        if(StringUtils.isBlank(id)){
            throw new BusinessException(BusinessCode.CODE_500010,"必填参数错误");
        }
        CouponGradeVO vo = couponGradeMapper.viewCouponGradeDetail(Long.parseLong(id));
        return vo;
    }

    @Override
    @Transactional
    public int updateCouponGradeValid(Long id,Long userId,String userName) {
        LOGGER.info("坎级规则设置无效参数 id:"+id+" userId:"+ userId+" userName:"+userName);
        if(id==null || userId==null || StringUtils.isBlank(userName)){
            throw  new BusinessException(BusinessCode.CODE_500010,"必传参数错误");
        }
        int count = couponGradeMapper.updateCouponGradeValid(id,userId,userName);
        if(count<=0){
            throw  new BusinessException(BusinessCode.CODE_1001,"设置失败");
        }
        return count;
    }

    @Override
    @Transactional
    public int addCouponGrade(CouponGradeCondition condition) {
        LOGGER.info("添加坎级规则参数"+condition.toString());
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
            if(IdKey==0){
                throw new BusinessException(BusinessCode.CODE_500005,"坎级规则新建失败");
            }

            if(IdKey!=0){
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
                if(n==0){
                    throw new BusinessException(BusinessCode.CODE_500005,"坎级规则新建失败");
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
        //拼接模板关联数量
        if(couponGradeList!=null && couponGradeList.size()>0){
            for(int i=0;i<couponGradeList.size();i++){
                CouponGradeVO vo = couponGradeList.get(i);
                TempleteRelationCountVO templeteRelationCountVO = couponGradeMapper.getRelationCouponGradeCount(vo.getId());
                if(templeteRelationCountVO!=null){
                    vo.setRelTempleteCount(String.valueOf(templeteRelationCountVO.getRelTempleteCount()));
                }else{
                    vo.setRelTempleteCount(String.valueOf(0));
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
