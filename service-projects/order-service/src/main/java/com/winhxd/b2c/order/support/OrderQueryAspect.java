package com.winhxd.b2c.order.support;

import com.winhxd.b2c.common.domain.order.enums.*;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 订单状态转换，code->mark
 *
 * @author pangjianhua
 */
@Component
@Aspect
public class OrderQueryAspect {

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
}
