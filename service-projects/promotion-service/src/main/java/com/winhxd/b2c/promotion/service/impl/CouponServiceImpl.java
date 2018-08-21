package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.ShopCarQueryCondition;
import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.ShopCarProdInfoVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.BrandVO;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.promotion.condition.*;
import com.winhxd.b2c.common.domain.promotion.enums.CouponActivityEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponApplyEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponGradeEnum;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.*;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.order.OrderServiceClient;
import com.winhxd.b2c.common.feign.order.ShopCartServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.mq.MQHandler;
import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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


    @Override
    public List<CouponVO> getNewUserCouponList() {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }
        //step1 查询符合
        CouponActivity couponActivity = new CouponActivity();
        couponActivity.setCouponType((short)1);
        couponActivity.setStatus(CouponActivityEnum.ACTIVITY_OPEN.getCode());
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
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),1,null);

        return this.getCouponDetail(couponVOS);
    }

    /**
     * 获取优惠券详情
     * @param couponVOS
     * @return
     */
    public List<CouponVO> getCouponDetail(List<CouponVO> couponVOS){
        for(CouponVO couponVO : couponVOS){
            if(couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()))){
                List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                if(!couponApplyProducts.isEmpty()){
                    List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                    //组装请求的参数
                    List<String> productSkus = new ArrayList<>();
                    for(CouponApplyProductList couponApplyProductList : couponApplyProductLists){
                        productSkus.add(couponApplyProductList.getSkuCode());
                    }
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

            if(couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()))){
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
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
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
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，待领取优惠券异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponVO> couponVOS = couponActivityMapper.selectUnclaimedCouponList(storeUserInfo.getId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.COUPON_SUM.getCode()))){
                //获取某个优惠券领取总数量
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    //limitNum为空代表不限制用户领取数量
                    if(couponVO.getLimitNum()==null) {
                        couponVO.setReceiveStatus("1");
                    }else{
                        //获取某个优惠券用户领取的数量
                        int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),customerUser.getCustomerId());
                        if(userNum < couponVO.getLimitNum()){
                            couponVO.setReceiveStatus("1");
                        }else{
                            couponVO.setReceiveStatus("0");
                        }
                    }
                }else{
                    // 优惠券已领完
                    couponVO.setReceiveStatus("0");
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if(couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.STORE_NUM.getCode()))){
                //获取某个优惠券门店领取的数量
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId());
                if(storeNum < couponVO.getCouponNum()){
                    //limitNum为空代表不限制用户领取数量
                    if(couponVO.getLimitNum()==null){
                        couponVO.setReceiveStatus("1");
                    }else{
                        //获取某个优惠券用户领取的数量
                        int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),customerUser.getCustomerId());
                        if(userNum < couponVO.getLimitNum()){
                            couponVO.setReceiveStatus("1");
                        }else{
                            couponVO.setReceiveStatus("0");
                        }
                    }
                }else{
                    // 当前门店优惠券已领完
                    couponVO.setReceiveStatus("0");
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
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }

        //我的优惠券列表不分页，一页显示全部
        if(null == couponCondition.getStatus()){
            List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),null,couponCondition.getStatus());
            couponCondition.setPageSize(couponVOS.size());
            couponCondition.setPageNo(1);
        }

        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),null,couponCondition.getStatus());

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
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }

        CouponActivityTemplate couponActivityTemplate = new CouponActivityTemplate();
        couponActivityTemplate.setCouponActivityId(condition.getCouponActivityId());
        couponActivityTemplate.setTemplateId(condition.getTemplateId());
        List<CouponActivityTemplate> couponActivityTemplates = couponActivityTemplateMapper.selectByExample(couponActivityTemplate);
        if(couponActivityTemplates.isEmpty()){
            logger.error("CouponServiceImpl.userReceiveCoupon 不存在的优惠券");
            throw new BusinessException(BusinessCode.CODE_500001);
        }

        List<CouponActivityStoreCustomer> couponActivityStoreCustomers = couponActivityStoreCustomerMapper.selectByTemplateId(couponActivityTemplates.get(0).getId());

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
        if(couponActivityStoreCustomers.isEmpty()){
            couponActivityRecord.setStoreId(null);
        }else{
            couponActivityRecord.setStoreId(couponActivityStoreCustomers.get(0).getStoreId());
        }
        couponActivityRecordMapper.insertSelective(couponActivityRecord);
        return true;
    }

    @Override
    public Boolean orderUseCoupon(OrderUseCouponCondition condition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
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
        if(null ==condition.getOrderNo()){
            logger.error("CouponSerciceImpl.orderUntreadCoupon-订单号为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        logger.info("根据订单退优惠券,订单号"+condition.getOrderNo());
        List<CouponTemplateUse> couponTemplateUses = couponTemplateUseMapper.selectByOrderNo(condition.getOrderNo());
        for(CouponTemplateUse couponTemplateUse : couponTemplateUses){
            couponTemplateUse.setStatus(CouponActivityEnum.UNTREAD.getCode());
            couponTemplateUseMapper.updateByPrimaryKeySelective(couponTemplateUse);

            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(couponTemplateUse.getSendId());
            couponTemplateSend.setStatus(CouponActivityEnum.UNTREAD.getCode());
            couponTemplateSendMapper.updateByPrimaryKeySelective(couponTemplateSend);
        }
        logger.info("订单退优惠券结束,订单号"+condition.getOrderNo());
        return true;
    }

    @EventMessageListener(value = EventTypeHandler.EVENT_CUSTOMER_ORDER_UNTREAD_COUPON_HANDLER)
    public void eventOrderUntreadCoupon(String orderNo, OrderInfo order) {
        if(null ==order.getOrderNo()){
            logger.error("CouponSerciceImpl.orderUntreadCoupon-订单号为空");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        OrderUntreadCouponCondition condition = new OrderUntreadCouponCondition();
        condition.setOrderNo(order.getOrderNo());
        this.orderUntreadCoupon(condition);
    }

    @Override
    public Boolean revokeCoupon(RevokeCouponCodition condition) {
        List<Long> sendIds = condition.getSendIds();
        if(sendIds.isEmpty()){
            logger.error("CouponServiceImpl.revokeCoupon 优惠券发放id为空");
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
            logger.error("CouponServiceImpl.couponCondition-订单号为空");
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
            logger.error("CouponServiceImpl.couponDiscountAmount 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }
        CouponTemplateSend couponTemplateSend = couponTemplateSendMapper.selectByPrimaryKey(sendIds.get(0));
        CouponTemplate couponTemplate = couponTemplateMapper.selectByPrimaryKey(couponTemplateSend.getTemplateId());
        CouponApply couponApply = couponApplyMapper.selectByPrimaryKey(couponTemplate.getApplyRuleId());

        couponDiscountVO.setCouponTitle(couponTemplate.getTitle());
        //通用券，按订单金额计算优惠金额
        if(couponApply.getApplyRuleType().equals(CouponApplyEnum.COMMON_COUPON.getCode())){
            //计算订单总额
            BigDecimal amountPrice = new BigDecimal(0);
            for(CouponProductCondition couponProductCondition: couponCondition.getProducts()){
                BigDecimal productPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                amountPrice = amountPrice.add(productPrice);
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

                    if(couponProductCondition.getBrandCode().equals(couponApplyBrandList.getBrandCode())&&couponProductCondition.getBrandBusinessCode().equals(couponApplyBrandList.getCompanyCode())){
                        BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                        amountPrice = amountPrice.add(brandProductPrice);
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
                        BigDecimal brandProductPrice = couponProductCondition.getPrice().multiply(BigDecimal.valueOf(couponProductCondition.getSkuNum()));
                        amountPrice = amountPrice.add(brandProductPrice);
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

            //满减金额等于0 代表无门槛券
            if(couponGradeDetail.getReducedAmt().compareTo(BigDecimal.valueOf(0))==0){
                //总额大于等于满减金额,返回满减金额否则返回总额
                return amountPrice.compareTo(couponGradeDetail.getReducedAmt())>=0 ? couponGradeDetail.getReducedAmt():amountPrice;
            }

            //总额大于等于满减金额
            if(amountPrice.compareTo(couponGradeDetail.getReducedAmt())>=0){
                return couponGradeDetail.getDiscountedAmt();
            }
        }else{
            //计算优惠金额
            BigDecimal discountAmount = amountPrice.multiply(couponGradeDetail.getDiscounted());

            //满减金额等于0 代表无门槛券
            if(couponGradeDetail.getReducedAmt().compareTo(BigDecimal.valueOf(0))==0){
                //总额大于等于满减金额,返回满减金额否则返回总额
                // 折扣券不存在优惠金额大于总额
            }

            //优惠金额大于等于优惠最大限额,取优惠最大限额
            if(discountAmount.compareTo(couponGradeDetail.getDiscountedMaxAmt())>=0){
                return couponGradeDetail.getDiscountedMaxAmt();
            }else{
                return discountAmount;
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
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }
        ResponseResult<StoreUserInfoVO> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
            logger.error("优惠券：{}获取门店信息接口调用失败:code={}，用户查询门店优惠券列表异常！~", customerUser.getCustomerId(), result == null ? null : result.getCode());
            throw new BusinessException(result.getCode());
        }
        StoreUserInfoVO storeUserInfo = result.getData();

        List<CouponVO> couponVOS = couponActivityMapper.selectStoreCouponList(storeUserInfo.getId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.COUPON_SUM.getCode()))){
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("1");
                    }else{
                        couponVO.setReceiveStatus("0");
                    }
                }else{
                    // 优惠券已领完
                    couponVO.setReceiveStatus("0");
                }
            }
            //根据每个门店可领取的优惠券数量限制用户领取
            if(couponVO.getCouponNumType().equals(String.valueOf(CouponActivityEnum.STORE_NUM.getCode()))){
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getId());
                if(storeNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),customerUser.getCustomerId());
                    if(userNum < couponVO.getLimitNum()){
                        couponVO.setReceiveStatus("1");
                    }else{
                        couponVO.setReceiveStatus("0");
                    }
                }else{
                    // 当前门店优惠券已领完
                    couponVO.setReceiveStatus("0");
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
    public List<CouponVO> availableCouponListByOrder(OrderAvailableCouponCondition couponCondition) {
        CustomerUser customerUser = UserContext.getCurrentCustomerUser();
        if (customerUser == null) {
            throw new BusinessException(BusinessCode.CODE_500014, "用户信息异常");
        }

        if(null == couponCondition.getStoreId()||null== couponCondition.getPayType()){
            logger.error("CouponServiceImpl.availableCouponListByOrder 参数错误");
            throw new BusinessException(BusinessCode.CODE_1007);
        }

        //获取购物车商品信息
        ShopCarQueryCondition shopCarQueryCondition = new ShopCarQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK ) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，订单可用优惠券接口异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();

        //查询当前用户下的所有优惠券
        List<CouponVO> couponVOS = couponActivityMapper.selectCouponList(customerUser.getCustomerId(),null,null);
        //
        List<CouponVO> results = new ArrayList<>();
        //遍历添加优惠券是否可用状态
        for(CouponVO couponVO : couponVOS){
            couponVO.setAvailableStatus(0);
            //支付方式
            if(couponVO.getPayType().equals(couponCondition.getPayType())){
                //通用券
                if(couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.COMMON_COUPON.getCode()))){
                    //计算订单总额
                    BigDecimal amountPrice = new BigDecimal(0);
                    for(ShopCarProdInfoVO shopCarProdInfoVO: shopCarProdInfoVOS){
                        BigDecimal productPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                        amountPrice = amountPrice.add(productPrice);
                    }
                    //订单金额大于等于满减金额优惠券可用
                    if(amountPrice.compareTo(couponVO.getReducedAmt())>=0){
                        couponVO.setAvailableStatus(1);
                        results.add(couponVO);
                    }
                }else if(couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.PRODUCT_COUPON.getCode()))){
                    //商品券
                    List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(couponVO.getApplyId());
                    if(!couponApplyProducts.isEmpty()) {
                        //优惠券适用的商品规则
                        List<CouponApplyProductList> couponApplyProductLists = couponApplyProductListMapper.selectByApplyProductId(couponApplyProducts.get(0).getId());
                        for (CouponApplyProductList couponApplyProduct : couponApplyProductLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                BigDecimal amountPrice = new BigDecimal(0);
                                if (couponApplyProduct.getSkuCode().equals(shopCarProdInfoVO.getSkuCode())) {
                                    BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                    amountPrice = amountPrice.add(brandProductPrice);
                                    //商品金额大于等于满减金额优惠券可用
                                    if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                                        couponVO.setAvailableStatus(1);
                                        results.add(couponVO);
                                    }
                                }
                            }
                        }
                    }
                }else if(couponVO.getApplyRuleType().equals(String.valueOf(CouponApplyEnum.BRAND_COUPON.getCode()))){
                    //品牌券
                    List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(couponVO.getApplyId());
                    if(!couponApplyBrands.isEmpty()) {
                        //优惠券适用的品牌规则
                        List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                        for (CouponApplyBrandList couponApplyBrand : couponApplyBrandLists) {
                            //循环购物车商品信息
                            for (ShopCarProdInfoVO shopCarProdInfoVO : shopCarProdInfoVOS) {
                                BigDecimal amountPrice = new BigDecimal(0);
                                if (couponApplyBrand.getBrandCode().equals(shopCarProdInfoVO.getBrandCode()) && couponApplyBrand.getCompanyCode().equals(shopCarProdInfoVO.getCompanyCode())) {
                                    BigDecimal brandProductPrice = shopCarProdInfoVO.getPrice().multiply(BigDecimal.valueOf(shopCarProdInfoVO.getAmount()));
                                    amountPrice = amountPrice.add(brandProductPrice);
                                    //商品金额大于等于满减金额优惠券可用
                                    if (amountPrice.compareTo(couponVO.getReducedAmt()) >= 0) {
                                        couponVO.setAvailableStatus(1);
                                        results.add(couponVO);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return this.getCouponDetail(results);
    }

    @Override
    public CouponKindsVo getStoreCouponKinds() {
        List<CouponVO> couponVOList =  findStoreCouponList();
        int count = 0 ;
        for (int i = 0; i < couponVOList.size(); i++){
            if(couponVOList.get(i).getReceiveStatus().equals("0")){
                count++;
            }
        }
        CouponKindsVo couponKindsVo = new CouponKindsVo();
        couponKindsVo.setStoreCouponKinds(count);
        return couponKindsVo;
    }

    @Override
    public List<CouponInvestorAmountVO> getCouponInvestorAmount(CouponInvestorAmountCondition condition) {
        List<CouponInvestorAmountVO> couponInvestorAmountVOs= new ArrayList<>();
        if(null == condition.getOrderNos()){
            throw new BusinessException(BusinessCode.CODE_1007);
        }

        List<CouponTemplateUse> couponTemplateUses = couponTemplateUseMapper.selectByOrderNos(condition.getOrderNos());
        if(couponTemplateUses.isEmpty()){
            throw new BusinessException(BusinessCode.CODE_500007);
        }
        for(CouponTemplateUse couponTemplateUse : couponTemplateUses){
            CouponInvestorAmountVO couponInvestorAmountVO = new CouponInvestorAmountVO();
            CouponTemplate couponTemplate= couponTemplateMapper.selectByPrimaryKey(couponTemplateUse.getTemplateId());
            if(null == couponTemplate){
                throw new BusinessException(BusinessCode.CODE_500008);
            }

            CouponInvestor couponInvestor = couponInvestorMapper.selectByPrimaryKey(couponTemplate.getInvestorId());
            if(null == couponInvestor){
                throw new BusinessException(BusinessCode.CODE_500009);
            }

            List<CouponInvestorDetail> couponInvestorDetails = couponInvestorDetailMapper.selectByInvestorId(couponInvestor.getId());
            if(couponInvestorDetails.isEmpty()){
                throw new BusinessException(BusinessCode.CODE_500009);
            }
            //调用订单系统获取订单信息
            ResponseResult<OrderInfoDetailVO4Management> result = orderServiceClient.getOrderDetail4Management(couponTemplateUse.getOrderNo());
            if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                logger.error("优惠券：{}获取订单信息接口调用失败:code={}，获取优惠券费用承担信息异常！~", couponTemplateUse.getOrderNo(), result == null ? null : result.getCode());
                throw new BusinessException(result.getCode());
            }
            BigDecimal discountMoney = result.getData().getOrderInfoDetailVO().getDiscountMoney();

            for(CouponInvestorDetail couponInvestorDetail :couponInvestorDetails){
                if(couponInvestorDetail.getInvestorType().equals(CouponTemplateEnum.INVERTOR_H_TYPE.getCode())){
                    couponInvestorAmountVO.setPlatformAmount(discountMoney.multiply(new BigDecimal(couponInvestorDetail.getPercent())));
                }else{
                    couponInvestorAmountVO.setBrandAmount(discountMoney.multiply(new BigDecimal(couponInvestorDetail.getPercent())));
                }
            }
            couponInvestorAmountVOs.add(couponInvestorAmountVO);
        }
        return couponInvestorAmountVOs;
    }

    @Override
    public PagedList<CouponInStoreGetedAndUsedVO> findCouponInStoreGetedAndUsedPage(Long storeId, Integer pageNo, Integer pageSize) {
        Page page = PageHelper.startPage(pageNo, pageSize);
        PagedList<CouponInStoreGetedAndUsedVO> pagedList = new PagedList();
        //查询优惠券列表
        List<CouponInStoreGetedAndUsedVO> list = couponTemplateMapper.selectCouponInStoreGetedAndUsedPage(storeId);
        //查询使用每张优惠券的使用数量和领取数量
        List<CouponInStoreGetedAndUsedVO> countList = couponTemplateMapper.selectCouponGetedAndUsedCout(storeId);
        //将数量拼接到列表
        if(list!=null){
            for(int i=0;i<list.size();i++){
                CouponInStoreGetedAndUsedVO vo = list.get(i);
                if(countList!=null){
                    for(int j=0;j<countList.size();j++){
                        if(vo.getTempleteId().equals(countList.get(j).getTempleteId())){
                            vo.setTotalCount(countList.get(j).getTotalCount());
                            vo.setUsedCount(countList.get(j).getUsedCount());
                        }
                    }
                }else{
                    vo.setTotalCount(0);
                    vo.setUsedCount(0);
                }
            }
        }

        List<CouponInStoreGetedAndUsedVO> finalList = this.getCouponApplyDetail(list);
        pagedList.setData(finalList);
        pagedList.setPageNo(pageNo);
        pagedList.setPageSize(pageSize);
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }



    /**
     * 获取可用最优惠的优惠券
     * @param couponCondition
     * @return
     */
    @Override
    public CouponVO findDefaultCouponByOrder(OrderAvailableCouponCondition couponCondition) {
        List<CouponProductCondition> productConditions = new ArrayList<>();
        BigDecimal maxDiscountAmount = new BigDecimal(0);
        CouponVO result = new CouponVO();

        List<CouponVO> couponVOs = this.availableCouponListByOrder(couponCondition);
        CouponPreAmountCondition couponPreAmountCondition = new CouponPreAmountCondition();
        //获取购物车商品信息
        ShopCarQueryCondition shopCarQueryCondition = new ShopCarQueryCondition();
        shopCarQueryCondition.setStoreId(couponCondition.getStoreId());
        ResponseResult<List<ShopCarProdInfoVO>> responseResult = shopCartServiceClient.findShopCar(shopCarQueryCondition);
        if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK ) {
            logger.error("优惠券：{}获取购物车信息接口调用失败:code={}，获取最优惠的优惠券信息异常！~", couponCondition.getStoreId(), responseResult == null ? null : responseResult.getCode());
            throw new BusinessException(responseResult.getCode());
        }
        List<ShopCarProdInfoVO> shopCarProdInfoVOS = responseResult.getData();

        for(ShopCarProdInfoVO shopCarProdInfoVO: shopCarProdInfoVOS){
            CouponProductCondition couponProductCondition = new CouponProductCondition();
            couponProductCondition.setBrandBusinessCode(shopCarProdInfoVO.getCompanyCode());
            couponProductCondition.setBrandCode(shopCarProdInfoVO.getBrandCode());
            couponProductCondition.setPrice(shopCarProdInfoVO.getPrice());
            couponProductCondition.setSkuNum(shopCarProdInfoVO.getAmount());
            couponProductCondition.setSkuCode(shopCarProdInfoVO.getSkuCode());
            productConditions.add(couponProductCondition);
        }
        couponPreAmountCondition.setProducts(productConditions);
        for(CouponVO couponVO :couponVOs){
            if(1 == couponVO.getAvailableStatus()){
                List<Long> sendIds = new ArrayList<>();
                sendIds.add(couponVO.getSendId());
                couponPreAmountCondition.setSendIds(sendIds);
                //计算优惠券优惠金额
                CouponDiscountVO couponDiscountVO = this.couponDiscountAmount(couponPreAmountCondition);

                if(couponDiscountVO.getDiscountAmount().compareTo(maxDiscountAmount)>0){
                    maxDiscountAmount = couponDiscountVO.getDiscountAmount();
                    result = couponVO;
                }
            }
        }
        return result;
    }


    /**
     * 获取优惠券适用范围
     * @param couponVOS
     * @return
     */
    public List<CouponInStoreGetedAndUsedVO> getCouponApplyDetail(List<CouponInStoreGetedAndUsedVO> couponVOS){
        for(CouponInStoreGetedAndUsedVO vo : couponVOS){
            if(vo.getApplyRuleType()!=null && vo.getApplyRuleType().equals(CouponApplyEnum.PRODUCT_COUPON.getCode())){
                List<CouponApplyProduct> couponApplyProducts = couponApplyProductMapper.selectByApplyId(vo.getApplyId());
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
                    productCondition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                    ResponseResult<List<ProductSkuVO>> result = productServiceClient.getProductSkus(productCondition);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}获取商品sku信息接口调用失败:code={}，获取优惠券适用范围异常！~", productCondition, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    vo.setProducts(result.getData());
                }
            }

            if(vo.getApplyRuleType()!=null && vo.getApplyRuleType().equals(CouponApplyEnum.BRAND_COUPON.getCode())){
                List<CouponApplyBrand> couponApplyBrands = couponApplyBrandMapper.selectByApplyId(vo.getApplyId());
                if(!couponApplyBrands.isEmpty()){
                    List<CouponApplyBrandList> couponApplyBrandLists = couponApplyBrandListMapper.selectByApplyBrandId(couponApplyBrands.get(0).getId());
                    //组装请求的参数
                    List<String> brandCodes = new ArrayList<>();
                    for(CouponApplyBrandList couponApplyBrandList : couponApplyBrandLists){
                        brandCodes.add(couponApplyBrandList.getBrandCode());
                    }
                    //调用获取商品信息接口
                    ResponseResult<List<BrandVO>> result = productServiceClient.getBrandInfo(brandCodes);
                    if (result == null || result.getCode() != BusinessCode.CODE_OK || result.getData() == null) {
                        logger.error("优惠券：{}根据brandCode获取商品信息接口调用失败:code={}，获取优惠券适用范围异常！~", brandCodes, result == null ? null : result.getCode());
                        throw new BusinessException(result.getCode());
                    }
                    vo.setBrands(result.getData());
                }
            }
        }
        return couponVOS;
    }







}
