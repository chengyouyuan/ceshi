package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    @Transactional
    public int addCouponApply(CouponApplyCondition condition) {
        int flag = 0;
            CouponApply couponApply = new CouponApply();
            couponApply.setApplyRuleType(condition.getApplyRuleType());
            couponApply.setCode(condition.getCode());
            couponApply.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
            couponApply.setName(condition.getName());
            couponApply.setRemarks(condition.getRemarks());
            couponApply.setCreatedBy(Long.parseLong(condition.getUserId()));
            couponApply.setCreatedByName(condition.getUserName());
            //couponApply.setCreatedBy(100123L);
            //couponApply.setCreatedByName("HAHA");
            couponApply.setCreated(new Date());
            long mainKey = couponApplyMapper.insertCouponApply(couponApply);
            if(mainKey==0){
                throw new BusinessException(BusinessCode.CODE_500004,"适用对象插入失败");
            }
                //品牌券插入
                if(condition.getApplyRuleType().shortValue() == CouponApplyEnum.BRAND_COUPON.getCode()){
                    CouponApplyBrand couponApplyBrand = new CouponApplyBrand();
                    couponApplyBrand.setApplyId(couponApply.getId());
                    long bkey = couponApplyBrandMapper.insertCouponApplyBrand(couponApplyBrand);
                    if(bkey==0){
                        throw new BusinessException(BusinessCode.CODE_500004,"适用对象插入失败");
                    }
                    if(bkey!=0){
                        //插入list
                        List<CouponApplyBrandList> list = condition.getCouponApplyBrandList();
                        if(list!=null && list.size()>0){
                            for(int i=0;i<list.size();i++){
                                CouponApplyBrandList couponApplyBrandList = list.get(i);
                                couponApplyBrandList.setApplyBrandId(couponApplyBrand.getId());
                                couponApplyBrandList.setStatus((short)1);
                                couponApplyBrandListMapper.insert(couponApplyBrandList);
                            }
                        }
                    }
                }
                //商品券插入
                if(condition.getApplyRuleType().shortValue() == CouponApplyEnum.PRODUCT_COUPON.getCode()){
                    CouponApplyProduct couponApplyProduct = new CouponApplyProduct();
                    couponApplyProduct.setApplyId(couponApply.getId());
                    long pkey = couponApplyProductMapper.insertCouponApplyProduct(couponApplyProduct);
                    if(pkey==0){
                        throw new BusinessException(BusinessCode.CODE_500004,"适用对象插入失败");
                    }
                    if(pkey!=0){
                        //插入list
                        List<CouponApplyProductList> list = condition.getCouponApplyProductList();
                        if(list!=null && list.size()>0){
                            for(int i=0;i<list.size();i++){
                                CouponApplyProductList couponApplyProductList = list.get(i);
                                couponApplyProductList.setApplyProductId(couponApplyProduct.getId());
                                couponApplyProductList.setStatus((short)1);
                                couponApplyProductListMapper.insert(couponApplyProductList);
                            }
                        }
                    }
                }

        return flag;
    }

    @Override
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(String applyId, Integer pageNo, Integer pageSize) {
        ResponseResult<PagedList<ApplyTempleteCountVO>> result= new ResponseResult<PagedList<ApplyTempleteCountVO>>();
        PagedList<ApplyTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(pageNo,pageSize);
        List<ApplyTempleteCountVO> couponApplyList = couponApplyMapper.findApplyTempleteCountPage(Long.parseLong(applyId));
        PageInfo<ApplyTempleteCountVO> pageInfo = new PageInfo<>(couponApplyList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }


}
