package com.winhxd.b2c.task.service;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.mq.event.support.EventCorrelationData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 事件消息补偿任务
 *
 * @author lixiaodong
 */
@Component
@Slf4j
public class EventMessageTask {
    /**
     * 10分钟
     */
    private static final double EVENT_HISTORY_BEFORE = 600000D;
    /**
     * 1分钟
     */
    private static final int TASK_INTERVAL = 60000;

    @Autowired
    private Cache cache;

    @Autowired
    @Qualifier("eventRabbitTemplate")
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = TASK_INTERVAL, initialDelay = TASK_INTERVAL)
    public void checkEventSentHistory() {
        log.info("事件补偿计划任务开始");
        double ms = System.currentTimeMillis() - EVENT_HISTORY_BEFORE;
        for (EventType et : EventType.values()) {
            log.info("开始检查事件:{}", et);
            String idKey = CacheName.EVENT_MESSAGE_ID + et;
            String bodyKey = CacheName.EVENT_MESSAGE_BODY + et;
            Set<String> idSet = cache.zrangeByScore(idKey, 0, ms);
            if (CollectionUtils.isNotEmpty(idSet)) {
                String[] ids = idSet.toArray(new String[0]);
                List<String> bodies = cache.hmget(bodyKey, ids);
                String key, body;
                for (int i = 0; i < ids.length; i++) {
                    key = ids[i];
                    body = bodies.get(i);
                    if (StringUtils.isNotBlank(body)) {
                        rabbitTemplate.convertAndSend(et.toString(), null, body, new EventCorrelationData(key, et));
                        log.info("事件补偿发送: {} - {} - {}", et, key, body);
                    } else {
                        log.warn("事件补偿错误: {} - {}", et, key);
                    }
                }
            }
            log.info("结束检查事件:{},count={}", et, idSet.size());
        }
        log.info("事件补偿计划任务结束");
    }
}
