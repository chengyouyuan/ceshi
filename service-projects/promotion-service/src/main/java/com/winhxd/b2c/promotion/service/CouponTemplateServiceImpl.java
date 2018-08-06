package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.promotion.dao.CouponTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @Author wl
 * @Date 2018/8/6 10:52
 * @Description 优惠券模板服务接口 实现类
 **/
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService{
     @Autowired
     private CouponTemplateMapper couponTemplateMapper;
    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:45
     */
    @Override
    public int saveCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setTitle(couponTemplateCondition.getTitle());
        couponTemplate.setExolian(couponTemplateCondition.getExolian());
        couponTemplate.setRemarks(couponTemplateCondition.getRemarks());
        couponTemplate.setInvestorId(couponTemplateCondition.getInvestorId());
        couponTemplate.setGradeId(couponTemplateCondition.getGradeId());
        couponTemplate.setApplyRuleId(couponTemplateCondition.getApplyRuleId());
        couponTemplate.setCouponLabel(couponTemplateCondition.getCouponLabel());
        couponTemplate.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
        couponTemplate.setCalType(couponTemplateCondition.getCalType());
        couponTemplate.setPayType(couponTemplateCondition.getPayType());
        couponTemplate.setCreated(new Date());
        couponTemplate.setCreatedByName("lidabenshi");
        couponTemplate.setCreatedBy(100102L);
        return couponTemplateMapper.insert(couponTemplate);
    }


    /**
     *
     *@Deccription 模板列表页面跳转到修改页面 根据id 查询出对应的实体类
     *@Params   id  模板id
     *@Return   ResponseResult
     *@User     wl
     *@Date   2018/8/6 14:41
     */
    @Override
    public CouponTemplateVO getCouponTemplateById(String id) {
        CouponTemplateVO vo = new CouponTemplateVO();
        CouponTemplate CouponTemplate = couponTemplateMapper.selectCouponTemplateById(Long.parseLong(id));
        if(CouponTemplate!=null){
           vo.setApplyRuleId(CouponTemplate.getApplyRuleId());
           vo.setApplyRuleName(CouponTemplate.getApplyRuleName());
           vo.setCalType(CouponTemplate.getCalType());
           vo.setPayType(CouponTemplate.getPayType());
           vo.setCouponLabel(CouponTemplate.getCouponLabel());
           vo.setId(CouponTemplate.getId());
           vo.setExolian(CouponTemplate.getExolian());
           vo.setUseRangeName(CouponTemplate.getGradeName());
           vo.setGradeId(CouponTemplate.getGradeId());
           vo.setTitle(CouponTemplate.getTitle());
           vo.setInvestorId(CouponTemplate.getInvestorId());
           vo.setInvestorName(CouponTemplate.getInvestorName());
            vo.setStatus(CouponTemplate.getStatus());
        }
        return vo;
    }


}
