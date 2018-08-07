package com.winhxd.b2c.order.service;

import java.util.List;

import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.vo.OrderChangeVO;
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
    
    /**
     * 记录订单流转状态
     * @author wangbin
     * @date  2018年8月6日 下午7:40:01
     * @param orderNo
     * @param originalJson
     * @param newJson
     * @param originalStatus
     * @param newStatus
     * @param createdBy
     * @param createdByName
     * @param changeMsg
     * @param pointType
     */
    void orderChange(String orderNo, String originalJson, String newJson, Short originalStatus, Short newStatus, Long createdBy, String createdByName, String changeMsg, MainPointEnum pointType);

    /**
     * 根据订单编号 获取订单流转信息
     * @author wangbin
     * @date  2018年8月6日 下午7:40:18
     * @param orderNo
     * @return
     */
    List<OrderChangeVO> listOrderChanges(String orderNo);
}
