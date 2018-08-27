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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponApplyServiceImpl.class);
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
    public CouponApplyVO viewCouponApplyDetail(String id,Short type) {
        LOGGER.info("适用对象详情查看参数id:"+id+" type:"+type);
        if(StringUtils.isBlank(id) ||type==null ){
            throw new BusinessException(BusinessCode.CODE_500010,"必传参数错误");
        }
        CouponApplyVO vo = null;
        if(type.equals(CouponApplyEnum.COMMON_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyCommonDetail(Long.parseLong(id));
        }
        if(type.equals(CouponApplyEnum.BRAND_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyBrandDetail(Long.parseLong(id));
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
        }



        if(type.equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
            vo = couponApplyMapper.viewCouponApplyProdDetail(Long.parseLong(id));
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
        }
        System.out.print(vo.toString());
        return vo;
    }

    @Override
    public int updateCouponApplyToValid(Long id, Long userId, String userName) {
        LOGGER.info("适用对象规则设置无效参数id:"+id+" userId:"+userId+" userName"+userName);
        if(id==null || userId==null || StringUtils.isBlank(userName)){
           throw new BusinessException(BusinessCode.CODE_500010,"必传参数错误");
        }
        int count = couponApplyMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }

    @Override
    public PagedList<CouponApplyVO> findCouponApplyPage(CouponApplyCondition condition) {
        PagedList<CouponApplyVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponApplyVO> couponApplyList = couponApplyMapper.getCouponApplyPage(condition);
        //拼接关联模板数量
        if(couponApplyList!=null && couponApplyList.size()>0){
            for(int i=0;i<couponApplyList.size();i++){
                CouponApplyVO vo = couponApplyList.get(i);
                TempleteRelationCountVO templeteRelationCountVO = couponApplyMapper.getRelationCouponApplyCount(vo.getId());
                if(templeteRelationCountVO!=null){
                    vo.setRelTempleteCount(String.valueOf(templeteRelationCountVO.getRelTempleteCount()==null?0:templeteRelationCountVO.getRelTempleteCount()));
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
        return pagedList;
    }

    @Override
    @Transactional
    public int addCouponApply(CouponApplyCondition condition) {
        LOGGER.info("添加适用对象参数:"+condition.toString());
        int flag = 0;
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
                                couponApplyBrandList.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
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
                                couponApplyProductList.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
                                couponApplyProductListMapper.insert(couponApplyProductList);
                            }
                        }
                    }
                }

        return flag;
    }

    @Override
    public PagedList<ApplyTempleteCountVO> findApplyTempleteCountPage(RuleRealationCountCondition condition) {
        PagedList<ApplyTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<ApplyTempleteCountVO> couponApplyList = couponApplyMapper.findApplyTempleteCountPage(condition.getId());
        PageInfo<ApplyTempleteCountVO> pageInfo = new PageInfo<>(couponApplyList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }


}
