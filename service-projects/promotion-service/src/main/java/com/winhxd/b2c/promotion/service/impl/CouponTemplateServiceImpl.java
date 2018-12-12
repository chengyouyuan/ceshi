package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponTemplateMapper;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/6 10:52
 * @Description 优惠券模板服务接口 实现类
 **/
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {
    private static final Logger logger = LoggerFactory.getLogger(CouponTemplateServiceImpl.class);
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
    @Transactional
    public int saveCouponTemplate(CouponTemplateCondition couponTemplateCondition) {
        logger.info("添加优惠券模板参数:{}",couponTemplateCondition);
        int flag = 0;
        CouponTemplate couponTemplate = new CouponTemplate();
        couponTemplate.setTitle(couponTemplateCondition.getTitle());
        couponTemplate.setExolian(couponTemplateCondition.getExolian());
        couponTemplate.setRemarks(couponTemplateCondition.getRemarks());
        couponTemplate.setInvestorId(couponTemplateCondition.getInvestorId());
        couponTemplate.setGradeId(couponTemplateCondition.getGradeId());
        couponTemplate.setApplyRuleId(couponTemplateCondition.getApplyRuleId());
        couponTemplate.setCouponLabel(couponTemplateCondition.getCouponLabel());
        couponTemplate.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
        couponTemplate.setPayType(couponTemplateCondition.getPayType());
        couponTemplate.setCode(couponTemplateCondition.getCode());
        couponTemplate.setCorner(couponTemplateCondition.getCorner());
        couponTemplate.setCreated(new Date());
        couponTemplate.setCreatedByName(couponTemplateCondition.getCreatedByName());
        couponTemplate.setCreatedBy(Long.parseLong(couponTemplateCondition.getCreatedBy()));
        int n = couponTemplateMapper.insert(couponTemplate);
        if(n==0){
            throw new BusinessException(BusinessCode.CODE_500003,"优惠券模板添加失败");
        }
        return flag;
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
        CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(Long.parseLong(id));
        if(couponTemplate!=null){
           vo.setApplyRuleId(couponTemplate.getApplyRuleId());
           vo.setApplyRuleName(couponTemplate.getApplyRuleName());
           vo.setPayType(couponTemplate.getPayType());
           vo.setCouponLabel(couponTemplate.getCouponLabel());
           vo.setId(couponTemplate.getId());
           vo.setExolian(couponTemplate.getExolian());
           vo.setGradeName(couponTemplate.getGradeName());
           vo.setGradeId(couponTemplate.getGradeId());
           vo.setTitle(couponTemplate.getTitle());
           vo.setInvestorId(couponTemplate.getInvestorId());
           vo.setInvestorName(couponTemplate.getInvestorName());
           vo.setStatus(couponTemplate.getStatus());
        }
        return vo;
    }

    /**
     *
     *@Deccription  多条件分页查询优惠券分页列表
     *@Params  couponTemplateCondition
     *@Return
     *@User  wl
     *@Date   2018/8/6 18:29
     */
    @Override
    public PagedList<CouponTemplateVO> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition) {

        PagedList<CouponTemplateVO> pagedList = new PagedList<>();
        if (!couponTemplateCondition.getIsQueryAll()) {
            PageHelper.startPage(couponTemplateCondition.getPageNo(), couponTemplateCondition.getPageSize());
        }
        checkApplyRuleName(couponTemplateCondition);

        List<CouponTemplateVO> couponTemplateVOList = couponTemplateMapper.getCouponTemplatePageByCondition(couponTemplateCondition);
        PageInfo<CouponTemplateVO> pageInfo = new PageInfo<>(couponTemplateVOList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    private void checkApplyRuleName(CouponTemplateCondition couponTemplateCondition) {
        String applyRuleName = couponTemplateCondition.getApplyRuleName();
        if (StringUtils.isNotBlank(applyRuleName)) {
            if (CouponApplyEnum.COMMON_COUPON.getDesc().equals(applyRuleName.trim())) {
                couponTemplateCondition.setApplyRuleType(CouponApplyEnum.COMMON_COUPON.getCode());
            } else if (CouponApplyEnum.BRAND_COUPON.getDesc().equals(applyRuleName.trim())) {
                couponTemplateCondition.setApplyRuleType(CouponApplyEnum.BRAND_COUPON.getCode());
            } else if (CouponApplyEnum.CAT_COUPON.getDesc().equals(applyRuleName.trim())) {
                couponTemplateCondition.setApplyRuleType(CouponApplyEnum.CAT_COUPON.getCode());
            } else if (CouponApplyEnum.PRODUCT_COUPON.getDesc().equals(applyRuleName.trim())) {
                couponTemplateCondition.setApplyRuleType(CouponApplyEnum.PRODUCT_COUPON.getCode());
            }
        }
    }

    @Override
    public CouponTemplateVO viewCouponTemplateDetailById(String id) {
        CouponTemplateVO vo = new CouponTemplateVO();
        CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(Long.parseLong(id));
        if(couponTemplate!=null){
            vo.setApplyRuleId(couponTemplate.getApplyRuleId());
            vo.setApplyRuleName(couponTemplate.getApplyRuleName());
            vo.setPayType(couponTemplate.getPayType());
            vo.setCouponLabel(couponTemplate.getCouponLabel());
            vo.setId(couponTemplate.getId());
            vo.setExolian(couponTemplate.getExolian());
            vo.setGradeName(couponTemplate.getGradeName());
            vo.setGradeId(couponTemplate.getGradeId());
            vo.setTitle(couponTemplate.getTitle());
            vo.setInvestorId(couponTemplate.getInvestorId());
            vo.setInvestorName(couponTemplate.getInvestorName());
            vo.setStatus(couponTemplate.getStatus());
            vo.setCode(couponTemplate.getCode());
            vo.setRemarks(couponTemplate.getRemarks());
        }
        return vo;
    }


    /**
     *@Deccription  设为无效
     *@Params  ids  多个页面勾选的ID 用逗号","隔开
     *@Return  ResponseResult 删除是否成功
     *@User  wl
     *@Date   2018/8/6 20:39
     */
    @Override
    public int updateCouponTemplateToValid(Long id, Long updatedBy, Date updated, String updatedByName) {
       return couponTemplateMapper.updateCouponTemplateToValid(id,updatedBy,updated,updatedByName);
    }
}
