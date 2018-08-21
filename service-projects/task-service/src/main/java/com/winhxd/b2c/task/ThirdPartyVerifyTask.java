package com.winhxd.b2c.task;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.pay.condition.ThirdPartyVerifyAccountingCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.pay.VerifyServiceClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 第三支付平台出对账单后，由该任务标记费用明细与支付平台结算状态为已结算
 */
@Component
public class ThirdPartyVerifyTask {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Autowired
    private Cache cache;

    @Autowired
    private VerifyServiceClient verifyServiceClient;

    @Scheduled(cron = "0/30 * * * * ?")
    public void thirdPartyVerifyAccounting() {
        String key = "third-party-not-verify-order-set";
        // 预计执行一小时
        long expires = 1000 * 60 * 60;
        String lockKey = key + "-lock";
        RedisLock lock = new RedisLock(cache, lockKey, expires);
        boolean isCanGetLock = lock.tryLock(3, TimeUnit.SECONDS);
        if (isCanGetLock) {
            log.info("与支付平台结算状态更新任务开始执行");
            int orderCount;
            int updateCount = 0;
            int errorCount = 0;
            try {
                // 初始化时，清空旧数据
                cache.del(key);
                // 未标记与支付平台已结算的订单号
                ResponseResult<List<String>> orderNoListResponseResult = verifyServiceClient.thirdPartyNotVerifyOrderNoList();
                if (orderNoListResponseResult != null && orderNoListResponseResult.getCode() == 0) {
                    List<String> orderNoList = orderNoListResponseResult.getData();
                    orderCount = orderNoList.size();
                    // 将订单号存入redis，减少应用内存开销
                    for (String orderNo : orderNoList) {
                        cache.sadd(key, orderNo);
                    }
                } else {
                    throw new BusinessException(orderNoListResponseResult.getCode(), orderNoListResponseResult.getMessage());
                }
                while (cache.scard(key) > 0) {
                    String orderNo = cache.spop(key);
                    ThirdPartyVerifyAccountingCondition condition = new ThirdPartyVerifyAccountingCondition();
                    condition.setOrderNo(orderNo);
                    try {
                        ResponseResult<Integer> responseResult = verifyServiceClient.thirdPartyVerifyAccounting(condition);
                        if (responseResult != null && responseResult.getCode() == 0) {
                            updateCount += responseResult.getData();
                        } else {
                            throw new BusinessException(responseResult.getCode(), responseResult.getMessage());
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage());
                        errorCount++;
                    }
                }
                log.info("与支付平台结算状态更新任务执行完成，查询出[{}]笔订单，共结算[{}]笔订单，发生异常[{}]笔订单", orderCount, updateCount, errorCount);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                cache.del(key);
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        } else {
            log.warn("与支付平台结算状态更新任务正在运行，本次不执行");
        }
    }
}
