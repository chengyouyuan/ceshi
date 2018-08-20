package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.CompanyInfo;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.condition.CouponApplyCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.company.CompanyServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    private ProductServiceClient productServiceClient;
    @Autowired
    private CompanyServiceClient companyServiceClient;


    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(long id,Short type) {
        ResponseResult responseResult = new ResponseResult();
        CouponApplyVO vo = null;
        if(type.equals(CouponApplyEnum.COMMON_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyCommonDetail(id);
            responseResult.setData(vo);
        }
        if(type.equals(CouponApplyEnum.BRAND_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyBrandDetail(id);

            List<CouponApplyBrandList> couponApplyBrandLists = vo.getCouponApplyBrandList();
            if(!couponApplyBrandLists.isEmpty()){
                //组装请求的参数
                List<String> brandCodes = new ArrayList<>();
                List<String> companyCodes = new ArrayList<>();
                for(CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists){
                    brandCodes.add(couponApplyBrandList.getBrandCode());
                    companyCodes.add(couponApplyBrandList.getCompanyCode());
                }
                //调用获取品牌信息接口
                ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                    throw new BusinessException(result.getCode());
                }
                List<BrandVO> BrandVoList = result.getData();
                for(int i=0;i<couponApplyBrandLists.size();i++){
                    CouponApplyBrandList couponApplyBrandList = couponApplyBrandLists.get(i);
                    for(int j=0;j<BrandVoList.size();j++){
                        if(couponApplyBrandList.getBrandCode().equals(BrandVoList.get(j).getBrandCode())){
                            couponApplyBrandList.setBrandName(BrandVoList.get(j).getBrandName());
                        }
                    }
                }
                //调用获取品牌商信息接口
                ResponseResult<List<CompanyInfo>> result1 = companyServiceClient.getCompanyInfoByCodes(companyCodes);
                if (result1 == null || result1.getCode() != BusinessCode.CODE_OK || result1.getData() == null) {
                    throw new BusinessException(result1.getCode());
                }
                List<CompanyInfo> companyInfoList = result1.getData();
                for(int i=0;i<couponApplyBrandLists.size();i++){
                    CouponApplyBrandList couponApplyBrandList = couponApplyBrandLists.get(i);
                    for(int j=0;j<companyInfoList.size();j++){
                        if(couponApplyBrandList.getCompanyCode().equals(companyInfoList.get(j).getCompanyCode())){
                            couponApplyBrandList.setCompanyName(companyInfoList.get(j).getCompanyName());
                        }
                    }
                }
            }
            responseResult.setData(vo);
        }



        if(type.equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyProdDetail(id);
            List<CouponApplyProductList> couponApplyProductLists = vo.getCouponApplyProductList();
            if(!couponApplyProductLists.isEmpty()){
                //组装请求的参数
                List<String> productSkus = new ArrayList<>();
                for(CouponApplyProductList couponApplyProductList : couponApplyProductLists){
                    productSkus.add(couponApplyProductList.getSkuCode());
                }
                ProductCondition productCondition = new ProductCondition();
                productCondition.setProductSkus(productSkus);
                //调用获取商品信息接口
                productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                ResponseResult<List<ProductSkuVO>> result2 = productServiceClient.getProductSkus(productCondition);
                if (result2 == null || result2.getCode() != BusinessCode.CODE_OK || result2.getData() == null) {
                    throw new BusinessException(result2.getCode());
                }
                List<ProductSkuVO> productSkuVOList = result2.getData();
                for(int i=0;i<couponApplyProductLists.size();i++){
                    CouponApplyProductList couponApplyProductList = couponApplyProductLists.get(i);
                    for(int j=0;j<productSkuVOList.size();j++){
                        if(couponApplyProductList.getSkuCode().equals(productSkuVOList.get(j).getSkuCode())){
                            couponApplyProductList.setProductName(productSkuVOList.get(j).getSkuName());
                        }
                    }

                }
            }

            responseResult.setData(vo);
        }
        System.out.print(vo.toString());
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
        //拼接关联模板数量
        if(couponApplyList!=null && couponApplyList.size()>0){
            for(int i=0;i<couponApplyList.size();i++){
                CouponApplyVO vo = couponApplyList.get(i);
                TempleteRelationCountVO templeteRelationCountVO = couponApplyMapper.getRelationCouponApplyCount(vo.getId());
                if(templeteRelationCountVO!=null){
                    vo.setRelTempleteCount(String.valueOf(templeteRelationCountVO.getRelTempleteCount()));
                }else{
                    vo.setRelTempleteCount(String.valueOf(0));
                }

            }
        }
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
    public ResponseResult<PagedList<ApplyTempleteCountVO>> findApplyTempleteCountPage(RuleRealationCountCondition condition) {
        ResponseResult<PagedList<ApplyTempleteCountVO>> result= new ResponseResult<PagedList<ApplyTempleteCountVO>>();
        PagedList<ApplyTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<ApplyTempleteCountVO> couponApplyList = couponApplyMapper.findApplyTempleteCountPage(condition.getId());
        PageInfo<ApplyTempleteCountVO> pageInfo = new PageInfo<>(couponApplyList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        result.setData(pagedList);
        return result;
    }


}
