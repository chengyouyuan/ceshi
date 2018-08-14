package com.winhxd.b2c.order.support;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.customer.vo.CustomerUserInfoVO;
import com.winhxd.b2c.common.domain.order.enums.*;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderItemVO;
import com.winhxd.b2c.common.domain.product.condition.ProductCondition;
import com.winhxd.b2c.common.domain.product.enums.SearchSkuCodeEnum;
import com.winhxd.b2c.common.domain.product.vo.ProductSkuVO;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation;
import org.apache.commons.collections4.CollectionUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单状态转换，code->mark
 *
 * @author pangjianhua
 */
@Component
@Aspect
public class OrderQueryAspect {

    private static final String DESC = "Desc";
    private static final String VALUATION_TYPE = "valuationType";
    private static final String PICKUP_TYPE = "pickupType";
    private static final String PAY_TYPE = "payType";
    private static final String PAY_STATUS = "payStatus";
    private static final String ORDER_STATUS = "orderStatus";
    private static final String CUSTOMER_ID = "customerId";
    private static final String NICK_NAME = "nickName";
    private static final String CUSTOMER_MOBILE = "customerMobile";
    private static final String STORE_ID = "storeId";
    public static final String STORE_MOBILE = "storeMobile";
    public static final String STORE_NAME = "storeName";
    public static final String ORDER_ITEMVO_LIST = "orderItemVoList";
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryAspect.class);

    @Resource
    private StoreServiceClient storeServiceClient;
    @Autowired
    private CustomerServiceClient customerServiceclient;
    @Resource
    private ProductServiceClient productServiceClient;

    @AfterReturning(returning = "detailVO", value = "@annotation(com.winhxd.b2c.order.support.annotation.OrderEnumConvertAnnotation)")
    public void orderEnumConvert(OrderInfoDetailVO detailVO) {
        if (null != detailVO) {
            //订单状态转换
            detailVO.setOrderStatusDesc(OrderStatusEnum.getMarkByCode(detailVO.getOrderStatus()));
            detailVO.setPayStatusDesc(PayStatusEnum.getDesc(detailVO.getPayStatus()));
            detailVO.setPayTypeDesc(PayTypeEnum.getPayTypeEnumDescByTypeCode(detailVO.getPayType()));
            detailVO.setPickupTypeDesc(PickUpTypeEnum.getPickUpTypeDescByCode(detailVO.getPickupType()));
            detailVO.setValuationTypeDesc(ValuationTypeEnum.getDescByCode(detailVO.getValuationType()));
        }
    }

    @AfterReturning(returning = "ret", value = "@annotation(com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation)")
    public void orderEnumConvert(JoinPoint joinPoint, Object ret) {
        if (ret instanceof Object[]) {
            Object[] objArr = (Object[]) ret;
            for (int i = 0; i < objArr.length; i++) {
                if (objArr[i] == null) {
                    continue;
                }
                assembleOrderInfos(objArr[i]);
            }
        } else if (ret instanceof List) {
            List objList = (List) ret;
            for (Iterator iterator = objList.iterator(); iterator.hasNext(); ) {
                Object object = iterator.next();
                if (object == null) {
                    continue;
                }
                assembleOrderInfos(object);
            }
        } else if (ret instanceof PagedList) {
            List objList = ((PagedList) ret).getData();
            for (Iterator iterator = objList.iterator(); iterator.hasNext(); ) {
                Object object = iterator.next();
                if (object == null) {
                    continue;
                }
                assembleOrderInfos(object);
            }
        } else {
            assembleOrderInfos(ret);
        }
        OrderInfoConvertAnnotation orderInfoConvertAnnotation = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(OrderInfoConvertAnnotation.class);
        if (orderInfoConvertAnnotation.queryCustomerInfo()) {
            // 获取用户相关信息
            customerInfoConvert(joinPoint, ret);
        }
        if (orderInfoConvertAnnotation.queryStoreInfo()) {
            // 获取门店相关信息
            storeInfoConvert(joinPoint, ret);
        }
        if (orderInfoConvertAnnotation.queryProductInfo()) {
            // 获取商品相关信息，订单项中已经冗余
//            productInfoConvert(joinPoint, ret);
        }
    }

    private void productInfoConvert(JoinPoint joinPoint, Object ret) {
        if (ret instanceof Object[]) {
            Object[] objArr = (Object[]) ret;
            assembleProductInfo(objArr);
        } else if (ret instanceof List) {
            List objList = (List) ret;
            assembleProductInfo(objList.toArray(new Object[objList.size()]));
        } else if (ret instanceof PagedList) {
            List objList = ((PagedList) ret).getData();
            if (objList != null) {
                assembleProductInfo(objList.toArray(new Object[objList.size()]));
            }
        } else {
            assembleProductInfo(ret);
        }
    }

    private void assembleProductInfo(Object... objArr) {
        try {
            Set<String> skuSet = new HashSet<>();
            for (Object obj : objArr) {
                Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getName().equals(ORDER_ITEMVO_LIST)).findFirst().orElse(null);
                if (null != field) {
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        @SuppressWarnings("unchecked")
                        List<OrderItemVO> orderItemVOS = (List<OrderItemVO>) field.get(obj);
                        for (OrderItemVO orderItemVO : orderItemVOS) {
                            skuSet.add(orderItemVO.getSkuCode());
                        }
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(skuSet)) {
                ProductCondition condition = new ProductCondition();
                condition.setProductSkus(new ArrayList<>(skuSet));
                condition.setSearchSkuCode(SearchSkuCodeEnum.IN_SKU_CODE);
                ResponseResult<List<ProductSkuVO>> productResponseResultData = productServiceClient.getProductSkus(condition);
                if (null != productResponseResultData && productResponseResultData.getCode() == BusinessCode.CODE_OK && CollectionUtils.isNotEmpty(productResponseResultData.getData())) {
                    List<ProductSkuVO> productSkuList = productResponseResultData.getData();
                    Map<String, ProductSkuVO> productListMap = productSkuList.stream().collect(Collectors.toMap(ProductSkuVO::getSkuCode, productSkuVO -> productSkuVO));
                    for (Object obj : objArr) {
                        Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getName().equals(ORDER_ITEMVO_LIST)).findFirst().orElse(null);
                        if (null != field) {
                            field.setAccessible(true);
                            if (field.get(obj) != null) {
                                @SuppressWarnings("unchecked")
                                List<OrderItemVO> orderItemVOS = (List<OrderItemVO>) field.get(obj);
                                for (OrderItemVO orderItemVO : orderItemVOS) {
                                    ProductSkuVO product = productListMap.get(orderItemVO.getSkuCode());
                                    if (null != product) {
                                        orderItemVO.setSkuDesc(product.getSkuName());
                                        orderItemVO.setSkuUrl(product.getSkuImage());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    logger.info("根据SKU查询商品信息失败：返回状态码:{}", productResponseResultData == null ? null : productResponseResultData.getCode());
                }
            }
        } catch (Exception e) {
            logger.error("订单商品信息封装异常", e);
        }
    }

    private void storeInfoConvert(JoinPoint joinPoint, Object ret) {
        if (ret instanceof Object[]) {
            Object[] objArr = (Object[]) ret;
            assembleStoreInfo(objArr);
        } else if (ret instanceof List) {
            List objList = (List) ret;
            assembleStoreInfo(objList.toArray(new Object[objList.size()]));
        } else if (ret instanceof PagedList) {
            List objList = ((PagedList) ret).getData();
            assembleStoreInfo(objList.toArray(new Object[objList.size()]));
        } else {
            assembleStoreInfo(ret);
        }
    }

    private void assembleStoreInfo(Object... objArr) {
        try {
            Set<Long> storeIds = new HashSet<>();
            for (Object obj : objArr) {
                Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getName().equals(STORE_ID)).findFirst().orElse(null);
                if (null != field) {
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        storeIds.add((Long) field.get(obj));
                    }
                }
            }

            ResponseResult<List<StoreUserInfoVO>> storeResponseResultData = this.storeServiceClient.findStoreUserInfoList(storeIds);
            if (null != storeResponseResultData && BusinessCode.CODE_OK == storeResponseResultData.getCode()) {
                List<StoreUserInfoVO> storeInfoList = storeResponseResultData.getData();
                for (Object obj : objArr) {
                    Field field = Arrays.stream(obj.getClass().getDeclaredFields()).filter(f -> f.getName().equals(STORE_ID)).findFirst().orElse(null);
                    if (null != field) {
                        field.setAccessible(true);
                        if (field.get(obj) != null) {
                            for (StoreUserInfoVO storeUserInfoVO : storeInfoList) {
                                if (storeUserInfoVO.getId().equals(field.get(obj))) {
                                    assembleInfos(obj, STORE_MOBILE, storeUserInfoVO.getStoreMobile());
                                    assembleInfos(obj, STORE_NAME, storeUserInfoVO.getStoreName());
                                }
                            }
                        }
                    }
                }
            } else {
                logger.info("根据门店storeId 查询门店信息失败：返回状态码:{}", storeResponseResultData == null ? null : storeResponseResultData.getCode());
            }
        } catch (Exception e) {
            logger.error("订单用户信息封装异常", e);
        }
    }

    public void customerInfoConvert(JoinPoint joinPoint, Object ret) {
        if (ret instanceof Object[]) {
            Object[] objArr = (Object[]) ret;
            assembleCustomerInfos(objArr);
        } else if (ret instanceof List) {
            List objList = (List) ret;
            assembleCustomerInfos(objList.toArray(new Object[objList.size()]));
        } else if (ret instanceof PagedList) {
            List objList = ((PagedList) ret).getData();
            assembleCustomerInfos(objList.toArray(new Object[objList.size()]));
        } else {
            assembleCustomerInfos(ret);
        }
    }

    private void assembleCustomerInfos(Object... objArr) {
        try {
            Set<Long> customerIds = new HashSet<>();
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                Field field = Arrays.asList(obj.getClass().getDeclaredFields()).stream().filter(f -> f.getName().equals(CUSTOMER_ID)).findFirst().orElse(null);
                if (field != null) {
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        customerIds.add((Long) field.get(obj));
                    }
                }
            }
            //根据 customerIds 到用户中心查询用户信息
            ResponseResult<List<CustomerUserInfoVO>> result = customerServiceclient.findCustomerUserByIds(new ArrayList<>(customerIds));
            if (result != null && result.getCode() == BusinessCode.CODE_OK) {
                List<CustomerUserInfoVO> customerUserInfoVO1s = result.getData();
                Map<Long, CustomerUserInfoVO> customerUserInfoVOMap = new HashMap<>();
                for (Iterator iterator = customerUserInfoVO1s.iterator(); iterator.hasNext(); ) {
                    CustomerUserInfoVO customerUserInfoVO1 = (CustomerUserInfoVO) iterator.next();
                    customerUserInfoVOMap.put(customerUserInfoVO1.getCustomerId(), customerUserInfoVO1);
                }
                for (int i = 0; i < objArr.length; i++) {
                    Object obj = objArr[i];
                    Field field = Arrays.asList(obj.getClass().getDeclaredFields()).stream().filter(f -> f.getName().equals(CUSTOMER_ID)).findFirst().orElse(null);
                    if (field != null) {
                        field.setAccessible(true);
                        if (field.get(obj) != null) {
                            CustomerUserInfoVO vo = customerUserInfoVOMap.get(field.get(obj));
                            if (vo != null) {
                                assembleInfos(obj, NICK_NAME, vo.getNickName());
                                assembleInfos(obj, CUSTOMER_MOBILE, vo.getCustomerMobile());
                            }
                        }
                    }
                }
            } else {
                logger.info("根据用户customerId 查询用户信息失败：返回状态码:{}", result == null ? null : result.getCode());
            }
        } catch (Exception e) {
            logger.error("订单用户信息封装异常", e);
        }
    }

    private void assembleOrderInfos(Object obj) {
        try {
            //订单相关状态类型信息
            setDesc(obj, ORDER_STATUS, OrderStatusEnum.getMarkMap());
            setDesc(obj, PAY_STATUS, PayStatusEnum.getDescMap());
            setDesc(obj, PAY_TYPE, PayTypeEnum.getDescMap());
            setDesc(obj, PICKUP_TYPE, PickUpTypeEnum.getDescMap());
            setDesc(obj, VALUATION_TYPE, ValuationTypeEnum.getDescMap());
        } catch (Exception e) {
            logger.error("订单信息封装异常", e);
        }
    }

    private void setDesc(Object obj, String fieldName, Map<Short, String> markMap) throws IllegalAccessException {
        Field field = Arrays.asList(obj.getClass().getDeclaredFields()).stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
        if (field != null) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                String markInfo = markMap.get((Short) field.get(obj));
                assembleInfos(obj, fieldName + DESC, markInfo);
            }
        }
    }

    private void assembleInfos(Object obj, String fieldName, Object val) throws IllegalAccessException {
        Field field = Arrays.asList(obj.getClass().getDeclaredFields()).stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
        if (field != null) {
            field.setAccessible(true);
            if (field.get(obj) == null) {
                field.set(obj, val);
            }
        }

    }
}
