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
import com.winhxd.b2c.common.domain.promotion.vo.ApplyTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.company.CompanyServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

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
        LOGGER.info("适用对象详情查看参数id:{},type:{}",id,type);

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

                List<BrandVO> brandVoList = result.getDataWithException();
                if (CollectionUtils.isEmpty(brandVoList)) {
                    throw new BusinessException(result.getCode());
                }

                for(int i=0;i<couponApplyBrandLists.size();i++){
                    CouponApplyBrandList couponApplyBrandList = couponApplyBrandLists.get(i);
                    for (int j = 0; j < brandVoList.size(); j++) {
                        if (couponApplyBrandList.getBrandCode().equals(brandVoList.get(j).getBrandCode())) {
                            couponApplyBrandList.setBrandName(brandVoList.get(j).getBrandName());
                            break;
                        }
                    }
                }
                //调用获取品牌商信息接口
                ResponseResult<List<CompanyInfo>> result1 = companyServiceClient.getCompanyInfoByCodes(companyCodes);

                List<CompanyInfo> companyInfoList = result1.getDataWithException();
                if (CollectionUtils.isEmpty(companyInfoList)) {
                    throw new BusinessException(result1.getCode());
                }

                for(int i=0;i<couponApplyBrandLists.size();i++){
                    CouponApplyBrandList couponApplyBrandList = couponApplyBrandLists.get(i);
                    for(int j=0;j<companyInfoList.size();j++){
                        if(couponApplyBrandList.getCompanyCode().equals(companyInfoList.get(j).getCompanyCode())){
                            couponApplyBrandList.setCompanyName(companyInfoList.get(j).getCompanyName());
                            break;
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

                List<ProductSkuVO> productSkuVOList = result2.getDataWithException();
                if (CollectionUtils.isEmpty(productSkuVOList)) {
                    throw new BusinessException(result2.getCode());
                }

                for(int i=0;i<couponApplyProductLists.size();i++){
                    CouponApplyProductList couponApplyProductList = couponApplyProductLists.get(i);
                    for(int j=0;j<productSkuVOList.size();j++){
                        if(couponApplyProductList.getSkuCode().equals(productSkuVOList.get(j).getSkuCode())){
                            couponApplyProductList.setProductName(productSkuVOList.get(j).getSkuName());
                            break;
                        }
                    }

                }
            }
        }
        return vo;
    }

    @Override
    public int updateCouponApplyToValid(Long id, Long userId, String userName) {
        LOGGER.info("适用对象规则设置无效参数id:{},userId:{},userName{}",id,userId,userName);

        int count = couponApplyMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }

    @Override
    public PagedList<CouponApplyVO> findCouponApplyPage(CouponApplyCondition condition) {
        PagedList<CouponApplyVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponApplyVO> couponApplyList = couponApplyMapper.getCouponApplyPage(condition);

        List<Map<Long, Object>> list = null;
        if (!CollectionUtils.isEmpty(couponApplyList)) {
            List<Long> ids = couponApplyList.stream().map(CouponApplyVO::getId).collect(Collectors.toList());
            list = couponApplyMapper.getRelationCouponApplyCountMap(ids);
        }

        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, Object> resultMap = getCouponApplyMap(list);
            for (CouponApplyVO couponApplyVO : couponApplyList) {
                if (resultMap.containsKey(couponApplyVO.getId())) {
                    Long relTempleteCount = (Long) resultMap.get(couponApplyVO.getId());
                    String templeteCount = relTempleteCount == null ? "0" : String.valueOf(relTempleteCount);
                    couponApplyVO.setRelTempleteCount(templeteCount);
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

    /**
     * 封装返回的数据，组装成所要的数据
     *
     * @param list
     * @return
     */
    public Map<Long, Object> getCouponApplyMap(List<Map<Long, Object>> list) {
        Map<Long, Object> resultMap = new HashMap<>(list.size());
        for (Map<Long, Object> map : list) {
            Long id = (Long) map.get("id");
            Long relTempleteCount = (Long) map.get("relTempleteCount");
            resultMap.put(id, relTempleteCount);
        }
        return resultMap;
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
            if(bkey == 0){
                throw new BusinessException(BusinessCode.CODE_500004,"适用对象插入失败");
            }
            if(bkey != 0){
                List<CouponApplyBrandList> list = condition.getCouponApplyBrandList();
                if(!CollectionUtils.isEmpty(list)){
                    for(CouponApplyBrandList cab :list){
                        cab.setApplyBrandId(couponApplyBrand.getId());
                        cab.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
                        couponApplyBrandListMapper.insert(cab);
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
                List<CouponApplyProductList> list = condition.getCouponApplyProductList();
                if(!CollectionUtils.isEmpty(list)){
                    for(CouponApplyProductList cap :list){
                        cap.setApplyProductId(couponApplyProduct.getId());
                        cap.setStatus(CouponTemplateEnum.EFFICTIVE.getCode());
                        couponApplyProductListMapper.insert(cap);
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
