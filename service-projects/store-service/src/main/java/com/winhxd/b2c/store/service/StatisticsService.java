package com.winhxd.b2c.store.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.winhxd.b2c.common.domain.order.model.OrderInfo;
import com.winhxd.b2c.common.domain.order.model.OrderItem;
import com.winhxd.b2c.common.domain.store.model.StoreProductStatistics;
import com.winhxd.b2c.common.mq.event.EventMessageListener;
import com.winhxd.b2c.common.mq.event.EventTypeHandler;
/**
 * 统计service
 * @ClassName: StatisticsService 
 * @Description: TODO
 * @author: wuyuanbao
 * @date: 2018年8月23日 下午3:15:05
 */
@Service
public class StatisticsService {
    private Logger logger = LoggerFactory.getLogger(StatisticsService.class);
    @Autowired
    private StoreProductStatisticsService storeProductStatisticsService;
    /**
     * 订单完成统计商品销售数量
    * @Title: orderFinishHandler 
    * @Description: TODO 
    * @param orderNo
    * @param orderInfo void
    * @author wuyuanbao
    * @date 2018年8月23日下午3:16:52
     */
    @EventMessageListener(value = EventTypeHandler.EVENT_STATISTICS_PROD_HANDLER, concurrency = "3-6")
    public void orderFinishHandler(String orderNo, OrderInfo orderInfo) {
        logger.info("统计订单号："+orderNo+",商品购买信息！");
        if(StringUtils.isNotBlank(orderNo)&&orderInfo!=null){
            List<OrderItem> orderItemList=orderInfo.getOrderItems();
            Long storeId=orderInfo.getStoreId();
            List<StoreProductStatistics> statisticsList=new ArrayList<>();
            if(orderItemList!=null){
                for(OrderItem o:orderItemList){
                    StoreProductStatistics spStatistics=new StoreProductStatistics();
                    spStatistics.setStoreId(storeId);
                    spStatistics.setCreated(new Date());
                    spStatistics.setOrderNo(orderNo);
                    spStatistics.setSkuCode(o.getSkuCode());
                    spStatistics.setQuantitySoldOut(o.getAmount());
                    statisticsList.add(spStatistics);
                    logger.info("订单号："+orderNo+",skuCode："+o.getSkuCode()+",数量："+o.getAmount());
                }
                //批量插入
                if(statisticsList.size()>0){
                    storeProductStatisticsService.bathSaveStoreProductStatistics(statisticsList);
                }
            }
        }
       
    }
}
