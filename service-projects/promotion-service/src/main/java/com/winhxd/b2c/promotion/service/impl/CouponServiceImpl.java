package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponGradeEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponDiscountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInStoreGetedAndUsedVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorAmountVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Auther wangxiaoshun
 * @Date 2018/8/6 13 59
 * @Description
 */
@Service
public class CouponServiceImpl implements CouponService {
    private static final Logger logger = LoggerFactory.getLogger(CouponServiceImpl.class);

    @Autowired
    CouponActivityMapper couponActivityMapper;

    @Autowired
    CouponActivityRecordMapper couponActivityRecordMapper;
    @Autowired
    CouponTemplateSendMapper couponTemplateSendMapper;
    @Autowired
    CouponActivityTemplateMapper couponActivityTemplateMapper;
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
    CouponGradeMapper couponGradeMapper;
    @Autowired
    CouponApplyMapper couponApplyMapper;
    @Autowired
    CouponGradeDetailMapper couponGradeDetailMapper;
    @Autowired
    StoreServiceClient storeServiceClient;
    @Autowired
    ProductServiceClient productServiceClient;


    @Override
    public List<CouponVO> getNewUserCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType((short)1);
        couponActivity.setStatus((short)1);
        couponActivity.setActivityStatus((short)1);
        couponActivity.setType((short)2);
        List<CouponActivity> couponActivities = couponActivityMapper.selectByExample(couponActivity);
        if(couponActivities.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }

        CouponActivityRecord activityRecord = new CouponActivityRecord();
        activityRecord.setCustomerId(customerUser.getCustomerId());
        activityRecord.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityRecord> couponActivityRecords = couponActivityRecordMapper.selectByExample(activityRecord);
        if(!couponActivityRecords.isEmpty()){
            logger.error("该手机号已经享受过新用户福利");
            throw new BusinessException(BusinessCode.CODE_500002);
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(couponActivities.get(0).getId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if(couponActivityTemplates.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
        }
        //step2 向用户推券
        for(CouponActivityTemplate activityTemplate : couponActivityTemplates){
            //推送数量
            for(int i=0; i <activityTemplate.getSendNum();i++){
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
                //TODO 用户名称
                couponTemplateSend.setCreatedByName("");

                if(activityTemplate.getEffectiveDays()==null){
                    couponTemplateSend.setStartTime(activityTemplate.getStartTime());
                    couponTemplateSend.setEndTime(activityTemplate.getEndTime());
                }else{
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);

                    couponTemplateSend.setStartTime(calendar.getTime());

                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date());
                    c.add(Calendar.DATE,activityTemplate.getEffectiveDays());
                    c.set(Calendar.HOUR_OF_DAY, 23);
                    c.set(Calendar.MINUTE, 59);
                    c.set(Calendar.SECOND, 59);
                    c.set(Calendar.MILLISECOND, 59);
                    couponTemplateSend.setEndTime(c.getTime());
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
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),1);

        return this.getCouponDetail(couponVOS);
    }

    /**
     * 获取优惠券适用范围
     * @param couponVOS
      * @return
     */
    public List<CouponVO> getCouponDetail(List<CouponVO> couponVOS){
        for(CouponVO couponVO : couponVOS){
            if(couponVO.getApplyRuleType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
                List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                if(!couponApplyProducts.isEmpty()){
                    List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                    //组装请求的参数
                    List<String> productSkus = new ArrayList<>();
                    for(CouponApplyProductList couponApplyProductList : couponApplyProductLists){
                        productSkus.add(couponApplyProductList.getSkuCode());
                    }
                    ProductCondition productCondition = new ProductCondition();
                    productCondition.setProductSkus(productSkus);
                    //调用获取商品信息接口
                    ResponseResult<List<ProductSkuVO>> result = productServiceClient.getProductSkus(productCondition);
                    couponVO.setProducts(result.getData());
                }
            }

            if(couponVO.getApplyRuleType().equals(CouponApplyEnum.BRAND_COUPON.getCode())){
                List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponVO.getApplyId());
                if(!couponApplyBrands.isEmpty()){
                    List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                    //组装请求的参数
                    List<String> brandCodes = new ArrayList<>();
                    for(CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists){
                        brandCodes.add(couponApplyBrandList.getBrandCode());
                    }
                    //调用获取商品信息接口
                    ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                    couponVO.setBrands(result.getData());
                }
            }
        }

