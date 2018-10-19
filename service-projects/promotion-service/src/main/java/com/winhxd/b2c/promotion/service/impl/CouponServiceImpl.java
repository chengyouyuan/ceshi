package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCartQueryCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.product.condition.BrandCondition;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.*;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.domain.store.condition.StoreCustomerRegionCondition;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.feign.order.ShopCartServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.common.util.DateDealUtils;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 59
 * @Description
 */
@Service
public class CouponServiceImpl implements CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    // 单商品优惠券
    private static final int SINGLE_BRAND = 1;

    @Autowired
    CouponActivityMapper couponActivityMapper;

    @Autowired
    CouponActivityRecordMapper couponActivityRecordMapper;
    @Autowired
    CouponTemplateSendMapper couponTemplateSendMapper;
    @Autowired
    CouponActivityTemplateMapper couponActivityTemplateMapper;
    @Autowired
    CouponActivityStoreCustomerMapper couponActivityStoreCustomerMapper;
    @Autowired
    CouponMapper couponMapper;
    @Autowired
    CouponTemplateUseMapper couponTemplateUseMapper;
    @Autowired
    CouponApplyBrandMapper couponApplyBrandMapper;
    @Autowired
    CouponApplyBrandListMapper couponApplyBrandListMapper;
    @Autowired
    CouponApplyProductMapper couponApplyProductMapper;
    @Autowired
    CouponApplyProductListMapper couponApplyProductListMapper;
    @Autowired
    CouponTemplateMapper couponTemplateMapper;
    @Autowired
    CouponInvestorMapper couponInvestorMapper;
    @Autowired
    CouponInvestorDetailMapper couponInvestorDetailMapper;
    @Autowired
    CouponApplyMapper couponApplyMapper;
    @Autowired
    CouponGradeDetailMapper couponGradeDetailMapper;
    @Autowired
    StoreServiceClient storeServiceClient;
    @Autowired
    ProductServiceClient productServiceClient;
    @Autowired
    OrderServiceClient orderServiceClient;
    @Autowired
    ShopCartServiceClient shopCartServiceClient;
    @Autowired
    private Cache cache;
    private static final int BACKROLL_LOCK_EXPIRES_TIME = 3000;

    @Autowired
    CouponPushCustomerMapper couponPushCustomerMapper;

    @Override
    public List<CouponVO> getNewUserCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType(CouponActivityEnum.NEW_USER.getCode());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
        couponActivity.setType(CouponActivityEnum.PUSH_COUPON.getCode());
        List<CouponActivity> couponActivities = couponActivityMapper.selectByExample(couponActivity);
        if (couponActivities.isEmpty()) {
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }

        CouponActivityRecord activityRecord = new CouponActivityRecord();
        activityRecord.setCustomerId(customerUser.getCustomerId());
        activityRecord.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityRecord> couponActivityRecords = couponActivityRecordMapper.selectByExample(activityRecord);
        if (!couponActivityRecords.isEmpty()) {
            logger.error("该手机号已经享受过新用户福利");
            throw new BusinessException(BusinessCode.CODE_500002);
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if (couponActivityTemplates.isEmpty()) {
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }
        //step2 向用户推券
        for (CouponActivityTemplate activityTemplate : couponActivityTemplates) {
            //推送数量
            for (int i = 0; i < activityTemplate.getSendNum(); i++) {
                CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
                couponTemplateSend.setStatus(CouponActivityEnum.NOT_USE.getCode());
                couponTemplateSend.setTemplateId(activityTemplate.getTemplateId());
                couponTemplateSend.setSource((int) CouponActivityEnum.SYSTEM.getCode());
                couponTemplateSend.setSendRole((int) CouponActivityEnum.ORDINARY_USER.getCode());
                couponTemplateSend.setCustomerId(customerUser.getCustomerId());
                couponTemplateSend.setCustomerMobile("");
                couponTemplateSend.setCount(1);
                couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
                couponTemplateSend.setCreated(new Date());
                couponTemplateSend.setCreatedByName("");

                if (activityTemplate.getEffectiveDays() == null) {
                    couponTemplateSend.setStartTime(activityTemplate.getStartTime());
                    couponTemplateSend.setEndTime(activityTemplate.getEndTime());
                } else {
                    couponTemplateSend.setStartTime(DateDealUtils.getStartDate(new Date()));
                    couponTemplateSend.setEndTime(DateDealUtils.getEndDate(new Date(), activityTemplate.getEffectiveDays()));
                }

                couponTemplateSendMapper.insertSelective(couponTemplateSend);

                CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
                couponActivityRecord.setCouponActivityId(activityTemplate.getCouponActivityId());
                couponActivityRecord.setCustomerId(customerUser.getCustomerId());
                couponActivityRecord.setSendId(couponTemplateSend.getId());
                couponActivityRecord.setTemplateId(activityTemplate.getTemplateId());
                couponActivityRecord.setCreated(new Date());
                couponActivityRecord.setCreatedBy(customerUser.getCustomerId());
                couponActivityRecord.setCreatedByName("");
                couponActivityRecordMapper.insertSelective(couponActivityRecord);
            }
        }
        //step3 返回数据
        List<CouponVO> couponVOS = couponActivityMapper.selectNewUserCouponList(customerUser.getCustomerId(), CouponActivityEnum.NEW_USER.getCode());

        return this.getCouponDetail(couponVOS);
    }

    /**
     * 获取优惠券详情
     *
     * @param couponVOS
     * @return
     */
    public List<CouponVO> getCouponDetail(List<CouponVO> couponVOS) {
        for (CouponVO couponVO : couponVOS) {
            if (String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()).equals(couponVO.getApplyRuleType())) {
                List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                if (!couponApplyProducts.isEmpty()) {
                    List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                    //组装请求的参数
                    List<String> productSkus = couponApplyProductLists.stream()
                            .filter(couponApplyProduct -> StringUtils.isNotBlank(couponApplyProduct.getSkuCode()))
                            .map(CouponApplyProductList::getSkuCode).collect(Collectors.toList());

                    ProductCondition productCondition = new ProductCondition();
                    productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                    productCondition.setProductSkus(productSkus);
                    //调用获取商品信息接口
                    ResponseResult<List<ProductSkuVO>> result = productServiceClient.getProductSkus(productCondition);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}获取商品sku信息接口调用失败:code={}，获取优惠券适用范围异常！~", productCondition, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    couponVO.setProducts(result.getData());
                }
            }

            if (String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()).equals(couponVO.getApplyRuleType())) {
                List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponVO.getApplyId());
                if (!couponApplyBrands.isEmpty()) {
                    List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                    //组装请求的参数
                    List<String> brandCodes = couponApplyBrandLists.stream()
                            .filter(couponApplyBrand -> StringUtils.isNotBlank(couponApplyBrand.getBrandCode()))
                            .map(CouponApplyBrandList::getBrandCode).collect(Collectors.toList());

                    //调用获取商品信息接口
                    ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    couponVO.setBrands(result.getData());

                    getCouponDetailLogo(couponVO, result.getData(), couponApplyBrandLists);
                }
            }

            // 优惠券是否快过期
            if (!String.valueOf(CouponActivityEnum.ALREADY_USE.getCode()).equals(couponVO.getUseStatus())
                    && !String.valueOf(CouponActivityEnum.UNTREAD.getCode()).equals(couponVO.getUseStatus())) {

                if (couponIsFastOverdue(couponVO.getActivityEnd())) {
                    couponVO.setUseStatus(String.valueOf(CouponActivityEnum.FAST_EXPIRED.getCode()));
                }
            }
        }

        return couponVOS;
    }

    /**
     * 获取品牌logo或者品牌商logo
     *
     * @param vo
     * @param result
     * @param couponApplyBrandLists
     */
    private void getCouponDetailLogo(CouponVO vo, List<BrandVO> result, List<CouponApplyBrandList> couponApplyBrandLists) {
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

    /**
     * 优惠券是否快过期
     */
    private boolean couponIsFastOverdue(Date activityEnd) {
        if (null != activityEnd) {
            // 快过期时间为活动结束前三天
            if (activityEnd.getTime() - System.currentTimeMillis() <= 1000 * 60 * 60 * 24 * 3) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer getCouponNumsByCustomerForStore(Long customerId) {
        Integer sum = couponTemplateSendMapper.getCouponNumsByCustomerForStore(customerId);
        return sum;
    }

    /**
     * 待领取优惠券
     *
     * @return
     */
    @Override
    public List<CouponVO> unclaimedCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，待领取优惠券异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponVO> couponVOS = couponActivityMapper.selectUnclaimedCouponList(storeUserInfo.getId());
        int size = CollectionUtils.isEmpty(couponVOS) ? 0 : couponVOS.size();
        List<CouponVO> results = new ArrayList<>(size);
        for (CouponVO couponVO : couponVOS) {
            //根据优惠券总数限制用户领取
            if (couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.COUPON_SUM.getCode()))) {
                //获取某个优惠券领取总数量
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(), couponVO.getTemplateId());
                //获取某个优惠券用户领取的数量
                int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(), couponVO.getTemplateId(), customerUser.getCustomerId());
                if (templateNum < couponVO.getCouponNum()) {
                    //limitNum为空代表不限制用户领取数量
                    if (couponVO.getLimitNum() == null) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                    } else {
                        if (userNum < couponVO.getLimitNum()) {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                        } else {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                        }
                    }
                } else {
                    if (userNum > 0) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                    } else {
                        // 优惠券已领完
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.HAVE_FINISHED.getCode());
                    }
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if (String.valueOf(CouponActivityEnum.STORE_NUM.getCode()).equals(couponVO.getCouponNumType())) {
                //获取某个优惠券门店领取的数量
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(), couponVO.getTemplateId(), storeUserInfo.getId());
                //获取某个优惠券用户领取的数量
                int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(), couponVO.getTemplateId(), customerUser.getCustomerId());
                if (storeNum < couponVO.getCouponNum()) {
                    //limitNum为空代表不限制用户领取数量
                    if (couponVO.getLimitNum() == null) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                    } else {
                        if (userNum < couponVO.getLimitNum()) {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                        } else {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                        }
                    }
                } else {
                    if (userNum > 0) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                    } else {
                        // 当前门店优惠券已领完
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.HAVE_FINISHED.getCode());
                    }
                }
            }
            results.add(couponVO);
        }

        results.sort((a, b) -> Integer.parseInt(a.getReceiveStatus()) - Integer.parseInt(b.getReceiveStatus()));
        return this.getCouponDetail(results);
    }

    @Override
    public PagedList<CouponVO> myCouponList(CouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        //我的优惠券列表不分页，一页显示全部
        if (null == couponCondition.getStatus()) {
            List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(), null, couponCondition.getStatus());
            couponCondition.setPageSize(couponVOS.size());
            couponCondition.setPageNo(1);
        }

        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(), null, couponCondition.getStatus());

        pagedList.setData(this.getCouponDetail(couponVOS));
        pagedList.setPageNo(couponCondition.getPageNo());
        pagedList.setPageSize(couponCondition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public Boolean userReceiveCoupon(ReceiveCouponCondition condition) {
        String lockKey = CacheName.RECEIVE_COUPON + condition.getCouponActivityId() + condition.getTemplateId();
        Lock lock = new RedisLock(cache, lockKey, BACKROLL_LOCK_EXPIRES_TIME);
        try {
            lock.lock();
            CustomerUser customerUser = UserContext.getCurrentCustomerUser();

            CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
            couponActivityTemplate.setCouponActivityId(condition.getCouponActivityId());
            couponActivityTemplate.setTemplateId(condition.getTemplateId());
            List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
            if (couponActivityTemplates.isEmpty()) {
                logger.error("CouponServiceImpl.userReceiveCoupon 不存在的优惠券");
                throw new BusinessException(BusinessCode.CODE_500001);
            }

            List<CouponActivityStoreCustomer> couponActivityStoreCustomers = couponActivityStoreCustomerMapper.selectByTemplateId(couponActivityTemplates.get(0).getId());

            ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
            if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                logger.error("优惠券：{}获取门店信息接口调用失败:code={}，待领取优惠券异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
                throw new BusinessException(result.getCode());
            }
            StoreUserInfoVO storeUserInfo = result.getData();
            //领取之前校验是否可领取
            if (!this.checkReceiptStatus(couponActivityTemplates, storeUserInfo, customerUser)) {
                throw new BusinessException(BusinessCode.CODE_500017);
            }
            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setStatus(CouponActivityEnum.NOT_USE.getCode());
            couponTemplateSend.setTemplateId(condition.getTemplateId());
            couponTemplateSend.setSource((int) CouponActivityEnum.SYSTEM.getCode());
            couponTemplateSend.setSendRole((int) CouponActivityEnum.ORDINARY_USER.getCode());
            couponTemplateSend.setCustomerId(customerUser.getCustomerId());
            couponTemplateSend.setCustomerMobile("");
            couponTemplateSend.setCount(1);
            couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
            couponTemplateSend.setCreated(new Date());
            couponTemplateSend.setCreatedByName("");

            if (couponActivityTemplates.get(0).getEffectiveDays() == null) {
                couponTemplateSend.setStartTime(couponActivityTemplates.get(0).getStartTime());
                couponTemplateSend.setEndTime(couponActivityTemplates.get(0).getEndTime());
            } else {
                couponTemplateSend.setStartTime(DateDealUtils.getStartDate(new Date()));
                couponTemplateSend.setEndTime(DateDealUtils.getEndDate(new Date(), couponActivityTemplates.get(0).getEffectiveDays()));
            }
            couponTemplateSendMapper.insertSelective(couponTemplateSend);

            CouponActivityRecord couponActivityRecord = new CouponActivityRecord();
            couponActivityRecord.setCouponActivityId(condition.getCouponActivityId());
            couponActivityRecord.setCustomerId(customerUser.getCustomerId());
            couponActivityRecord.setSendId(couponTemplateSend.getId());
            couponActivityRecord.setTemplateId(condition.getTemplateId());
            couponActivityRecord.setCreated(new Date());
            couponActivityRecord.setCreatedBy(customerUser.getCustomerId());
            couponActivityRecord.setCreatedByName("");
            if (couponActivityStoreCustomers.isEmpty()) {
                couponActivityRecord.setStoreId(null);
            } else {
                couponActivityRecord.setStoreId(storeUserInfo.getId());
            }
            couponActivityRecordMapper.insertSelective(couponActivityRecord);
            //领完之后校验是否下次可领取
            if (!this.checkReceiptStatus(couponActivityTemplates, storeUserInfo, customerUser)) {
                return false;
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    @Override
    public Boolean orderUseCoupon(OrderUseCouponCondition condition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        for (int i = 0; i < condition.getSendIds().size(); i++) {
            CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(condition.getSendIds().get(i));
            couponTemplateSend.setStatus(CouponActivityEnum.ALREADY_USE.getCode());
            couponTemplateSend.setUpdated(new Date());
            couponTemplateSend.setUpdatedBy(customerUser.getCustomerId());
            couponTemplateSend.setUpdatedByName("");
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);

            CouponTemplateUse couponTemplateUse = new CouponTemplateUse();
            couponTemplateUse.setSendId(couponTemplateSend.getId());
            couponTemplateUse.setTemplateId(couponTemplateSend.getTemplateId());
            couponTemplateUse.setOrderNo(condition.getOrderNo());
            couponTemplateUse.setStatus(CouponActivityEnum.ALREADY_USE.getCode());
            couponTemplateUse.setCouponPrice(condition.getCouponPrice());
            couponTemplateUse.setOrderPrice(condition.getOrderPrice());
            couponTemplateUse.setCustomerMonbile("");
            couponTemplateUse.setCustomerId(customerUser.getCustomerId());
            couponTemplateUse.setCreated(new Date());
            couponTemplateUse.setCreatedBy(customerUser.getCustomerId());
            couponTemplateUse.setCreatedByName("");
            couponTemplateUseMapper.insertSelective(couponTemplateUse);
        }
        return true;
    }

    @Override
    public Boolean orderUntreadCoupon(OrderUntreadCouponCondition condition) {
        logger.info("根据订单退优惠券,订单号" + condition.getOrderNo());
        List<CouponTemplateUse> couponTemplateUses = couponTemplateUseMapper.selectByOrderNo(condition.getOrderNo());
        for (CouponTemplateUse couponTemplateUse : couponTemplateUses) {
            Integer activeStatus = couponTemplateUseMapper.selectActiveStatusByOrderNo(couponTemplateUse);
            logger.info("订单号" + condition.getOrderNo() + "退优惠券 活动状态：" + activeStatus);
            short couponStatus = activeStatus.intValue() == 3 ? CouponActivityEnum.INVALYD.getCode() : CouponActivityEnum.UNTREAD.getCode();
            logger.info("订单号" + condition.getOrderNo() + "退优惠券 退回状态：" + couponStatus);


            couponTemplateUse.setStatus(couponStatus);
            couponTemplateUseMapper.updateByPrimaryKeySelective(couponTemplateUse);

            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(couponTemplateUse.getSendId());
            couponTemplateSend.setStatus(couponStatus);
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);
        }
        logger.info("订单退优惠券结束,订单号" + condition.getOrderNo());
        return true;
    }

    @EventMessageListener(value = EventTypeHandler.EVENT_CUSTOMER_ORDER_UNTREAD_COUPON_HANDLER)
    public void eventOrderUntreadCoupon(String orderNo, OrderInfo order) {
        if (null == order.getOrderNo()) {
            logger.error("CouponSerciceImpl.orderUntreadCoupon-订单号为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        OrderUntreadCouponCondition condition = new OrderUntreadCouponCondition();
        condition.setOrderNo(order.getOrderNo());
        this.orderUntreadCoupon(condition);
    }

    @Override
    public Boolean revokeCoupon(RevokeCouponCodition condition) {
        for (int i = 0; i < condition.getSendIds().size(); i++) {
            logger.info("=撤回优惠券,优惠券发放表ID{}", condition.getSendIds());
            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(condition.getSendIds().get(i));
            couponTemplateSend.setStatus(CouponActivityEnum.INVALYD.getCode());
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);
        }
        return true;
    }

    /**
     * 查询订单使用的优惠券列表
     *
     * @param couponCondition
     * @return
     */
    @Override
    public List<CouponVO> couponListByOrder(OrderCouponCondition couponCondition) {
        List<CouponVO> couponVOS = couponMapper.couponListByOrder(couponCondition.getOrderNo());
        return this.getCouponDetail(couponVOS);
    }

    @Override
    public CouponDiscountVO couponDiscountAmount(CouponPreAmountCondition couponCondition) {
        CouponDiscountVO couponDiscountVO = new CouponDiscountVO();
        couponDiscountVO.setDiscountAmount(BigDecimal.valueOf(0));

        CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(couponCondition.getSendIds().get(0));
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(couponTemplateSend.getTemplateId());
        CouponApply couponApply = couponApplyMapper.selectByPrimaryKey(couponTemplate.getApplyRuleId());

        couponDiscountVO.setCouponTitle(couponTemplate.getTitle());
        //通用券，按订单金额计算优惠金额
        if (String.valueOf(CouponApplyEnum.COMMON_COUPON.getCode()).equals(couponApply.getApplyRuleType())) {
            //计算订单总额
            BigDecimal amountPrice = new BigDecimal(0);
            for (CouponProductCondition couponProductCondition : couponCondition.getProducts()) {
                if (couponProductCondition.getPrice() != null && couponProductCondition.getSkuNum() != null) {
                    BigDecimal productPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                    amountPrice = amountPrice.add(productPrice);
                }
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(), amountPrice);
            logger.info("couponDiscountAmount-通用券-订单总额{},优惠金额{}", amountPrice, discountAmount);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }
        //品牌券，按品牌总额计算优惠金额
        if (String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()).equals(couponApply.getApplyRuleType())) {
            List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponApply.getId());
            if (couponApplyBrands.isEmpty()) {
                logger.error("优惠券适用的品牌参数不存在");
                return couponDiscountVO;
            }
            List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());

            BigDecimal amountPrice = new BigDecimal(0);
            //遍历适用的品牌并计算金额
            for (CouponProductCondition couponProductCondition : couponCondition.getProducts()) {
                for (CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists) {
                    if (couponProductCondition.getBrandCode().equals(couponApplyBrandList.getBrandCode()) && couponProductCondition.getBrandBusinessCode().equals(couponApplyBrandList.getCompanyCode())) {
                        if (couponProductCondition.getPrice() != null && couponProductCondition.getSkuNum() != null) {
                            BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                            amountPrice = amountPrice.add(brandProductPrice);
                        }
                    }
                }
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(), amountPrice);
            logger.info("couponDiscountAmount-品牌券-该品牌商品总额{},优惠金额{}", amountPrice, discountAmount);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }

        //商品券，按商品总额计算优惠金额
        if (String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()).equals(couponApply.getApplyRuleType())) {
            List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponApply.getId());
            if (couponApplyProducts.isEmpty()) {
                logger.error("优惠券适用的商品参数不存在");
                return couponDiscountVO;
            }
            List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());

            BigDecimal amountPrice = new BigDecimal(0);
            //遍历适用的商品并计算金额
            for (CouponProductCondition couponProductCondition : couponCondition.getProducts()) {
                for (CouponApplyProductList couponApplyProductList : couponApplyProductLists) {
                    if (couponProductCondition.getSkuCode().equals(couponApplyProductList.getSkuCode())) {
                        if (couponProductCondition.getPrice() != null && couponProductCondition.getSkuNum() != null) {
                            BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                            amountPrice = amountPrice.add(brandProductPrice);
                        }
                    }
                }
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(), amountPrice);
            logger.info("couponDiscountAmount-商品券-该商品总额{},优惠金额{}", amountPrice, discountAmount);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }
        return couponDiscountVO;
    }

    /**
     * 计算优惠金额
     *
     * @param gradeId     坎级ID
     * @param amountPrice
     * @return
     */
    public BigDecimal computeAumont(Long gradeId, BigDecimal amountPrice) {

        //查询满减规则
        List<CouponGradeDetail> couponGradeDetails = couponGradeDetailMapper.selectByGradeId(gradeId);
        CouponGradeDetail couponGradeDetail = couponGradeDetails.get(0);
        //优惠类型 1.金额2，折扣
        if (String.valueOf(CouponGradeEnum.UP_TO_REDUCE_CASH.getCode()).equals(couponGradeDetail.getReducedType())) {

            //满减金额等于0 代表无门槛券
            if (couponGradeDetail.getReducedAmt().compareTo(BigDecimal.valueOf(0)) == 0) {
                //总额大于等于优惠金额返回优惠金额否则返回总额
                return amountPrice.compareTo(couponGradeDetail.getDiscountedAmt()) >= 0 ? couponGradeDetail.getDiscountedAmt() : amountPrice;
            }

            //总额大于等于满减金额满足使用条件返回优惠金额否则返回0
            return amountPrice.compareTo(couponGradeDetail.getReducedAmt()) >= 0 ? couponGradeDetail.getDiscountedAmt() : new BigDecimal(0);
        } else {
            //计算优惠金额
            BigDecimal discountAmount = amountPrice.multiply(couponGradeDetail.getDiscounted()).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP);

            //满减金额等于0 代表无门槛券
            if (couponGradeDetail.getReducedAmt().compareTo(BigDecimal.valueOf(0)) == 0) {
                //总额大于等于满减金额,返回满减金额否则返回总额
                // 折扣券不存在优惠金额大于总额
            }

            //优惠金额大于等于优惠最大限额,取优惠最大限额
            if (discountAmount.compareTo(couponGradeDetail.getDiscountedMaxAmt()) >= 0) {
                return couponGradeDetail.getDiscountedMaxAmt();
            } else {
                return discountAmount;
            }
        }
    }

    @Override
    public Boolean checkCouponStatus(CouponCheckStatusCondition condition) {
        int count = couponTemplateSendMapper.checkCouponStatus(condition);
        if (count > 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 用户查询门店优惠券列表
     *
     * @return
     */
    @Override
    public List<CouponVO> findStoreCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，用户查询门店优惠券列表异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponVO> couponVOS = couponActivityMapper.selectStoreCouponList(storeUserInfo.getId());
        List<CouponVO> results = new ArrayList<>();
        for (CouponVO couponVO : couponVOS) {
            //根据优惠券总数限制用户领取
            logger.info("findStoreCouponList-优惠券数量的限制类型{},数量{}", couponVO.getCouponNumType(), couponVO.getCouponNum());
            if (couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.COUPON_SUM.getCode()))) {
                //获取某个优惠券领取总数量
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(), couponVO.getTemplateId());
                logger.info("findStoreCouponList-优惠券总数{},已领取了{}张", couponVO.getCouponNum(), templateNum);
                //获取某个优惠券用户领取的数量
                int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(), couponVO.getTemplateId(), customerUser.getCustomerId());
                logger.info("findStoreCouponList-用户{}可领取{}张,已领取{}", customerUser.getCustomerId(), couponVO.getLimitNum(), userNum);
                if (templateNum < couponVO.getCouponNum()) {
                    //limitNum为空代表不限制用户领取数量
                    if (couponVO.getLimitNum() == null) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                    } else {
                        if (userNum < couponVO.getLimitNum()) {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                        } else {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                            continue;
                        }
                    }
                } else {
                    // 优惠券已领完
                    couponVO.setReceiveStatus(CouponReceiveStatusEnum.HAVE_FINISHED.getCode());
                    continue;
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if (couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.STORE_NUM.getCode()))) {
                //获取某个优惠券门店领取的数量
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(), couponVO.getTemplateId(), storeUserInfo.getId());
                logger.info("findStoreCouponList-门店{}可领取{}张,已领取了{}张优惠券", storeUserInfo.getId(), couponVO.getCouponNum(), storeNum);
                //获取某个优惠券用户领取的数量
                int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(), couponVO.getTemplateId(), customerUser.getCustomerId());
                logger.info("findStoreCouponList-用户{}可领取{}张,已领取{}", customerUser.getCustomerId(), couponVO.getLimitNum(), userNum);
                if (storeNum < couponVO.getCouponNum()) {
                    //limitNum为空代表不限制用户领取数量
                    if (couponVO.getLimitNum() == null) {
                        couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                    } else {
                        if (userNum < couponVO.getLimitNum()) {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.CAN_RECEIVED.getCode());
                        } else {
                            couponVO.setReceiveStatus(CouponReceiveStatusEnum.ALREADY_RECEIVED.getCode());
                            continue;
                        }
                    }
                } else {
                    // 当前门店优惠券已领完
                    couponVO.setReceiveStatus(CouponReceiveStatusEnum.HAVE_FINISHED.getCode());
                    continue;
                }
            }
            results.add(couponVO);
        }
        results.sort((a, b) -> Integer.parseInt(b.getReceiveStatus()) - Integer.parseInt(a.getReceiveStatus()));
        return this.getCouponDetail(results);
    }

    /**
     * 订单可用优惠券
     *
     * @param couponCondition
     * @return
     */
    @Override
    public List<CouponVO> availableCouponListByOrder(OrderAvailableCouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        //获取购物车商品信息
        ShopCartQueryCondition shopCarQueryCondition = new ShopCartQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        shopCarQueryCondition.setCustomerId(customerUser.getCustomerId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，订单可用优惠券接口异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();
        logger.info("availableCouponListByOrder-购物车信息{}", shopCarProdInfoVOS.toString());

        //查询当前用户下的所有优惠券
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(), null, null);
        // 初始化list 的size
        int size = CollectionUtils.isEmpty(couponVOS) ? 0 : couponVOS.size();
        List<CouponVO> results = new ArrayList<>(size);
        //遍历添加优惠券是否可用状态
        for (CouponVO couponVO : couponVOS) {
            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.UN_AVAILABLE.getCode());
            //支付方式
            if (couponVO.getPayType().equals(couponCondition.getPayType())) {
                logger.info("availableCouponListByOrder-优惠券支付方式:{},订单支付方式:{},优惠券类型:{}", couponVO.getPayType(), couponCondition.getPayType(), couponVO.getApplyRuleType());
                //通用券
                if (String.valueOf(CouponApplyEnum.COMMON_COUPON.getCode()).equals(couponVO.getApplyRuleType())) {
                    //计算订单总额
                    BigDecimal amountPrice = new BigDecimal(0);
                    for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                        if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                            BigDecimal productPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                            amountPrice = amountPrice.add(productPrice);
                        }
                    }
                    //订单金额大于等于满减金额优惠券可用
                    logger.info("availableCouponListByOrder-通用券-优惠ID:{},订单金额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                    if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                        couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                        results.add(couponVO);
                    }
                } else if (String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()).equals(couponVO.getApplyRuleType())) {
                    //商品券
                    List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                    if (!couponApplyProducts.isEmpty()) {
                        //满足优惠券使用条件的商品金额
                        BigDecimal amountPrice = new BigDecimal(0);
                        //优惠券适用的商品规则
                        List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                        for (CouponApplyProductList couponApplyProduct : couponApplyProductLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                logger.info("availableCouponListByOrder-商品券-优惠ID:{},优惠券适用商品SKU:{},购物车商品SKU:{}", couponVO.getTemplateId(), couponApplyProduct.getSkuCode(), shopCarProdInfoVO.getSkuCode());
                                if (couponApplyProduct.getSkuCode().equals(shopCarProdInfoVO.getSkuCode())) {
                                    if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                                        BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                        amountPrice = amountPrice.add(brandProductPrice);
                                    }
                                }
                            }
                        }
                        //商品金额大于等于满减金额优惠券可用
                        logger.info("availableCouponListByOrder-商品券-优惠ID:{},商品总额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                        if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                            results.add(couponVO);
                        }
                    }
                } else if (String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()).equals(couponVO.getApplyRuleType())) {
                    //品牌券
                    List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponVO.getApplyId());
                    if (!couponApplyBrands.isEmpty()) {
                        //满足优惠券使用条件的商品金额
                        BigDecimal amountPrice = new BigDecimal(0);
                        //优惠券适用的品牌规则
                        List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                        for (CouponApplyBrandList couponApplyBrand : couponApplyBrandLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                logger.info("availableCouponListByOrder-品牌券-优惠ID:{},优惠券适用品牌编码:{},优惠券适用品牌商编码:{},购物车商品品牌编码:{},购物车商品品牌商编码:{}", couponVO.getTemplateId(), couponApplyBrand.getBrandCode(), couponApplyBrand.getCompanyCode(), shopCarProdInfoVO.getBrandCode(), shopCarProdInfoVO.getCompanyCode());
                                if (couponApplyBrand.getBrandCode().equals(shopCarProdInfoVO.getBrandCode()) && couponApplyBrand.getCompanyCode().equals(shopCarProdInfoVO.getCompanyCode())) {
                                    if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                                        BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                        amountPrice = amountPrice.add(brandProductPrice);
                                    }
                                }
                            }
                        }
                        //品牌商品金额大于等于满减金额优惠券可用
                        logger.info("availableCouponListByOrder-品牌券-优惠ID:{},商品总额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                        if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                            results.add(couponVO);
                        }
                    }
                }
            }
        }
        return this.getCouponDetail(results);
    }

    @Override
    public CouponKindsVo getStoreCouponKinds() {
        List<CouponVO> couponVOList = findStoreCouponList();
        Integer count = 0;
        for (int i = 0; i < couponVOList.size(); i++) {
            //优惠券是否可领取 0 已领取  1 可领取
            if (CouponReceiveStatusEnum.CAN_RECEIVED.getCode().equals(couponVOList.get(i).getReceiveStatus())) {
                count++;
            }
        }
        CouponKindsVo couponKindsVo = new CouponKindsVo();
        couponKindsVo.setStoreCouponKinds(count);
        return couponKindsVo;
    }


    @Override
    public List<CouponInvestorAmountVO> getCouponInvestorAmount(CouponInvestorAmountCondition condition) {
        List<CouponTemplateUse> couponTemplateUses = couponTemplateUseMapper.selectByOrderNos(condition.getOrderNos());
        if (CollectionUtils.isEmpty(couponTemplateUses)) {
            throw new BusinessException(BusinessCode.CODE_500007);
        }
        // 初始化list size
        List<CouponInvestorAmountVO> couponInvestorAmountVOs = new ArrayList<>(couponTemplateUses.size());
        for (CouponTemplateUse couponTemplateUse : couponTemplateUses) {
            CouponInvestorAmountVO couponInvestorAmountVO = new CouponInvestorAmountVO();
            CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(couponTemplateUse.getTemplateId());
            if (null == couponTemplate) {
                throw new BusinessException(BusinessCode.CODE_500008);
            }

            CouponInvestor couponInvestor = couponInvestorMapper.selectByPrimaryKey(couponTemplate.getInvestorId());
            if (null == couponInvestor) {
                throw new BusinessException(BusinessCode.CODE_500009);
            }

            List<CouponInvestorDetail> couponInvestorDetails = couponInvestorDetailMapper.selectByInvestorId(couponInvestor.getId());
            if (CollectionUtils.isEmpty(couponInvestorDetails)) {
                throw new BusinessException(BusinessCode.CODE_500009);
            }
            //调用订单系统获取订单信息
            ResponseResult<OrderInfoDetailVO4Management> result = orderServiceClient.getOrderDetail4Management(couponTemplateUse.getOrderNo());
            if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                logger.error("优惠券：{}获取订单信息接口调用失败:code={}，获取优惠券费用承担信息异常！~", couponTemplateUse.getOrderNo(), result == null ? null : result.getCode());
                throw new BusinessException(result.getCode());
            }
            BigDecimal discountMoney = result.getData().getOrderInfoDetailVO().getDiscountMoney();

            for (CouponInvestorDetail couponInvestorDetail : couponInvestorDetails) {
                if (couponInvestorDetail.getInvestorType().equals(CouponTemplateEnum.INVERTOR_H_TYPE.getCode())) {
                    couponInvestorAmountVO.setPlatformAmount(discountMoney.multiply(new BigDecimal(couponInvestorDetail.getPercent())));
                } else {
                    couponInvestorAmountVO.setBrandAmount(discountMoney.multiply(new BigDecimal(couponInvestorDetail.getPercent())));
                }
            }
            couponInvestorAmountVOs.add(couponInvestorAmountVO);
        }
        return couponInvestorAmountVOs;
    }

    @Override
    public PagedList<CouponInStoreGetedAndUsedVO> findCouponInStoreGetedAndUsedPage(Long storeId, CouponInStoreGetedAndUsedCodition codition) {
        Page page = PageHelper.startPage(codition.getPageNo(), codition.getPageSize());
        PagedList<CouponInStoreGetedAndUsedVO> pagedList = new PagedList();
        //查询优惠券列表

        List<CouponInStoreGetedAndUsedVO> resultList = couponTemplateMapper.selectCouponInStoreGetedAndUsedPage(storeId);

        ResponseResult<List<Long>> customers = getCustomerListByStoreId(storeId);

        List<CouponInStoreGetedAndUsedVO> countList = null;
        if (!CollectionUtils.isEmpty(customers.getData())) {
            HashMap<String, Object> paraMap = Maps.newHashMap();
            paraMap.put("storeId", storeId);
            paraMap.put("list", customers.getData());
            //查询使用每张优惠券的使用数量和领取数量
            countList = couponTemplateMapper.selectCouponGetedAndUsedCout(paraMap);
        }
        // 获取优惠券推给用户总数
        int pushCount = getPushCount(storeId);


        //将数量拼接到列表
        if (resultList != null) {
            for (int i = 0; i < resultList.size(); i++) {
                CouponInStoreGetedAndUsedVO vo = resultList.get(i);
                if (countList != null) {
                    for (int j = 0; j < countList.size(); j++) {
                        if (vo.getCouponActivityId().equals(countList.get(j).getCouponActivityId()) && vo.getTempleteId().equals(countList.get(j).getTempleteId())) {
                            vo.setTotalCount(countList.get(j).getTotalCount());
                            vo.setUsedCount(countList.get(j).getUsedCount());
                            break;
                        }
                    }
                } else {
                    vo.setTotalCount(0);
                    vo.setUsedCount(0);
                }

                vo.setPushCount(pushCount);
            }
        }
        this.getCouponApplyDetail(resultList);


        pagedList.setData(resultList);
        pagedList.setPageNo(codition.getPageNo());
        pagedList.setPageSize(codition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    /**
     * 获取优惠券推给用户总数
     *
     * @param storeId
     * @return
     */
    private int getPushCount(Long storeId) {
        ResponseResult<List<Long>> customerIdData = getCustomerListByStoreId(storeId);

        int pushCount = 0;
        if (!CollectionUtils.isEmpty(customerIdData.getData())) {
            pushCount = customerIdData.getData().size();
//            int couponPushCustomerCount = couponPushCustomerMapper.selectCouponStoreUser(customerIdData.getData());
//            pushCount = storeBindingUserCount + couponPushCustomerCount;
        }
        return pushCount;
    }

    /**
     * 查询门店下所属的用户
     *
     * @param storeId
     * @return
     */
    private ResponseResult<List<Long>> getCustomerListByStoreId(Long storeId) {
        StoreCustomerRegionCondition regionCondition = new StoreCustomerRegionCondition();
        ArrayList<String> storeUserInfoIds = Lists.newArrayList();
        storeUserInfoIds.add(String.valueOf(storeId));
        regionCondition.setStoreUserInfoIds(storeUserInfoIds);

        return storeServiceClient.findStoreCustomerRegions(regionCondition);
    }


    /**
     * 获取可用最优惠的优惠券
     *
     * @param couponCondition
     * @return
     */
    @Override
    public CouponVO findDefaultCouponByOrder(OrderAvailableCouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        BigDecimal maxDiscountAmount = new BigDecimal(0);
        CouponVO result = new CouponVO();

        List<CouponVO> couponVOs = this.availableCouponListByOrderGroup(couponCondition);
        //获取购物车商品信息
        ShopCartQueryCondition shopCarQueryCondition = new ShopCartQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        shopCarQueryCondition.setCustomerId(customerUser.getCustomerId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，获取最优惠的优惠券信息异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        CouponPreAmountCondition couponPreAmountCondition = new CouponPreAmountCondition();

        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();
        // 添加list初始化size
        List<CouponProductCondition> productConditions = new ArrayList<>(shopCarProdInfoVOS.size());
        for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
            CouponProductCondition couponProductCondition = new CouponProductCondition();
            couponProductCondition.setBrandBusinessCode(shopCarProdInfoVO.getCompanyCode());
            couponProductCondition.setBrandCode(shopCarProdInfoVO.getBrandCode());
            couponProductCondition.setPrice(shopCarProdInfoVO.getPrice());
            couponProductCondition.setSkuNum(shopCarProdInfoVO.getAmount());
            couponProductCondition.setSkuCode(shopCarProdInfoVO.getSkuCode());
            productConditions.add(couponProductCondition);
        }
        couponPreAmountCondition.setProducts(productConditions);
        for (CouponVO couponVO : couponVOs) {
            if (CouponAvailableStatusEnum.AVAILABLE.getCode() == couponVO.getAvailableStatus()) {
                List<Long> sendIds = new ArrayList<>();
                sendIds.add(couponVO.getSendId());
                couponPreAmountCondition.setSendIds(sendIds);
                //计算优惠券优惠金额
                CouponDiscountVO couponDiscountVO = this.couponDiscountAmount(couponPreAmountCondition);

                if (couponDiscountVO.getDiscountAmount().compareTo(maxDiscountAmount) > 0) {
                    maxDiscountAmount = couponDiscountVO.getDiscountAmount();
                    result = couponVO;
                }
            }
        }
        return result;
    }

    @Override
    public CouponDiscountVO getCouponDiscountAmount(CouponAmountCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        //获取购物车商品信息
        ShopCartQueryCondition shopCarQueryCondition = new ShopCartQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        shopCarQueryCondition.setCustomerId(customerUser.getCustomerId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，获取最优惠的优惠券信息异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();

        // 指定list初始化size
        List<CouponProductCondition> productConditions = new ArrayList<>(shopCarProdInfoVOS.size());
        for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
            CouponProductCondition couponProductCondition = new CouponProductCondition();
            couponProductCondition.setBrandBusinessCode(shopCarProdInfoVO.getCompanyCode());
            couponProductCondition.setBrandCode(shopCarProdInfoVO.getBrandCode());
            couponProductCondition.setPrice(shopCarProdInfoVO.getPrice());
            couponProductCondition.setSkuNum(shopCarProdInfoVO.getAmount());
            couponProductCondition.setSkuCode(shopCarProdInfoVO.getSkuCode());
            productConditions.add(couponProductCondition);
        }
        CouponPreAmountCondition couponPreAmountCondition = new CouponPreAmountCondition();
        couponPreAmountCondition.setProducts(productConditions);
        couponPreAmountCondition.setSendIds(couponCondition.getSendIds());
        return this.couponDiscountAmount(couponPreAmountCondition);
    }

    /**
     * 获取优惠券适用范围
     *
     * @param couponVOS
     * @return
     */
    public void getCouponApplyDetail(List<CouponInStoreGetedAndUsedVO> couponVOS) {
        for (CouponInStoreGetedAndUsedVO vo : couponVOS) {
            if (vo.getApplyRuleType() != null && vo.getApplyRuleType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())) {
                List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(vo.getApplyId());
                if (!couponApplyProducts.isEmpty()) {
                    List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                    // 组装请求的参数
                    List<String> productSkus = new ArrayList<>();
                    for (CouponApplyProductList couponApplyProductList : couponApplyProductLists) {
                        productSkus.add(couponApplyProductList.getSkuCode());
                    }
                    ProductCondition productCondition = new ProductCondition();
                    productCondition.setProductSkus(productSkus);
                    //调用获取商品信息接口
                    productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                    ResponseResult<List<ProductSkuVO>> result = productServiceClient.getProductSkus(productCondition);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}获取商品sku信息接口调用失败:code={}，获取优惠券适用范围异常！~", productCondition, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    vo.setProducts(result.getData());
                }
            }

            if (vo.getApplyRuleType() != null && vo.getApplyRuleType().equals(CouponApplyEnum.BRAND_COUPON.getCode())) {
                List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(vo.getApplyId());
                if (!couponApplyBrands.isEmpty()) {
                    List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                    //组装请求的参数
                    List<String> brandCodes = new ArrayList<>();
                    for (CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists) {
                        brandCodes.add(couponApplyBrandList.getBrandCode());
                    }
                    //调用获取商品信息接口
                    ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    vo.setBrands(result.getData());

                    // 获取品牌logo或者品牌商logo
                    getLogo(vo, result.getData(), couponApplyBrandLists);
                }
            }
        }

    }


    /**
     * 获取品牌logo或者品牌商logo
     *
     * @param vo
     * @param result
     * @param couponApplyBrandLists
     */
    private void getLogo(CouponInStoreGetedAndUsedVO vo, List<BrandVO> result, List<CouponApplyBrandList> couponApplyBrandLists) {
        // 优惠券是单品牌商就显示单品牌商logo
        Optional<String> logoUrl = null;
        if (result.size() == SINGLE_BRAND) {
            logoUrl = result.stream()
                    .filter(brand -> StringUtils.isNotBlank(brand.getBrandImg()))
                    .map(brand -> brand.getBrandImg()).findAny();
        }

        if (result.size() > SINGLE_BRAND) {
            BrandCondition condition = new BrandCondition();
            condition.setBrandCode(couponApplyBrandLists.get(0).getBrandCode());
            condition.setCompanyCode(couponApplyBrandLists.get(0).getCompanyCode());

            ResponseResult<PagedList<BrandVO>> brandInfo = productServiceClient.getBrandInfoByPage(condition);
            if (brandInfo != null && !CollectionUtils.isEmpty(brandInfo.getData().getData())) {
                logoUrl = brandInfo.getData().getData().stream()
                        .filter(brand -> StringUtils.isNotBlank(brand.getCompanyLogo()))
                        .map(brand -> brand.getCompanyLogo()).findAny();
            }
        }

        if (logoUrl.isPresent()) {
            vo.setLogoUrl(logoUrl.get());
        }
    }


    @Override
    public Boolean verifyNewUserActivity() {
        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType(CouponActivityEnum.NEW_USER.getCode());
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_EFFICTIVE.getCode());
        couponActivity.setActivityStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
        couponActivity.setType(CouponActivityEnum.PUSH_COUPON.getCode());
        List<CouponActivity> couponActivities = couponActivityMapper.selectByExample(couponActivity);
        if (couponActivities.isEmpty()) {
            logger.info("不存在符合新用户注册的优惠券活动");
            return false;
        }


        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if (couponActivityTemplates.isEmpty()) {
            logger.info("couponActivityTemplates->不存在符合新用户注册的优惠券活动");
            return false;
        }
        return true;
    }

    /**
     * 校验优惠券是否可领取
     *
     * @param num
     * @param activityTemplate
     * @param customerUser
     * @return
     */
    private boolean checkCouponIsReceive(int num, CouponActivityTemplate activityTemplate, CustomerUser customerUser) {
        if (num < activityTemplate.getCouponNum()) {
            //limitNum为空代表不限制用户领取数量
            if (activityTemplate.getCustomerVoucherLimitNum() == null) {
                // 可领取
                return true;
            }
            //获取某个优惠券用户领取的数量
            int userNum = couponMapper.getCouponNumByCustomerId(activityTemplate.getCouponActivityId(), activityTemplate.getTemplateId(), customerUser.getCustomerId());
            logger.info("用户{}可领取{}张,已领取{}", customerUser.getCustomerId(), activityTemplate.getCustomerVoucherLimitNum(), userNum);
            if (userNum >= activityTemplate.getCustomerVoucherLimitNum()) {
                //不可领取
                return false;
            }
            // 可领取
            return true;
        }
        // 优惠券已领完
        return false;
    }

    /**
     * 校验优惠券是否可领取
     *
     * @param couponActivityTemplates
     * @param storeUserInfo
     * @param customerUser
     * @return
     */
    public Boolean checkReceiptStatus(List<CouponActivityTemplate> couponActivityTemplates, StoreUserInfoVO storeUserInfo, CustomerUser customerUser) {
        //领取之前校验优惠券是否可领取
        //默认可以领取
        Boolean result = true;
        for (CouponActivityTemplate activityTemplate : couponActivityTemplates) {
            logger.info("优惠券数量的限制类型{},数量{}", activityTemplate.getCouponNumType(), activityTemplate.getCouponNum());
            //根据优惠券总数限制用户领取
            if (activityTemplate.getCouponNumType() == CouponActivityEnum.COUPON_SUM.getCode()) {
                //获取某个优惠券领取总数量
                int templateNum = couponMapper.getCouponNumByTemplateId(activityTemplate.getCouponActivityId(), activityTemplate.getTemplateId());
                logger.info("优惠券总数{},已领取了{}张", activityTemplate.getCouponNum(), templateNum);

                result = checkCouponIsReceive(templateNum, activityTemplate, customerUser);
            } else if (activityTemplate.getCouponNumType() == CouponActivityEnum.STORE_NUM.getCode()) {
                //根据每个门店可领取的优惠券数量限制用户领取
                //获取某个优惠券门店领取的数量
                int storeNum = couponMapper.getCouponNumByStoreId(activityTemplate.getCouponActivityId(), activityTemplate.getTemplateId(), storeUserInfo.getId());
                logger.info("门店{}可领取{}张,已领取了{}张优惠券", storeUserInfo.getId(), activityTemplate.getCouponNum(), storeNum);

                result = checkCouponIsReceive(storeNum, activityTemplate, customerUser);
            }
        }
        return result;
    }

    /**
     * 订单可用优惠券分组
     *
     * @param couponCondition
     * @return
     */
    @Override
    public List<CouponVO> availableCouponListByOrderGroup(OrderAvailableCouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();

        if (null == couponCondition.getStoreId() || null == couponCondition.getPayType()) {
            logger.error("CouponServiceImpl.availableCouponListByOrder 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }

        //获取购物车商品信息
        ShopCartQueryCondition shopCarQueryCondition = new ShopCartQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        shopCarQueryCondition.setCustomerId(customerUser.getCustomerId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，订单可用优惠券接口异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();
        logger.info("availableCouponListByOrder-购物车信息{}", shopCarProdInfoVOS.toString());

        //查询当前用户下的所有优惠券
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponListGroup(customerUser.getCustomerId(), null, null);
        // 初始化list的size
        List<CouponVO> results = null;
        if (!CollectionUtils.isEmpty(couponVOS)) {
            results = new ArrayList<>(couponVOS.size());
        }
        //遍历添加优惠券是否可用状态
        for (CouponVO couponVO : couponVOS) {
            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.UN_AVAILABLE.getCode());
            //支付方式
            if (couponVO.getPayType().equals(couponCondition.getPayType())) {
                logger.info("availableCouponListByOrder-优惠券支付方式:{},订单支付方式:{},优惠券类型:{}", couponVO.getPayType(), couponCondition.getPayType(), couponVO.getApplyRuleType());
                //通用券
                if (couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.COMMON_COUPON.getCode()))) {
                    //计算订单总额
                    BigDecimal amountPrice = new BigDecimal(0);
                    for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                        if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                            BigDecimal productPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                            amountPrice = amountPrice.add(productPrice);
                        }
                    }
                    //订单金额大于等于满减金额优惠券可用
                    logger.info("availableCouponListByOrder-通用券-优惠ID:{},订单金额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                    if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                        couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                        results.add(couponVO);
                    }
                } else if (couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()))) {
                    //商品券
                    List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                    if (!couponApplyProducts.isEmpty()) {
                        //满足优惠券使用条件的商品金额
                        BigDecimal amountPrice = new BigDecimal(0);
                        //优惠券适用的商品规则
                        List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                        for (CouponApplyProductList couponApplyProduct : couponApplyProductLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                logger.info("availableCouponListByOrder-商品券-优惠ID:{},优惠券适用商品SKU:{},购物车商品SKU:{}", couponVO.getTemplateId(), couponApplyProduct.getSkuCode(), shopCarProdInfoVO.getSkuCode());
                                if (couponApplyProduct.getSkuCode().equals(shopCarProdInfoVO.getSkuCode())) {
                                    if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                                        BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                        amountPrice = amountPrice.add(brandProductPrice);
                                    }
                                }
                            }
                        }
                        //商品金额大于等于满减金额优惠券可用
                        logger.info("availableCouponListByOrder-商品券-优惠ID:{},商品总额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                        if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                            results.add(couponVO);
                        }
                    }
                } else if (couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()))) {
                    //品牌券
                    List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponVO.getApplyId());
                    if (!couponApplyBrands.isEmpty()) {
                        //满足优惠券使用条件的商品金额
                        BigDecimal amountPrice = new BigDecimal(0);
                        //优惠券适用的品牌规则
                        List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                        for (CouponApplyBrandList couponApplyBrand : couponApplyBrandLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                logger.info("availableCouponListByOrder-品牌券-优惠ID:{},优惠券适用品牌编码:{},优惠券适用品牌商编码:{},购物车商品品牌编码:{},购物车商品品牌商编码:{}", couponVO.getTemplateId(), couponApplyBrand.getBrandCode(), couponApplyBrand.getCompanyCode(), shopCarProdInfoVO.getBrandCode(), shopCarProdInfoVO.getCompanyCode());
                                if (couponApplyBrand.getBrandCode().equals(shopCarProdInfoVO.getBrandCode()) && couponApplyBrand.getCompanyCode().equals(shopCarProdInfoVO.getCompanyCode())) {
                                    if (shopCarProdInfoVO.getPrice() != null && shopCarProdInfoVO.getAmount() != null) {
                                        BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                        amountPrice = amountPrice.add(brandProductPrice);
                                    }
                                }
                            }
                        }
                        //品牌商品金额大于等于满减金额优惠券可用
                        logger.info("availableCouponListByOrder-品牌券-优惠ID:{},商品总额:{},满减金额:{}", couponVO.getTemplateId(), amountPrice, couponVO.getReducedAmt());
                        if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                            couponVO.setAvailableStatus((int) CouponAvailableStatusEnum.AVAILABLE.getCode());
                            results.add(couponVO);
                        }
                    }
                }
            }
        }
        return this.getCouponDetail(results);
    }
}
