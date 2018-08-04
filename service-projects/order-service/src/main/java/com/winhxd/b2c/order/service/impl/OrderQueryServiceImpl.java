package com.winhxd.b2c.order.service.impl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.OrderListCondition;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderQueryService;

/**
 * @author pangjianhua
 * @date 2018/8/3 15:02
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {

    @Resource
    private OrderInfoMapper orderInfoMapper;
    
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);
    
    @Autowired
    private Cache cache;

    /**
     * 根据用户ID查询所有订单
     *
     * @param condition
     * @return
     */
    @Override
    public PagedList<OrderInfoDetailVO> findOrderByCustomerId(OrderListCondition condition) {
        //TODO 待添加获取当前用户的接口
        Long customerId = 1L;
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        PagedList<OrderInfoDetailVO> pagedList = new PagedList();
        pagedList.setData(this.orderInfoMapper.selectOrderInfoListByCustomerId(customerId));
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    public StoreOrderSalesSummaryVO getStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            throw new NullPointerException("查询区间startDateTime={}、endDateTime={}不能为空");
        }
        logger.info("获取门店订单销售汇总信息开始：storeId={}，startDateTime={}，endDateTime={}");
        StoreOrderSalesSummaryVO orderSalesSummaryVO = null; 
        // 从缓存中获取
        String summaryInfoStr = cache.hget(CacheName.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime),
                String.valueOf(storeId));
        if (StringUtils.isBlank(summaryInfoStr)) {
            logger.info("获取到缓存订单销售汇总信息：storeId={}，startDateTime={}，endDateTime={}");
            orderSalesSummaryVO = JsonUtil.parseJSONObject(summaryInfoStr, StoreOrderSalesSummaryVO.class);
        }else {
            logger.info("缓存中未找到订单销售汇总信息：storeId={}，startDateTime={}，endDateTime={},通过数据库计算");
            orderSalesSummaryVO = calculateStoreOrderSalesSummaryAndSetCache(storeId, startDateTime, endDateTime);
        }
        return orderSalesSummaryVO;
    }

    private StoreOrderSalesSummaryVO calculateStoreOrderSalesSummaryAndSetCache(long storeId, Date startDateTime, Date endDateTime) {
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderInfoMapper.getStoreOrderTurnover(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO == null) {
            storeOrderSalesSummaryVO = new StoreOrderSalesSummaryVO();
        }
        Integer dailyCustomerNum = orderInfoMapper.getStoreOrderCustomerNum(storeId, startDateTime, endDateTime);
        if (dailyCustomerNum == null) {
            dailyCustomerNum = 0;
        }
        storeOrderSalesSummaryVO.setDailyOrderNum(dailyCustomerNum);
        storeOrderSalesSummaryVO.setStoreId(storeId);
        cache.hset(CacheName.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime), String.valueOf(storeId), JsonUtil.toJSONString(storeOrderSalesSummaryVO));
        //获取当天最后一秒
        long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59)).getTime();
        //当天有效
        cache.expire(CacheName.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime), Integer.valueOf(DurationFormatUtils.formatDuration(lastSecond - System.currentTimeMillis(), "s")));
        return storeOrderSalesSummaryVO;
    }
    
}
