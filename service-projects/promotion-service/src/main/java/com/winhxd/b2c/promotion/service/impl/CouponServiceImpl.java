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
import com.winhxd.b2c.common.domain.promotion.model.*;
import com.winhxd.b2c.common.domain.promotion.vo.CouponVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.promotion.dao.*;
import com.winhxd.b2c.promotion.service.CouponService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
                couponTemplateSend.setStartTime(activityTemplate.getStartTime());
                couponTemplateSend.setEndTime(activityTemplate.getEndTime());
                couponTemplateSend.setCount(1);
                couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
                couponTemplateSend.setCreated(new Date());
                //TODO 用户名称
                couponTemplateSend.setCreatedByName("");
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
            if(couponVO.getApplyRuleType().equals(4)){
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

            if(couponVO.getApplyRuleType().equals(2)){
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
	public Integer getCouponNumsByCustomerForStore(Long storeId, Long customerId) {
		// TODO Auto-generated method stub
		return null;
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
        ResponseResult<StoreUserInfo> result = storeServiceClient.findStoreUserInfoByCustomerId(customerUser.getCustomerId());
        StoreUserInfo storeUserInfo = result.getData();


        List<CouponVO> couponVOS = couponActivityMapper.selectUnclaimedCouponList(storeUserInfo.getStoreCustomerId());
        List<CouponVO> results = new ArrayList<>();
        for(CouponVO couponVO : couponVOS){
            //根据优惠券总数限制用户领取
            if(couponVO.getCouponNumType().equals(CouponActivityEnum.COUPON_SUM.getCode())){
                int templateNum = couponMapper.getCouponNumByTemplateId(couponVO.getActivityId(),couponVO.getTemplateId());
                if(templateNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId(),customerUser.getCustomerId());
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
                int storeNum = couponMapper.getCouponNumByStoreId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId());
                if(storeNum < couponVO.getCouponNum()){
                    int userNum = couponMapper.getCouponNumByCustomerId(couponVO.getActivityId(),couponVO.getTemplateId(),storeUserInfo.getStoreCustomerId(),customerUser.getCustomerId());
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
        couponTemplateSend.setStartTime(couponActivityTemplates.get(0).getStartTime());
        couponTemplateSend.setEndTime(couponActivityTemplates.get(0).getEndTime());
        couponTemplateSend.setCount(1);
        couponTemplateSend.setCreatedBy(customerUser.getCustomerId());
        couponTemplateSend.setCreated(new Date());
        //TODO 用户名称
        couponTemplateSend.setCreatedByName("");
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
            couponTemplateSend.setUpdateBy(customerUser.getCustomerId());
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
            couponTemplateUse.setStatus(CouponActivityEnum.UNTREAD.getCode());
            couponTemplateUseMapper.updateByPrimaryKeySelective(couponTemplateUse);

            CouponTemplateSend couponTemplateSend = new CouponTemplateSend();
            couponTemplateSend.setId(couponTemplateUse.getSendId());
            couponTemplateSend.setStatus(CouponActivityEnum.UNTREAD.getCode());
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
    public PagedList<CouponVO> couponListByOrder(OrderCouponCondition couponCondition) {

        Page page = PageHelper.startPage(couponCondition.getPageNo(), couponCondition.getPageSize());
        PagedList<CouponVO> pagedList = new PagedList();
        List<CouponVO> couponVOS =  couponMapper.couponListByOrder(couponCondition.getOrderNo());

        pagedList.setData(this.getCouponDetail(couponVOS));
        pagedList.setPageNo(couponCondition.getPageNo());
        pagedList.setPageSize(couponCondition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;

    }
}
