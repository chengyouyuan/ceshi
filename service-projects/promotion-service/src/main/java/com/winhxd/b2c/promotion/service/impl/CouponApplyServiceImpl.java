package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/9 12:14
 * @Description
 **/
@Service
public class CouponApplyServiceImpl implements CouponApplyService {
     @Autowired
     private CouponApplyMapper couponApplyMapper;
     @Autowired
     private CouponApplyBrandMapper couponApplyBrandMapper;
     @Autowired
     private CouponApplyBrandListMapper couponApplyBrandListMapper;
     @Autowired
     private CouponApplyProductMapper couponApplyProductMapper;
     @Autowired
     private CouponApplyProductListMapper couponApplyProductListMapper;


    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(long id) {
        ResponseResult responseResult = new ResponseResult();
        CouponApplyVO vo = couponApplyMapper.viewCouponApplyDetail(id);
        responseResult.setData(vo);
        return responseResult;
    }

    @Override
    public int updateCouponApplyToValid(long id, long userId, String userName) {
        int count = couponApplyMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }

    @Override
    public ResponseResult<PagedList<CouponApplyVO>> findCouponApplyPage(CouponApplyCondition condition) {
        ResponseResult<PagedList<CouponApplyVO>> result= new ResponseResult<PagedList<CouponApplyVO>>();
        PagedList<CouponApplyVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponApplyVO> couponApplyList = couponApplyMapper.getCouponApplyPage(condition);
        PageInfo<CouponApplyVO> pageInfo = new PageInfo<>(couponApplyList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }

    @Override
    public int addCouponApply(CouponApplyCondition condition) {
        int flag = 0;
        try {
            CouponApply couponApply = new CouponApply();
            couponApply.setApplyRuleType(condition.getApplyRuleType());
            couponApply.setCode(condition.getCode());
            couponApply.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
            couponApply.setName(condition.getName());
            couponApply.setRemarks(condition.getRemarks());
            couponApply.setCreatedBy(Long.parseLong(condition.getUserId()));
            couponApply.setCreatedByName(condition.getUserName());
            couponApply.setCreated(new Date());
            long mainKey = couponApplyMapper.insertCouponApply(couponApply);
            //品牌券插入
            if(condition.getApplyRuleType().shortValue() == CouponApplyEnum.BRAND_COUPON.getCode()){
                CouponApplyBrand couponApplyBrand = new CouponApplyBrand();
                couponApplyBrand.setApplyId(mainKey);
                long bkey = couponApplyBrandMapper.insertCouponApplyBrand(couponApplyBrand);
                //插入list
                List<CouponApplyBrandList> list = condition.getCouponApplyBrandList();
                if(list!=null && list.size()>0){
                    for(int i=0;i<list.size();i++){
                        CouponApplyBrandList couponApplyBrandList = list.get(i);
                        couponApplyBrandListMapper.insert(couponApplyBrandList);
                    }
                }

            }
            //商品券插入
            if(condition.getApplyRuleType().shortValue() == CouponApplyEnum.PRODUCT_COUPON.getCode()){
                CouponApplyProduct couponApplyProduct = new CouponApplyProduct();
                couponApplyProduct.setApplyId(mainKey);
                long pkey = couponApplyProductMapper.insertCouponApplyProduct(couponApplyProduct);
                //插入list
                List<CouponApplyProductList> list = condition.getCouponApplyProductList();
                if(list!=null && list.size()>0){
                    for(int i=0;i<list.size();i++){
                        CouponApplyProductList couponApplyProductList = list.get(i);
                        couponApplyProductListMapper.insert(couponApplyProductList);
                    }
                }

            }
        }catch (Exception e){
            flag = 1;
            e.printStackTrace();
        }
        return 0;
    }


}
