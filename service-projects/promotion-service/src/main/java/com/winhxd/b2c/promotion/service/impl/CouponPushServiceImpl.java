package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.product.condition.BrandCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponPushVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.DateDealUtils;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponPushService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

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

    @Autowired
    CouponApplyProductMapper couponApplyProductMapper;

    @Autowired
    CouponApplyProductListMapper couponApplyProductListMapper;

    @Autowired
    private Cache cache;
    private static final int BACKROLL_LOCK_EXPIRES_TIME = 3000;

    @Override
    public List<CouponPushVO> getSpecifiedPushCoupon() {

        List<CouponPushVO> resultList;
            CustomerUser customerUser = UserContext.getCurrentCustomerUser();
            if (customerUser == null) {
                throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
            }
            StoreUserInfoVO storeUserInfo = getStoreUserInfoVO(customerUser);
            List<CouponPushVO> couponPushResult = getCouponPush(customerUser, storeUserInfo);

            List<Long> activityIds = new ArrayList<>();
            List<Long> unActivityIds = new ArrayList<>();
            // 通过活动ID和模板ID查询优惠券已使用数量
            Long receiveCouponCount = 0L;
            resultList = new ArrayList<>();
            boolean flag = false;
            for (CouponPushVO couponPushVO:couponPushResult) {
                for (int x=0;x<couponPushVO.getSendNum();x++) {

                    if (activityIds.contains(couponPushVO.getActivityId()) && receiveCouponCount < couponPushVO.getCouponNum()) {
                        flag = true;
                    }else if (unActivityIds.contains(couponPushVO.getActivityId())) {
                        continue;
                    }else {
                        receiveCouponCount = couponPushCustomerMapper.countUsedCouponNum(couponPushVO);
                        if (receiveCouponCount < couponPushVO.getCouponNum()) {
                            activityIds.add(couponPushVO.getActivityId());
                            flag = true;
                        }else{
                            // 优惠券是否可领取 0 已领取,1 可领取,2已领完
                            flag = false;
                            couponPushVO.setReceiveStatus("2");
                            unActivityIds.add(couponPushVO.getActivityId());
                        }
                    }

                    // 是否领取只有推券给用户才会有值  0 :未领取 1：已领取
                    if (couponPushVO.getReceive() == null) {
                        flag = checkStoreUserIsPushCoupon(customerUser.getCustomerId(),storeUserInfo.getId(),couponPushVO.getActivityId());
                    }

                    sendCoupon(customerUser, flag, couponPushVO);
                }

                resultList.add(couponPushVO);
            }
        return this.getCouponPushDetail(resultList);
    }


    private void sendCoupon(CustomerUser customerUser, boolean flag, CouponPushVO couponPushVO) {
        Lock lock = null;
        try {
            if (flag) {
                String lockKey = CacheName.PUSH_COUPON + couponPushVO.getActivityId()+couponPushVO.getTemplateId();
                lock = new RedisLock(cache, lockKey,BACKROLL_LOCK_EXPIRES_TIME);
                lock.lock();

                CouponTemplateSend couponTemplateSend = saveCouponTemplateSend(customerUser, couponPushVO);
                saveActivityRecord(customerUser, couponPushVO, couponTemplateSend);
                couponPushVO.setReceiveStatus("1");

                // 用户渠道领取优惠券修改状态
                if (couponPushVO.getReceive() != null) {
                    CouponPushCustomer couponPushCustomer = new CouponPushCustomer();
                    couponPushCustomer.setCouponActivityId(couponPushVO.getActivityId());
                    couponPushCustomer.setCustomerId(customerUser.getCustomerId());
                    couponPushCustomer.setReceive(true);
                    couponPushCustomerMapper.updateByActivityIdAndCustomerId(couponPushCustomer);
                }

                // 优惠券设定有效期，没有设定开始时间和结束时间。
                if (couponPushVO.getEffectiveDays() != null) {
                    DateFormat df = new SimpleDateFormat("yyyy.MM.dd");
                    couponPushVO.setActivityStart(df.format(new Date()));
                    couponPushVO.setActivityEnd(df.format(DateDealUtils.getEndDate(new Date(), couponPushVO.getEffectiveDays())));
                }
            }
        } finally {
            if (lock != null) {
                lock.unlock();
            }
        }
    }

    /**
     * 验证门店下的用户是否推券
     * @param customerId
     * @param storeId
     * @param activityId
     * @return
     */
    private boolean checkStoreUserIsPushCoupon(Long customerId, Long storeId, Long activityId) {
        CouponActivityRecord couponActivityRecord = couponActivityRecordMapper.checkStoreUserIsPushCoupon(customerId, storeId, activityId);
        if (couponActivityRecord == null) {
            return true;
        }
        return false;
    }


    private StoreUserInfoVO getStoreUserInfoVO(CustomerUser customerUser) {
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，用户查询门店优惠券列表异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        return result.getData();
    }

    private List<CouponPushVO> getCouponPushDetail(List<CouponPushVO> couponPushResult) {
        for (CouponPushVO couponPushVO:couponPushResult) {
            getBrandList(couponPushVO);
            getproductList(couponPushVO);
        }
        return couponPushResult;
    }

    /**
     * 获取商品信息
     * @param couponPushVO
     */
    private void getproductList(CouponPushVO couponPushVO) {
        if (String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()).equals(couponPushVO.getApplyRuleType())) {
            List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponPushVO.getApplyId());
            if(!CollectionUtils.isEmpty(couponApplyProducts)){
                List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                //组装请求的参数
                List<String> productSkus = couponApplyProductLists.stream()
                        .filter(couponApplyProduct -> StringUtils.isNotBlank(couponApplyProduct.getSkuCode()))
                        .map(couponApplyProduct -> couponApplyProduct.getSkuCode()).collect(Collectors.toList());
                ProductCondition productCondition = new ProductCondition();
                productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                productCondition.setProductSkus(productSkus);
                //调用获取商品信息接口
                ResponseResult<List<ProductSkuVO>> result = productServiceClient.getProductSkus(productCondition);
                if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                    logger.error("优惠券：{}获取商品sku信息接口调用失败:code={}，获取优惠券适用范围异常！~", productCondition, result == null ? null : result.getCode());
                    throw new BusinessException(result.getCode());
                }
                couponPushVO.setProducts(result.getData());
            }
        }
    }

    /**
     * 获取品牌信息
     * @param couponPushVO
     */
    private void getBrandList(CouponPushVO couponPushVO) {
        if (String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()).equals(couponPushVO.getApplyRuleType())) {
            List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponPushVO.getApplyId());
            if (!CollectionUtils.isEmpty(couponApplyBrands)) {
                List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());

                List<String> brandCodes = couponApplyBrandLists.stream()
                        .filter(couponApplyBrand -> StringUtils.isNotBlank(couponApplyBrand.getBrandCode()))
                        .map(couponApplyBrand -> couponApplyBrand.getBrandCode()).collect(Collectors.toList());

                //调用获取商品信息接口
                ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                    logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                    throw new BusinessException(result.getCode());
                }

                couponPushVO.setBrands(result.getData());
                getLogo(couponPushVO,result.getData(),couponApplyBrandLists);
            }
        }
    }

    /**
     * 保存优惠券活动领取记录
     * @param customerUser
     * @param couponPushVO
     * @param couponTemplateSend
     */
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

    /**
     * 保存活动优惠券模板信息
     * @param customerUser
     * @param couponPushVO
     * @return
     */
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

        // 优惠券设定有效期
        if (couponPushVO.getEffectiveDays() != null) {
            couponTemplateSend.setStartTime(DateDealUtils.getStartDate(new Date()));
            couponTemplateSend.setEndTime(DateDealUtils.getEndDate(new Date(),couponPushVO.getEffectiveDays()));
        }else {
            couponTemplateSend.setStartTime(getStringTurnDate(couponPushVO.getActivityStart()));
            couponTemplateSend.setEndTime(getStringTurnDate(couponPushVO.getActivityEnd()));
        }

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
            if(StringUtils.isNotBlank(time)) {
                date = DateUtils.parseDate(time, "yyyy.MM.dd");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获取总的优惠券推券
     * @param customerUser
     * @param storeUserInfo
     * @return
     */
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



    @Override
    public boolean getAvailableCoupon(Long customerId) {
        //指定具体人
        List<CouponPushVO> couponPushVOS = couponPushCustomerMapper.selectCouponPushCustomer(customerId);

        boolean falg = false;
        falg = isAvailable(couponPushVOS);
        if(falg){
            return falg;
        }

        //指定门店
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerId);
        List<CouponPushVO> couponPushCustomers = couponPushCustomerMapper.selectCouponPushCustomer(result.getData().getId());
        falg = isAvailable(couponPushCustomers);

        return falg;
    }

    private boolean isAvailable(List<CouponPushVO> couponPushVOS) {
        boolean falg = false;
        //不可领取活动ID集合
        List<Long> unActiveIds = new ArrayList<>();
        CouponActivityRecord car = new CouponActivityRecord();
        for(CouponPushVO cpv : couponPushVOS){
            if(unActiveIds.contains(cpv.getActivityId())){
                continue;
            }else{
                car.setCouponActivityId(cpv.getActivityId());
                car.setCustomerId(UserContext.getCurrentCustomerUser().getCustomerId());
                Integer count = couponActivityRecordMapper.checkCustomerJoinActive(car);
                if(count>0){
                    //已参加过此活动
                    unActiveIds.add(cpv.getActivityId());
                    continue;
                }else {
                    Long usedNum = couponPushCustomerMapper.countUsedCouponNum(cpv);
                    if((cpv.getCouponNum() == null ? 0:cpv.getCouponNum()) > usedNum){
                        //优惠券数量大于 已使用数量
                        falg =  true;
                        break;
                    }else {
                        unActiveIds.add(cpv.getActivityId());
                    }
                }
            }
        }
        return falg;
    }

}
