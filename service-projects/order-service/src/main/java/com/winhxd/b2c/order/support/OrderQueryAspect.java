package com.winhxd.b2c.order.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayStatusEnum;
import com.winhxd.b2c.common.domain.order.enums.PayTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.PickUpTypeEnum;
import com.winhxd.b2c.common.domain.order.enums.ValuationTypeEnum;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.system.login.vo.CustomerUserInfoVO1;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation;

/**
 * 订单状态转换，code->mark
 *
 * @author pangjianhua
 */
@Component
@Aspect
public class OrderQueryAspect {
    
    private static final String VALUATION_TYPE = "valuationType";
    private static final String PICKUP_TYPE = "pickupType";
    private static final String PAY_TYPE = "payType";
    private static final String PAY_STATUS = "payStatus";
    private static final String ORDER_STATUS = "orderStatus";
    private static final String CUSTOMER_ID = "customerId";
    private static final String NICK_NAME = "nickName";
    private static final String CUSTOMER_MOBILE = "customerMobile";
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryAspect.class);
    
    @Autowired
    private CustomerServiceClient customerServiceclient;

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
    public void orderEnumConvert(JoinPoint joinPoint,Object ret) {
        try {
            if (ret instanceof Object[]) {
                Object[] objArr = (Object[]) ret;
                for (int i = 0; i < objArr.length; i++) {
                    if (objArr[i] == null) {
                        continue;
                    }
                    assambleOrderInfos(objArr[i]);
                }
            } else if (ret instanceof List) {
                List objList = (List) ret;
                for (Iterator iterator = objList.iterator(); iterator.hasNext();) {
                    Object object = (Object) iterator.next();
                    if (object == null) {
                        continue;
                    }
                    assambleOrderInfos(object);
                }
            }else if (ret instanceof PagedList) {
                List objList = ((PagedList) ret).getData();
                for (Iterator iterator = objList.iterator(); iterator.hasNext();) {
                    Object object = (Object) iterator.next();
                    if (object == null) {
                        continue;
                    }
                    assambleOrderInfos(object);
                }
            } else {
                assambleOrderInfos(ret);
            }
            //获取用户相关信息
            OrderInfoConvertAnnotation orderInfoConvertAnnotation = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(OrderInfoConvertAnnotation.class);
            if (orderInfoConvertAnnotation.queryCustomerInfo()) {
                customerInfoConvert(joinPoint, ret);
            }
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            logger.error("订单信息封装异常：", e);
        }
    }
    
    public void customerInfoConvert(JoinPoint joinPoint,Object ret) {
        try {
            if (ret instanceof Object[]) {
                Object[] objArr = (Object[]) ret;
                assambleCustomerInfos(objArr);
            } else if (ret instanceof List) {
                List objList = (List) ret;
                assambleCustomerInfos(objList.toArray(new Object[objList.size()]));
            }else if (ret instanceof PagedList) {
                List objList = ((PagedList) ret).getData();
                assambleCustomerInfos(objList.toArray(new Object[objList.size()]));
            } else {
                assambleCustomerInfos(ret);
            }
        } catch (IllegalAccessException | NoSuchFieldException | SecurityException e) {
            logger.error("订单信息封装异常：", e);
        }
    }
    
    private void assambleCustomerInfos(Object... objArr) throws NoSuchFieldException, IllegalAccessException {
        Set<Long> customerIds = new HashSet<>();
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            Field field = obj.getClass().getDeclaredField(CUSTOMER_ID);
            if (field != null) {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    customerIds.add((Long) field.get(obj));
                }
            }
        }
        //根据 customerIds 到用户中心查询用户信息
        ResponseResult<List<CustomerUserInfoVO1>> result = customerServiceclient.findCustomerUserByIds(new ArrayList<>(customerIds));
        if (result != null && result.getCode() == BusinessCode.CODE_OK) {
            List<CustomerUserInfoVO1> customerUserInfoVO1s = result.getData();
            Map<Long, CustomerUserInfoVO1> customerUserInfoVOMap = new HashMap<>();
            for (Iterator iterator = customerUserInfoVO1s.iterator(); iterator.hasNext();) {
                CustomerUserInfoVO1 customerUserInfoVO1 = (CustomerUserInfoVO1) iterator.next();
                customerUserInfoVOMap.put(customerUserInfoVO1.getCustomerId(), customerUserInfoVO1);
            }
            for (int i = 0; i < objArr.length; i++) {
                Object obj = objArr[i];
                Field field = obj.getClass().getDeclaredField(CUSTOMER_ID);
                if (field != null) {
                    field.setAccessible(true);
                    if (field.get(obj) != null) {
                        CustomerUserInfoVO1 vo = customerUserInfoVOMap.get(field.get(obj));
                        if (vo != null) {
                            assambleInfos(obj, NICK_NAME, vo.getNickName());
                            assambleInfos(obj, CUSTOMER_MOBILE, vo.getCustomerMobile());
                        }
                    }
                }
            }
        }else {
            logger.info("根据用户customerId 查询用户信息失败：返回状态码:{}", result.getCode());
        }
    }

    private void assambleOrderInfos(Object obj) throws IllegalAccessException, NoSuchFieldException, SecurityException {
        //订单相关状态类型信息
        setDesc(obj, ORDER_STATUS, OrderStatusEnum.getMarkMap());
        setDesc(obj, PAY_STATUS, PayStatusEnum.getDescMap());
        setDesc(obj, PAY_TYPE, PayTypeEnum.getDescMap());
        setDesc(obj, PICKUP_TYPE, PickUpTypeEnum.getDescMap());
        setDesc(obj, VALUATION_TYPE, ValuationTypeEnum.getDescMap());
    }
    private void setDesc(Object obj, String fieldName, Map<Short, String> markMap)
            throws IllegalAccessException, NoSuchFieldException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        if (field != null) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                String markInfo = markMap.get((Short) field.get(obj));
                assambleInfos(obj, fieldName + "Desc", markInfo);
            }
        }
    }
    
    private void assambleInfos(Object arg, String fieldName, Object val)
            throws IllegalAccessException, NoSuchFieldException, SecurityException {
        String fieldNameDesc = fieldName;
        Field field = arg.getClass().getDeclaredField(fieldNameDesc);
        if (field != null) {
            field.setAccessible(true);
            if (field.get(arg) == null) {
                field.set(arg, val);
            }
        }
    }
}
