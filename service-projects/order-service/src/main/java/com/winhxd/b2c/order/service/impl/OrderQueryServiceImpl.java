package com.winhxd.b2c.order.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import com.winhxd.b2c.common.cache.Lock;
import com.winhxd.b2c.common.cache.RedisLock;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(OrderQueryServiceImpl.class);

    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Resource
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
        //TODO 调用商品仓库添加商品图片URL和商品名称
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
        }
        return orderSalesSummaryVO;
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
        String getCodeKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_QUEUE + storeId;
        if (this.cache.llen(getCodeKey) < 1) {
            String lockKey = CacheName.CACHE_KEY_STORE_PICK_UP_CODE_GENERATE + storeId;
            Lock lock = new RedisLock(cache, lockKey, 1000);
            try {
                lock.lock();
                List<String> pickUpCodeList;
                do {
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
            } finally {
                lock.unlock();
            }
        }
        return this.cache.lpop(getCodeKey);
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