        return couponVOS;
    }

	@Override
	public ResponseResult<String> getCouponNumsByCustomerForStore(Long customerId) {
        ResponseResult result = new ResponseResult();
        int sum = couponTemplateSendMapper.getCouponNumsByCustomerForStore(customerId);
        result.setData(sum+"");
		return result;
	}

    /**
     * 待领取优惠券
     * @return
     */
    @Override
    public List<CouponVO> unclaimedCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        StoreUserInfoVO storeUserInfo = result.getData();


        List<CouponVO> couponVOS = couponActivityMapper.selectUnclaimedCouponList(storeUserInfo.getId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.COUPON_SUM.getCode())){
                //获取某个优惠券领取总数量
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    //获取某个优惠券用户领取的数量
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("0");
                    }else{
                        couponVO.setReceiveStatus("1");
                    }
                }else{
                    // 优惠券已领完
                    continue;
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.STORE_NUM.getCode())){
                //获取某个优惠券门店领取的数量
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId());
                if(storeNum < couponVO.getCouponNum()){
                    //获取某个优惠券用户领取的数量
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("0");
                    }else{
                        couponVO.setReceiveStatus("1");
                    }
                }else{
                    // 当前门店优惠券已领完
                    continue;
                }
            }
            results.add(couponVO);
        }
        return this.getCouponDetail(results);
    }

    @Override
    public PagedList<CouponVO> myCouponList(CouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),null);

        pagedList.setData(this.getCouponDetail(couponVOS));
        pagedList.setPageNo(couponCondition.getPageNo());
        pagedList.setPageSize(couponCondition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public Boolean userReceiveCoupon(ReceiveCouponCondition condition) {

        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(condition.getCouponActivityId());
        couponActivityTemplate.setTemplateId(condition.getTemplateId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if(couponActivityTemplates.isEmpty()){
            logger.error("不存在符合新用户注册的优惠券活动");
            throw new BusinessException(BusinessCode.CODE_500001);
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
        //TODO 用户名称
        couponTemplateSend.setCreatedByName("");

        if(couponActivityTemplates.get(0).getEffectiveDays()==null){
            couponTemplateSend.setStartTime(couponActivityTemplates.get(0).getStartTime());
            couponTemplateSend.setEndTime(couponActivityTemplates.get(0).getEndTime());
        }else{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            couponTemplateSend.setStartTime(calendar.getTime());

            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE,couponActivityTemplates.get(0).getEffectiveDays());
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
            c.set(Calendar.MILLISECOND, 59);
            couponTemplateSend.setEndTime(c.getTime());
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
        couponActivityRecordMapper.insertSelective(couponActivityRecord);
        return true;
    }

    @Override
    public Boolean orderUseCoupon(OrderUseCouponCondition condition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        List<Long> sendIds = condition.getSendIds();
        if(sendIds.isEmpty() || condition.getCouponPrice() ==null || null ==condition.getOrderNo()||null == condition.getOrderPrice()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }

        for(int i =0 ;i<sendIds.size();i++){
            CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(sendIds.get(i));
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
            couponTemplateUse.setCustomerId(customerUser.getCustomerId());
            couponTemplateUse.setCustomerMonbile("");
            couponTemplateUse.setCreated(new Date());
            couponTemplateUse.setCreatedBy(customerUser.getCustomerId());
            couponTemplateUse.setCreatedByName("");
            couponTemplateUseMapper.insertSelective(couponTemplateUse);
        }
        return true;
    }

    @Override
    public Boolean orderUntreadCoupon(OrderUntreadCouponCondition condition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
        if(null ==condition.getOrderNo()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        List<CouponTemplateUse> couponTemplateUses = couponTemplateUseMapper.selectByOrderNo(condition.getOrderNo());
        for(CouponTemplateUse couponTemplateUse : couponTemplateUses){
            couponTemplateUse.setStatus(Short.valueOf(condition.getStatus()));
            couponTemplateUseMapper.updateByPrimaryKeySelective(couponTemplateUse);

            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(couponTemplateUse.getSendId());
            couponTemplateSend.setStatus(Short.valueOf(condition.getStatus()));
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);
        }
        return true;
    }

    @Override
    public Boolean revokeCoupon(RevokeCouponCodition condition) {
        List<Long> sendIds = condition.getSendIds();
        if(sendIds.isEmpty()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        for(int i =0 ;i<sendIds.size();i++){
            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(sendIds.get(i));
            couponTemplateSend.setStatus(CouponActivityEnum.INVALYD.getCode());
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);
        }
        return true;
    }

    /**
     * 查询订单使用的优惠券列表
     * @param couponCondition
     * @return
     */
    @Override
    public List<CouponVO> couponListByOrder(OrderCouponCondition couponCondition) {
        if(null == couponCondition.getOrderNo()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        List<CouponVO> couponVOS =  couponMapper.couponListByOrder(couponCondition.getOrderNo());

        return this.getCouponDetail(couponVOS);

    }

    @Override
    public CouponDiscountVO couponDiscountAmount(CouponPreAmountCondition couponCondition) {
        List<Long> sendIds = couponCondition.getSendIds();
        CouponDiscountVO couponDiscountVO = new CouponDiscountVO();
        couponDiscountVO.setDiscountAmount(BigDecimal.valueOf(0));
        if(sendIds.isEmpty()||null == couponCondition.getProducts()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(sendIds.get(0));
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(couponTemplateSend.getTemplateId());
        CouponApply couponApply = couponApplyMapper.selectByPrimaryKey(couponTemplate.getApplyRuleId());

        //通用券，按订单金额计算优惠金额
        if(couponApply.getApplyRuleType().equals(CouponApplyEnum.COMMON_COUPON.getCode())){
            //计算订单总额
            BigDecimal amountPrice = new BigDecimal(0);
            for(CouponProductCondition couponProductCondition: couponCondition.getProducts()){
                BigDecimal productPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                amountPrice.add(productPrice);
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(),amountPrice);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }
        //品牌券，按品牌总额计算优惠金额
        if(couponApply.getApplyRuleType().equals(CouponApplyEnum.BRAND_COUPON.getCode())){
            List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponApply.getId());
            if(couponApplyBrands.isEmpty()){
                logger.error("优惠券适用的品牌参数不存在");
                return couponDiscountVO;
            }
            List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());

            BigDecimal amountPrice = new BigDecimal(0);
            //遍历适用的品牌并计算金额
            for(CouponProductCondition couponProductCondition: couponCondition.getProducts()){
                for(CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists){

                    if(couponProductCondition.getBrandCode().equals(couponApplyBrandList.getBrandCode())){
                        BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                        amountPrice.add(brandProductPrice);
                    }
                }
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(),amountPrice);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }

        //商品券，按商品总额计算优惠金额
        if(couponApply.getApplyRuleType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
            List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponApply.getId());
            if(couponApplyProducts.isEmpty()){
                logger.error("优惠券适用的商品参数不存在");
                return couponDiscountVO;
            }
            List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());

            BigDecimal amountPrice = new BigDecimal(0);
            //遍历适用的商品并计算金额
            for(CouponProductCondition couponProductCondition: couponCondition.getProducts()){
                for(CouponApplyProductList couponApplyProductList : couponApplyProductLists){
                    if(couponProductCondition.getSkuCode().equals(couponApplyProductList.getSkuCode())){
                        BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                        amountPrice.add(brandProductPrice);
                    }
                }
            }
            BigDecimal discountAmount = this.computeAumont(couponTemplate.getGradeId(),amountPrice);
            couponDiscountVO.setDiscountAmount(discountAmount);
            return couponDiscountVO;
        }
        return couponDiscountVO;
    }

    /**
     * 计算优惠金额
     * @param gradeId 坎级ID
     * @param amountPrice
     * @return
     */
    public BigDecimal computeAumont(Long gradeId,BigDecimal amountPrice){

        //查询满减规则
        List<CouponGradeDetail> couponGradeDetails = couponGradeDetailMapper.selectByGradeId(gradeId);
        CouponGradeDetail couponGradeDetail = couponGradeDetails.get(0);
        //优惠类型 1.金额2，折扣
        if(couponGradeDetail.getReducedType().equals(CouponGradeEnum.UP_TO_REDUCE_CASH.getCode())){
            //订单金额大于等于满减金额,取优惠金额
            if(amountPrice.compareTo(couponGradeDetail.getReducedAmt())>=0){
                return couponGradeDetail.getDiscountedAmt();
            }
        }else{
            BigDecimal discountAmount = amountPrice.multiply(couponGradeDetail.getDiscounted());
            //优惠金额大于等于优惠最大限额,取优惠最大限额
            if(discountAmount.compareTo(couponGradeDetail.getDiscountedMaxAmt())>=0){
                return couponGradeDetail.getDiscountedMaxAmt();
            }
        }
        return new BigDecimal(0);
    }

    @Override
    public Boolean checkCouponStatus(CouponCheckStatusCondition condition) {
        int count = couponTemplateSendMapper.checkCouponStatus(condition);
        if(count > 0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 用户查询门店优惠券列表
     * @return
     */
    @Override
    public List<CouponVO> findStoreCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponVO> couponVOS = couponActivityMapper.selectStoreCouponList(storeUserInfo.getId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.COUPON_SUM.getCode())){
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("1");
                    }else{
                        couponVO.setReceiveStatus("0");
                    }
                }else{
                    // 优惠券已领完
                    continue;
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.STORE_NUM.getCode())){
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId());
                if(storeNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("1");
                    }else{
                        couponVO.setReceiveStatus("0");
                    }
                }else{
                    // 当前门店优惠券已领完
                    continue;
                }
            }
            results.add(couponVO);
        }
        return this.getCouponDetail(results);
    }

    /**
     * 订单可用优惠券
     * @param couponCondition
     * @return
     */
    @Override
    public List<CouponVO> availableCouponListByOrder(CouponPreAmountCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_410001, "用户信息异常");
        }

        //查询当前用户下的所有优惠券
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),null);
        //获取优惠券适用范围
        List<CouponVO> couponDetailS = this.getCouponDetail(couponVOS);
        //遍历添加优惠券是否可用状态
        for(CouponVO couponVO : couponDetailS){
            //通用券
            if(couponVO.getReducedType().equals(CouponApplyEnum.COMMON_COUPON.getCode())){
                //计算订单总额
                BigDecimal amountPrice = new BigDecimal(0);
                for(CouponProductCondition couponProductCondition: couponCondition.getProducts()){
                    BigDecimal productPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                    amountPrice.add(productPrice);
                }
                //订单金额大于等于满减金额优惠券可用
                if(amountPrice.compareTo(couponVO.getReducedAmt())>=0){
                    couponVO.setAvailableStatus("1");
                }
            }else if(couponVO.getReducedType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
                //商品券
                //循环优惠券适用的商品规则
                for(ProductSkuVO productSkuVO : couponVO.getProducts()){
                    //循环订单传参的商品信息
                    for(CouponProductCondition couponProductCondition :couponCondition.getProducts()){
                        BigDecimal amountPrice = new BigDecimal(0);
                        if(productSkuVO.getSkuCode().equals(couponProductCondition.getSkuCode())){
                            BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                            amountPrice.add(brandProductPrice);
                            //商品金额大于等于满减金额优惠券可用
                            if(amountPrice.compareTo(couponVO.getReducedAmt())>=0){
                                couponVO.setAvailableStatus("1");
                            }
                        }
                    }
                }
            }else if(couponVO.getReducedType().equals(CouponApplyEnum.BRAND_COUPON.getCode())){
                //品牌券
                //循环优惠券适用的品牌规则
                for(BrandVO brandVO : couponVO.getBrands()){
                    //循环订单传参的商品信息
                    for(CouponProductCondition couponProductCondition :couponCondition.getProducts()){
                        BigDecimal amountPrice = new BigDecimal(0);
                        if(brandVO.getBrandCode().equals(couponProductCondition.getBrandCode())){
                            BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getNum()));
                            amountPrice.add(brandProductPrice);
                            //商品金额大于等于满减金额优惠券可用
                            if(amountPrice.compareTo(couponVO.getReducedAmt())>=0){
                                couponVO.setAvailableStatus("1");
                            }
                        }
                    }
                }
            }
        }
        return couponDetailS;
    }

    @Override
    public Integer getStoreCouponKinds() {
        List<CouponVO> couponVOList =  findStoreCouponList();
        int count = 0 ;
        for (int i = 0; i < couponVOList.size(); i++){
            if(couponVOList.get(i).getReceiveStatus().equals("0")){
                count++;
            }
        }
        return count;
    }

    @Override
    public List<CouponInvestorAmountVO> getCouponInvestorAmount(OrderCouponCondition condition) {
        return null;
    }

    @Override
    public PagedList<CouponInStoreGetedAndUsedVO> findCouponInStoreGetedAndUsedPage(Long storeId, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo, pageSize);
        PagedList<CouponInStoreGetedAndUsedVO> pagedList = new PagedList();
        List<CouponInStoreGetedAndUsedVO> list = couponTemplateMapper.selectCouponInStoreGetedAndUsedPage(storeId);
        pagedList.setData(list);
        pagedList.setPageNo(pageNo);
        pagedList.setPageSize(pageSize);
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

}
