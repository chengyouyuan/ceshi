package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.promotion.dao.CouponTemplateMapper;
import com.winhxd.b2c.promotion.service.CouponTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @Author wl
 * @Date 2018/8/6 10:52
 * @Description 优惠券模板服务接口 实现类
 **/
@Service
public class CouponTemplateServiceImpl implements CouponTemplateService {
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
        couponTemplate.setCode(getUUID());
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
        CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(Long.parseLong(id));
        if(couponTemplate!=null){
           vo.setApplyRuleId(couponTemplate.getApplyRuleId());
           vo.setApplyRuleName(couponTemplate.getApplyRuleName());
           vo.setCalType(couponTemplate.getCalType());
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
    public ResponseResult<PagedList<CouponTemplateVO>> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition) {
        ResponseResult<PagedList<CouponTemplateVO>> result= new ResponseResult<PagedList<CouponTemplateVO>>();
        PagedList<CouponTemplateVO> pagedList = new PagedList<>();
        PageHelper.startPage(couponTemplateCondition.getPageNo(),couponTemplateCondition.getPageSize());
        List<CouponTemplateVO> customers = couponTemplateMapper.getCouponTemplatePageByCondition(couponTemplateCondition);
        PageInfo<CouponTemplateVO> pageInfo = new PageInfo<>(customers);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    @Override
    public ResponseResult<CouponTemplateVO> viewCouponTemplateDetailById(String id) {
        CouponTemplateVO vo = new CouponTemplateVO();
        CouponTemplate couponTemplate = couponTemplateMapper.selectCouponTemplateById(Long.parseLong(id));
        if(couponTemplate!=null){
            vo.setApplyRuleId(couponTemplate.getApplyRuleId());
            vo.setApplyRuleName(couponTemplate.getApplyRuleName());
            vo.setCalType(couponTemplate.getCalType());
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
        return null;
    }


    /**
     * 自动生成32位的UUid，对应数据库的主键id进行插入用。
     * @return
     */
    public String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
