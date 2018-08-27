package com.winhxd.b2c.order.service.impl;

import java.sql.Timestamp;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.DurationFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.constant.OrderNotifyMsg;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.condition.AllOrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderInfoQuery4ManagementCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQuery4StoreCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByCustomerCondition;
import com.winhxd.b2c.common.domain.order.condition.OrderQueryByStoreCondition;
import com.winhxd.b2c.common.domain.order.enums.OrderStatusEnum;
import com.winhxd.b2c.common.domain.order.vo.OrderChangeVO;
import com.winhxd.b2c.common.domain.order.vo.OrderCountByStatus4StoreVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO4Management;
import com.winhxd.b2c.common.domain.order.vo.StoreOrderSalesSummaryVO;
import com.winhxd.b2c.common.domain.pay.condition.PayPreOrderCondition;
import com.winhxd.b2c.common.domain.pay.vo.OrderPayVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.pay.PayServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.order.dao.OrderInfoMapper;
import com.winhxd.b2c.order.service.OrderChangeLogService;
import com.winhxd.b2c.order.service.OrderQueryService;
import com.winhxd.b2c.order.support.annotation.OrderInfoConvertAnnotation;
import com.winhxd.b2c.order.util.OrderUtil;

/**
 * @author pangjianhua
 * @date 2018/8/3 15:02
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    private static final String DEFAULT_IP = "127.0.0.1";

    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    @Autowired
    private OrderInfoMapper orderInfoMapper;
    @Autowired
    private Cache cache;

    @Autowired
    private OrderChangeLogService orderChangeLogService;
    
    @Autowired
    private PayServiceClient payServiceClient;

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
        if (customer == null||null==customer.getCustomerId()) {
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
        CustomerUser user = UserContext.getCurrentCustomerUser();
        if (null == user) {
            throw new BusinessException(BusinessCode.CODE_1002, "登录用户不存在");
        }
        return orderInfoMapper.selectOrderInfoByOrderNoAndCustomer(condition.getOrderNo(),user.getCustomerId());
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
        StoreUser store = UserContext.getCurrentStoreUser();
        if (null == store) {
            throw new BusinessException(BusinessCode.WRONG_STORE_ID, "门店不存在");
        }
        return orderInfoMapper.selectOrderInfoByOrderNoAndStore(condition.getOrderNo(), store.getBusinessId());
    }

    @Override
    public StoreOrderSalesSummaryVO getStoreIntradayOrderSalesSummary(long storeId) {
        logger.info("获取门店当天订单销售汇总信息开始：storeId={}", storeId);
        String intradayOrderSalesSummaryStr = cache.hget(CacheName.CACHE_KEY_STORE_ORDER_INTRADAY_SALESSUMMARY, storeId + "");
        if (StringUtils.isNotBlank(intradayOrderSalesSummaryStr)) {
            logger.info("从缓存中获取门店当天销售数据，直接返回：storeId={}", storeId);
            return JsonUtil.parseJSONObject(intradayOrderSalesSummaryStr, StoreOrderSalesSummaryVO.class);
        }
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = null;
        String lockKey = CacheName.CACHE_KEY_STORE_ORDER_INTRADAY_SALESSUMMARY + "LOCK" + storeId;
        Lock lock = new RedisLock(cache, lockKey, 50000);
        try {
            lock.lock();
            intradayOrderSalesSummaryStr = cache.hget(CacheName.CACHE_KEY_STORE_ORDER_INTRADAY_SALESSUMMARY, storeId + "");
            if (StringUtils.isNotBlank(intradayOrderSalesSummaryStr)) {
                logger.info("从缓存中获取门店当天销售数据，直接返回：storeId={}", storeId);
                return JsonUtil.parseJSONObject(intradayOrderSalesSummaryStr, StoreOrderSalesSummaryVO.class);
            }
            //查询当天数据
            //获取当天最后一秒
            long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59, 999999999)).getTime();
            //获取当天开始第一秒
            long startSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0)).getTime();
            Date startDateTime = new Date(startSecond);
            Date endDateTime = new Date(lastSecond);
            storeOrderSalesSummaryVO = calculateStoreOrderSalesSummary(storeId, startDateTime, endDateTime);
            //先删除所有的redis门店当日下单用户数据
            cache.del(OrderUtil.getStoreOrderCustomerIdSetField(storeId));
            if (storeOrderSalesSummaryVO != null && storeOrderSalesSummaryVO.getCustomerNum() != null && storeOrderSalesSummaryVO.getCustomerNum() > 0 ) {
                //获取当前下单的用户及所对应的订单数,并存入redis用于进行缓存计算
                List<Map<String, Long>> customerOrderCountList = orderInfoMapper.getStoreOrderDistinctCustomerIds(storeId, startDateTime, endDateTime);
                if (CollectionUtils.isNotEmpty(customerOrderCountList)) {
                    Map<String, Double> totalCustomerOrderCountMap = new HashMap<>(customerOrderCountList.size());
                    for (Iterator<Map<String, Long>> iterator = customerOrderCountList.iterator(); iterator.hasNext();) {
                        Map<String, Long> customerOrderCountMap = iterator.next();
                        totalCustomerOrderCountMap.put(customerOrderCountMap.get("key").toString(), customerOrderCountMap.get("value").doubleValue());
                    }
                    cache.zadd(OrderUtil.getStoreOrderCustomerIdSetField(storeId), totalCustomerOrderCountMap);
                    cache.expire(OrderUtil.getStoreOrderCustomerIdSetField(storeId), Integer.valueOf(DurationFormatUtils.formatDuration(lastSecond - System.currentTimeMillis(), "s")));
                }
            }
            //设置到缓存
            cache.hset(CacheName.CACHE_KEY_STORE_ORDER_INTRADAY_SALESSUMMARY, storeId + "", JsonUtil.toJSONString(storeOrderSalesSummaryVO));
            //当天有效
            cache.expire(CacheName.CACHE_KEY_STORE_ORDER_INTRADAY_SALESSUMMARY, Integer.valueOf(DurationFormatUtils.formatDuration(lastSecond - System.currentTimeMillis(), "s")));
        } finally {
            lock.unlock();
        }
        logger.info("获取门店当天订单销售汇总信息结束：storeId={}", storeId);
        return storeOrderSalesSummaryVO;
    }
    
    @Override
    public StoreOrderSalesSummaryVO getStoreMonthOrderSalesSummary(long storeId) {
        logger.info("获取门店当月商品销量信息开始：storeId={}", storeId);
        String monthOrderSalesSummaryStr = cache.hget(CacheName.CACHE_KEY_STORE_ORDER_MONTH_SALESSUMMARY, storeId + "");
        if (StringUtils.isNotBlank(monthOrderSalesSummaryStr)) {
            logger.info("从缓存中获取门店当月销售数据，直接返回：storeId={}", storeId);
            return JsonUtil.parseJSONObject(monthOrderSalesSummaryStr, StoreOrderSalesSummaryVO.class);
        }
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = null;
        String lockKey = CacheName.CACHE_KEY_STORE_ORDER_MONTH_SALESSUMMARY + "LOCK" + storeId;
        Lock lock = new RedisLock(cache, lockKey, 50000);
        try {
            lock.lock();
            monthOrderSalesSummaryStr = cache.hget(CacheName.CACHE_KEY_STORE_ORDER_MONTH_SALESSUMMARY, storeId + "");
            if (StringUtils.isNotBlank(monthOrderSalesSummaryStr)) {
                logger.info("从缓存中获取门店当月销售数据，直接返回：storeId={}", storeId);
                return JsonUtil.parseJSONObject(monthOrderSalesSummaryStr, StoreOrderSalesSummaryVO.class);
            }
            //查询当天数据
            //获取当天最后一秒
            long lastSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 23, 59, 59, 999999999)).getTime();
            //获取当天开始第一秒
            long startSecond = Timestamp.valueOf(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0)).getTime();
            Date startDateTime = new Date(startSecond);
            Date endDateTime = new Date(lastSecond);
            //取前30天开始日期
            startDateTime = DateUtils.addMonths(startDateTime, -1);
            storeOrderSalesSummaryVO = calculateStoreCompletedOrderSalesSummary(storeId, startDateTime, endDateTime);
            //设置到缓存
            cache.hset(CacheName.CACHE_KEY_STORE_ORDER_MONTH_SALESSUMMARY, storeId + "", JsonUtil.toJSONString(storeOrderSalesSummaryVO));
            //当天有效
            cache.expire(CacheName.CACHE_KEY_STORE_ORDER_MONTH_SALESSUMMARY, Integer.valueOf(DurationFormatUtils.formatDuration(lastSecond - System.currentTimeMillis(), "s")));
        } finally {
            lock.unlock();
        }
        logger.info("获取门店当月商品销量信息结束：storeId={};ret={}", storeId, storeOrderSalesSummaryVO);
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
                Set<String> pickUpCodeList;
                do {
                    //批量生成50个提货码
                    pickUpCodeList = generatePickUpCodeList(50);
                    if (!this.orderInfoMapper.getPickUpCodeByStoreId(pickUpCodeList, storeId)) {
                        logger.info("提货码批量生成成功 storeId={},pickUpList={}", storeId, pickUpCodeList);
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
        logger.info("提货码获取 storeId={},code={}", storeId, code);
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
    public OrderInfoDetailVO4Management getOrderDetail4Management(String orderNo) {
        if (StringUtils.isBlank(orderNo)) {
            throw new NullPointerException("订单编号不能为空");
        }
        String cacheStr = cache.get(CacheName.CACHE_ORDER_INFO_4_MANAGEMENT + orderNo);
        if (StringUtils.isNotBlank(cacheStr)) {
            logger.info("订单 orderNo={} 命中缓存 订单信息查询结束", orderNo);
            return JsonUtil.parseJSONObject(cacheStr, OrderInfoDetailVO4Management.class);
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
    
    @Override
    public OrderPayVO getOrderPayInfo(String orderNo, String spbillCreateIp, String deviceInfo, Long customerId, String openid) {
        if (StringUtils.isBlank(orderNo)) {
            throw new BusinessException(BusinessCode.ORDER_NO_EMPTY);
        }
        if (StringUtils.isBlank(spbillCreateIp)) {
            spbillCreateIp = DEFAULT_IP;
        }
        if (customerId == null || StringUtils.isBlank(openid)) {
            throw new BusinessException(BusinessCode.CUSTOMER_ID_EMPTY);
        }
        OrderInfoDetailVO orderInfoDetailVO = orderInfoMapper.selectOrderInfoByOrderNo(orderNo);
        if (orderInfoDetailVO == null || orderInfoDetailVO.getCustomerId().longValue() != customerId.longValue()) {
            throw new BusinessException(BusinessCode.WRONG_ORDERNO);
        }
        if (OrderStatusEnum.WAIT_PAY.getStatusCode() != orderInfoDetailVO.getOrderStatus()) {
            throw new BusinessException(BusinessCode.WRONG_ORDER_STATUS);
        }
        if (orderInfoDetailVO.getOrderItemVoList() == null || orderInfoDetailVO.getOrderItemVoList().isEmpty()) {
            throw new BusinessException(BusinessCode.ORDER_SKU_EMPTY);
        }
        OrderPayVO ret;
        String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + orderNo;
        Lock lock = new RedisLock(cache, lockKey, 5000);
        if (lock.tryLock()) {
            logger.info("订单：{},customerId={},openid={};发起支付，获取支付信息开始", orderNo, customerId.toString(), openid);
            try {
                //支付 显示title
                String body = MessageFormat.format(OrderNotifyMsg.ORDER_ITEM_TITLE_4_PAYMENT, orderInfoDetailVO.getOrderItemVoList().get(0).getSkuDesc(), orderInfoDetailVO.getSkuQuantity());
                PayPreOrderCondition payPreOrderCondition = new PayPreOrderCondition();
                payPreOrderCondition.setBody(body);
                payPreOrderCondition.setOutOrderNo(orderNo);
                payPreOrderCondition.setDeviceInfo(deviceInfo);
                payPreOrderCondition.setOpenid(openid);
                payPreOrderCondition.setPayType(orderInfoDetailVO.getPayType());
                payPreOrderCondition.setSpbillCreateIp(spbillCreateIp);
                payPreOrderCondition.setTotalAmount(orderInfoDetailVO.getRealPaymentMoney());
                ResponseResult<OrderPayVO> responseResult = payServiceClient.orderPay(payPreOrderCondition);
                if (responseResult == null || responseResult.getCode() != BusinessCode.CODE_OK || responseResult.getData() == null) {
                	logger.info("----------------AAAAAAAAA--");
                	throw new BusinessException(BusinessCode.ORDER_GET_PAY_INFO_ERROR);
                }
                ret = responseResult.getData();
            }finally {
                lock.unlock();
            }
        }else {
            logger.info("订单：{},customerId={},openid={};发起支付，出现并发，本次忽略");
            ret = null;
        }
        return ret;
    }

    /**
     * 一次生成多个个提货码
     *
     * @return 提货码列表
     */
    private static Set<String> generatePickUpCodeList(int size) {
        Set<String> pickUpList = new HashSet<>();
            for (int i = 0; i < size; i++) {
                String pickUpCode = (int) ((Math.random() * 9 + 1) * 1000) + "";
                pickUpList.add(pickUpCode);
            }
        return pickUpList;
    }
    
    
    /**
     * 根据门店id获取指定时间订单汇总数据
     * @author wangbin
     * @date  2018年8月23日 下午1:14:43
     * @param storeId
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    private StoreOrderSalesSummaryVO calculateStoreOrderSalesSummary(long storeId, Date startDateTime, Date endDateTime) {
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderInfoMapper.getStoreOrderTurnover(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO == null) {
            storeOrderSalesSummaryVO = new StoreOrderSalesSummaryVO();
        }
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO1 = orderInfoMapper.getStoreOrderCustomerNum(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO1 != null) {
            BeanUtils.copyProperties(storeOrderSalesSummaryVO1, storeOrderSalesSummaryVO, "turnover", "orderNum");
        }
        storeOrderSalesSummaryVO.setStoreId(storeId);
        return storeOrderSalesSummaryVO;
    }
    
    private StoreOrderSalesSummaryVO calculateStoreCompletedOrderSalesSummary(long storeId, Date startDateTime,
            Date endDateTime) {
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO = orderInfoMapper.getStoreCompletedOrderTurnover(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO == null) {
            storeOrderSalesSummaryVO = new StoreOrderSalesSummaryVO();
        }
        StoreOrderSalesSummaryVO storeOrderSalesSummaryVO1 = orderInfoMapper.getStoreCompletedOrderCustomerNum(storeId, startDateTime, endDateTime);
        if (storeOrderSalesSummaryVO1 != null) {
            BeanUtils.copyProperties(storeOrderSalesSummaryVO1, storeOrderSalesSummaryVO, "turnover", "orderNum");
        }
        storeOrderSalesSummaryVO.setStoreId(storeId);
        return storeOrderSalesSummaryVO;
    }
}
