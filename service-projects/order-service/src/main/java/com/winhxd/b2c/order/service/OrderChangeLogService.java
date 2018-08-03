package com.winhxd.b2c.order.service;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.order.service.OrderChangeLogService.MainPointEnum;

/**
 * 订单流转状态记录服务
 * @author wangbin
 * @date  2018年8月2日 下午1:35:43
 * @version 
 */
public interface OrderChangeLogService {

    /**
     * 主要流转节点
     * @author wangbin
     * @date  2018年8月2日 下午1:54:08
     * @version 
     */
    public enum MainPointEnum {
        /**
         * 主要节点
         */
        MAIN((short)1), 
        /**
         * 非主要节点 
         */
        NOT_MAIN((short)0);
        short code;

        private MainPointEnum(short code) {
            this.code = code;
        }

        public short getCode() {
            return code;
        }
    }
    
    void orderChange(String orderNo, String originalJson, String newJson, Short originalStatus, Short newStatus, Long createdBy, String createdByName, String changeMsg, MainPointEnum pointType);
}
