package com.winhxd.b2c.order.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.order.condition.*;
import com.winhxd.b2c.common.domain.order.util.OrderUtil;
import com.winhxd.b2c.common.domain.order.vo.*;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.customer.CustomerServiceClient;
import com.winhxd.b2c.common.feign.product.ProductServiceClient;
import com.winhxd.b2c.common.feign.store.StoreServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author pangjianhua
 * @date 2018/8/3 15:02
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
    private Cache cache;

    @Autowired
    private OrderChangeLogService orderChangeLogService;
    @Resource
    private CustomerServiceClient customerServiceClient;
    @Resource
    private ProductServiceClient productServiceClient;
    @Resource
    private StoreServiceClient storeServiceClient;

    /**
     * 根据用户ID查询所有订单
     *
     * @param condition
     * @return
     */
    @Override
    @OrderInfoConvertAnnotation(queryProductInfo = true)
    public PagedList<OrderInfoDetailVO> findOrderListByCustomerId(AllOrderQueryByCustomerCondition condition) {
        CustomerUser customer = UserContext.getCurrentCustomerUser();
        if (customer == null) {
            throw new BusinessException(BusinessCode.CODE_4010001, "用户不存在");
        }
        Long customerId = customer.getCustomerId();
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        PagedList<OrderInfoDetailVO> pagedList = new PagedList();
        OrderInfoQuery4ManagementCondition orderInfoQuery4ManagementCondition = new OrderInfoQuery4ManagementCondition();
        orderInfoQuery4ManagementCondition.setCustomerId(customerId);
        List<Long> orderIds = this.orderInfoMapper.listOrder4Management(orderInfoQuery4ManagementCondition);
        if (orderIds != null && !orderIds.isEmpty()) {
            pagedList.setData(orderInfoMapper.listOrderInOrderIds(orderIds));
        }else {
            pagedList.setData(new ArrayList<>());
        }
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    /**
     * C端查询订单
     *
     * @param condition 入参
     * @return
     * @author pangjianhua
     */
    @Override
    @OrderInfoConvertAnnotation(queryStoreInfo = true, queryProductInfo = true)
    public OrderInfoDetailVO findOrderByCustomerId(OrderQueryByCustomerCondition condition) {
        if (StringUtils.isBlank(condition.getOrderNo())) {
            throw new BusinessException(BusinessCode.CODE_4011001, "查询订单参数异常");
        }
        OrderInfoDetailVO detailVO = this.orderInfoMapper.selectOrderInfoByOrderNo(condition.getOrderNo());
        return detailVO;
    }

    /**
     * B端查询订单
     *
     * @param condition 入参
     * @return
     * @author pangjianhua
     */
    @Override
    @OrderInfoConvertAnnotation(queryCustomerInfo = true, queryProductInfo = true)
    public OrderInfoDetailVO findOrderForStore(OrderQueryByStoreCondition condition) {
        if (StringUtils.isBlank(condition.getOrderNo())) {
            throw new BusinessException(BusinessCode.CODE_4011001, "查询订单参数异常");
        }
        OrderInfoDetailVO detailVO = this.orderInfoMapper.selectOrderInfoByOrderNo(condition.getOrderNo());
        return detailVO;
    }

    @Override
    public StoreOrderSalesSummaryVO getStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime) {
        if (startDateTime == null || endDateTime == null) {
            throw new NullPointerException(MessageFormat.format("查询区间startDateTime={0}、endDateTime={1}不能为空", startDateTime, endDateTime));
        }
        logger.info("获取门店订单销售汇总信息开始：storeId={}，startDateTime={}，endDateTime={}", storeId, startDateTime, endDateTime);
        StoreOrderSalesSummaryVO orderSalesSummaryVO = null;
        // 从缓存中获取
        String summaryInfoStr = cache.hget(OrderUtil.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime),
                String.valueOf(storeId));
        if (StringUtils.isNotBlank(summaryInfoStr)) {
            logger.info("获取到缓存订单销售汇总信息：storeId={}，startDateTime={}，endDateTime={}", storeId, startDateTime, endDateTime);
            orderSalesSummaryVO = JsonUtil.parseJSONObject(summaryInfoStr, StoreOrderSalesSummaryVO.class);
        } else {
            logger.info("缓存中未找到订单销售汇总信息：storeId={}，startDateTime={}，endDateTime={},通过数据库计算", storeId, startDateTime, endDateTime);
            orderSalesSummaryVO = calculateStoreOrderSalesSummaryAndSetCache(storeId, startDateTime, endDateTime);
        }
        return orderSalesSummaryVO;
    }

    @Override
    public StoreOrderSalesSummaryVO calculateStoreOrderSalesSummaryAndSetCache(long storeId, Date startDateTime, Date endDateTime) {
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderInfoMapper.getStoreOrderTurnover(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO == null) {
            storeOrderSalesSummaryVO = new StoreOrderSalesSummaryVO();
        }
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO1 = orderInfoMapper.getStoreOrderCustomerNum(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO1 != null) {
            BeanUtils.copyProperties(storeOrderSalesSummaryVO1, storeOrderSalesSummaryVO);
        }
        storeOrderSalesSummaryVO.setStoreId(storeId);
        cache.hset(OrderUtil.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime), String.valueOf(storeId), JsonUtil.toJSONString(storeOrderSalesSummaryVO));
        //获取当天最后一秒
        long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59)).getTime();
        //当天有效
        cache.expire(OrderUtil.getStoreOrderSalesSummaryKey(storeId, startDateTime, endDateTime), Integer.valueOf(DurationFormatUtils.formatDuration(lastSecond - System.currentTimeMillis(), "s")));
        return storeOrderSalesSummaryVO;
    }


    /**
     * 根据门店ID获取门店提货码
     *
     * @param storeId 门店ID
     * @return 门店提货码（不重复）
     * @author pangjianhua
     */
    @Override
    public String getPickUpCode(long storeId) {
        String code;
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + storeId;
        String getCodeKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_QUEUE + storeId;
        Lock lock = new RedisLock(cache, lockKey, 1000);
        try {
            lock.lock();
            code = this.cache.lpop(getCodeKey);
            if (StringUtils.isBlank(code)) {
                List<String> pickUpCodeList;
                do {
                    //批量生成50个提货码
                    pickUpCodeList = generatePickUpCodeList(50);
                    if (!this.orderInfoMapper.getPickUpCodeByStoreId(pickUpCodeList, storeId)) {
                        logger.info("提货码生成 storeId={},pickUpList={}", storeId, pickUpCodeList);
                        break;
                    }
                } while (true);
                //添加到redis 队列
                this.cache.lpush(getCodeKey, pickUpCodeList.toArray(new String[pickUpCodeList.size()]));
                //设置过期时间
                this.cache.expire(getCodeKey, 7200);
                code = this.cache.lpop(getCodeKey);
            }
        } finally {
            lock.unlock();
        }
        return code;
    }
    
    @Override
    @OrderInfoConvertAnnotation(queryCustomerInfo=true, queryStoreInfo=true)
    public List<OrderInfoDetailVO> listOrder4ManagementWithNoPage(
            OrderInfoQuery4ManagementCondition infoQuery4ManagementCondition) {
        if (infoQuery4ManagementCondition == null) {
            throw new NullPointerException("infoQuery4ManagementCondition can not be null");
        }
        List<Long> orderIds = this.orderInfoMapper.listOrder4Management(infoQuery4ManagementCondition);
        if (orderIds != null && !orderIds.isEmpty()) {
            return this.orderInfoMapper.listOrderInOrderIds(orderIds);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    @OrderInfoConvertAnnotation(queryCustomerInfo=true, queryStoreInfo=true)
    public PagedList<OrderInfoDetailVO> listOrder4Management(
            OrderInfoQuery4ManagementCondition condition) {
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        PagedList<OrderInfoDetailVO> pagedList = new PagedList<>();
        List<Long> orderIds = this.orderInfoMapper.listOrder4Management(condition);
        if (orderIds != null && !orderIds.isEmpty()) {
            pagedList.setData(orderInfoMapper.listOrderInOrderIds(orderIds));
        }else {
            pagedList.setData(new ArrayList<>());
        }
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        return pagedList;
    }

    @Override
    @OrderInfoConvertAnnotation(queryCustomerInfo=true, queryProductInfo=true, queryStoreInfo=true)
    public OrderInfoDetailVO4Management getOrderDetail4Management(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单编号不能为空");
        }
        OrderInfoDetailVO4Management orderInfoDetailVO4Management = null;
        OrderInfoDetailVO orderInfoDetailVO = orderInfoMapper.selectOrderInfoByOrderNo(orderNo);
        if (orderInfoDetailVO == null) {
            logger.info("订单 orderNo={} 未找到相关订单信息", orderNo);
            return orderInfoDetailVO4Management;
        }
        orderInfoDetailVO4Management = new OrderInfoDetailVO4Management();
        List<OrderChangeVO> orderChangeVoList = orderChangeLogService.listOrderChanges(orderNo);
        orderInfoDetailVO4Management.setOrderInfoDetailVO(orderInfoDetailVO);
        orderInfoDetailVO4Management.setOrderChangeVoList(orderChangeVoList);
        logger.info("订单 orderNo={} 订单信息查询结束", orderNo);
        return orderInfoDetailVO4Management;
    }

    @Override
    @OrderInfoConvertAnnotation(queryCustomerInfo=true, queryProductInfo=true)
    public PagedList<OrderInfoDetailVO> listOrder4Store(OrderQuery4StoreCondition condition, Long storeId) {
        if (storeId == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        logger.info("查询门店订单列表开始：condition={}，storeId={}", condition, storeId);
        Page page = PageHelper.startPage(condition.getPageNo(), condition.getPageSize());
        PagedList<OrderInfoDetailVO> pagedList = new PagedList<>();
        List<Long> orderIds = orderInfoMapper.listOrder4Store(condition, storeId);
        if (orderIds != null && !orderIds.isEmpty()) {
            pagedList.setData(orderInfoMapper.listOrder4StoreInOrderIds(orderIds));
        }else {
            pagedList.setData(new ArrayList<>());
        }
        pagedList.setPageNo(condition.getPageNo());
        pagedList.setPageSize(condition.getPageSize());
        pagedList.setTotalRows(page.getTotal());
        logger.info("查询门店订单列表结束：condition={}，storeId={}, totalRows={}", condition, storeId, page.getTotal());
        return pagedList;
    }
    
    @Override
    public OrderCountByStatus4StoreVO getOrderCountByStatus(Long storeCustomerId) {
        if (storeCustomerId == null) {
            throw new BusinessException(BusinessCode.STORE_ID_EMPTY);
        }
        logger.info("查询门店各状态订单数量开始：storeId={}", storeCustomerId);
        OrderCountByStatus4StoreVO orderCountByStatus4StoreVO = orderInfoMapper.getOrderCountByStatus(storeCustomerId);
        if (orderCountByStatus4StoreVO == null) {
            orderCountByStatus4StoreVO = new OrderCountByStatus4StoreVO();
        }
        logger.info("查询门店各状态订单数量结束：storeId={}", storeCustomerId);
        return orderCountByStatus4StoreVO;
    }

    /**
     * 一次生成多个个提货码
     *
     * @return 提货码列表
     */
    private List<String> generatePickUpCodeList(int size) {
        List<String> pickUpList = new ArrayList<>();
        do {
            for (int i = 0; i < size; i++) {
                String pickUpCode = (int) ((Math.random() * 9 + 1) * 1000) + "";
                pickUpList.add(pickUpCode);
            }
        } while (!hasSame(pickUpList));
        return pickUpList;
    }

    /**
     * 判断list中是否有重复字符串
     *
     * @param list
     * @return true为不重复，false为重复
     */
    private boolean hasSame(List<String> list) {
        if (null == list) {
            return false;
        }
        return list.size() == new HashSet<Object>(list).size();
    }

}
