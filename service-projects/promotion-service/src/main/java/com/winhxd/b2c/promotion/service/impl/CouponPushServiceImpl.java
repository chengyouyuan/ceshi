package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.BrandCondition;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponActivityRecord;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrand;
import com.winhxd.b2c.common.domain.promotion.model.CouponApplyBrandList;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplateSend;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInStoreGetedAndUsedVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponPushService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CouponPushServiceImpl implements CouponPushService {
    private static final Logger logger = LoggerFactory.getLogger(CouponPushServiceImpl.class);

    // 单商品优惠券
    private static final int SINGLE_BRAND = 1;

    @Autowired
    CouponPushCustomerMapper couponPushCustomerMapper;

    @Autowired
    StoreServiceClient storeServiceClient;

    @Autowired
    CouponMapper couponMapper;

    @Autowired
    CouponTemplateSendMapper couponTemplateSendMapper;

    @Autowired
    CouponActivityRecordMapper couponActivityRecordMapper;

    @Autowired
    CouponApplyBrandMapper couponApplyBrandMapper;

    @Autowired
    CouponApplyBrandListMapper couponApplyBrandListMapper;

    @Autowired
    ProductServiceClient productServiceClient;

    @Override
    public List<CouponPushVO> getSpecifiedPushCoupon() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }

        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，用户查询门店优惠券列表异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponPushVO> couponPushResult = getCouponPush(customerUser, storeUserInfo);

        List<Long> activityIds = new ArrayList<>();
        List<Long> unActivityIds = new ArrayList<>();
        for (CouponPushVO couponPushVO:couponPushResult) {
            for (int x=0;x<couponPushVO.getSendNum();x++) {
                CouponTemplateSend couponTemplateSend = saveCouponTemplateSend(customerUser, couponPushVO);

                saveActivityRecord(customerUser, couponPushVO, couponTemplateSend);
            }

        }


        return this.getCouponPushDetail(couponPushResult);
    }

    private List<CouponPushVO> getCouponPushDetail(List<CouponPushVO> couponPushResult) {
        for (CouponPushVO couponPushVO:couponPushResult) {
            List<CouponApplyBrand> couponApplyBrands = null;
            if (String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()).equals(couponPushVO.getApplyRuleType())) {
                couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponPushVO.getApplyId());
            }

            if (!CollectionUtils.isEmpty(couponApplyBrands)) {
                List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());

                List<String> brandCodes = couponApplyBrandLists.stream().map(couponApplyBrand -> couponApplyBrand.getBrandCode()).collect(Collectors.toList());

                //调用获取商品信息接口
                ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                    logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                    throw new BusinessException(result.getCode());
                }

                getLogo(couponPushVO,result.getData(),couponApplyBrandLists);

            }
        }
        return couponPushResult;
    }

    private void saveActivityRecord(CustomerUser customerUser, CouponPushVO couponPushVO, CouponTemplateSend couponTemplateSend) {
        CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
        couponActivityRecord.setCouponActivityId(couponPushVO.getActivityId());
        couponActivityRecord.setCustomerId(customerUser.getCustomerId());
        couponActivityRecord.setSendId(couponTemplateSend.getId());
        couponActivityRecord.setTemplateId(couponPushVO.getTemplateId());
        couponActivityRecord.setCreated(new Date());
        couponActivityRecord.setCreatedBy(customerUser.getCustomerId());
        couponActivityRecord.setCreatedByName("");
        couponActivityRecordMapper.insertSelective(couponActivityRecord);
    }

    private CouponTemplateSend saveCouponTemplateSend(CustomerUser customerUser, CouponPushVO couponPushVO) {
        CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
        couponTemplateSend.setStatus(CouponActivityEnum.NOT_USE.getCode());
        couponTemplateSend.setTemplateId(couponPushVO.getTemplateId());
        couponTemplateSend.setSource((int) CouponActivityEnum.SYSTEM.getCode());
        couponTemplateSend.setSendRole((int) CouponActivityEnum.ORDINARY_USER.getCode());
        couponTemplateSend.setCustomerId(customerUser.getCustomerId());
        couponTemplateSend.setCustomerMobile("");
        couponTemplateSend.setCount(1);
        couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
        couponTemplateSend.setCreated(new Date());
        couponTemplateSend.setCreatedByName("");
        couponTemplateSend.setStartTime(getStringTurnDate(couponPushVO.getActivityStart()));

        couponTemplateSend.setEndTime(getStringTurnDate(couponPushVO.getActivityEnd()));

        couponTemplateSendMapper.insertSelective(couponTemplateSend);
        return couponTemplateSend;
    }

    /**
     * 获取品牌logo或者品牌商logo
     * @param vo
     * @param result
     * @param couponApplyBrandLists
     */
    private void getLogo(CouponPushVO vo, List<BrandVO> result, List<CouponApplyBrandList> couponApplyBrandLists) {
        // 优惠券是单品牌商就显示单品牌商logo
        Optional<String> logoUrl = null;
        if (result.size() == SINGLE_BRAND) {
            logoUrl = result.stream().filter(brand -> StringUtils.isNotBlank(brand.getBrandImg()))
                    .map(brand -> brand.getBrandImg()).findAny();
        }

        if (result.size() > SINGLE_BRAND) {
            BrandCondition condition = new BrandCondition();
            condition.setBrandCode(couponApplyBrandLists.get(0).getBrandCode());
            condition.setCompanyCode(couponApplyBrandLists.get(0).getCompanyCode());

            ResponseResult<PagedList<BrandVO>> brandInfo = productServiceClient.getBrandInfoByPage(condition);
            if (brandInfo != null && !CollectionUtils.isEmpty(brandInfo.getData().getData())) {
                logoUrl = brandInfo.getData().getData().stream()
                        .filter(brand -> StringUtils.isNotBlank(brand.getCompanyLogo())).map(brand -> brand.getCompanyLogo()).findAny();
            }
        }

        if (logoUrl.isPresent()) {
            vo.setLogoImg(logoUrl.get());
        }
    }

    private Date getStringTurnDate(String time) {
        Date date = null;
        try {
            date = DateUtils.parseDate(time, "yyyy.MM.dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private List<CouponPushVO> getCouponPush(CustomerUser customerUser, StoreUserInfoVO storeUserInfo) {
        // 查询优惠券推送给用户
        List<CouponPushVO> couponPushStores = couponPushCustomerMapper.selectCouponPushStore(storeUserInfo.getId());
        // 查询优惠券推送给门店
        List<CouponPushVO> couponPushCustomers = couponPushCustomerMapper.selectCouponPushCustomer(customerUser.getCustomerId());

        List<CouponPushVO> couponPushResult = new ArrayList<>();
        couponPushResult.addAll(couponPushCustomers);
        couponPushCustomers.addAll(couponPushStores);

        return couponPushResult;
    }
}
