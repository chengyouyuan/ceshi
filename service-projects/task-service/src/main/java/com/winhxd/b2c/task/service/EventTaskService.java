package com.winhxd.b2c.task.service;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.mq.event.EventMessageSender;
import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.mq.event.support.EventMessageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class EventTaskService {
    private static final double EVENT_HISTORY_BEFORE = 600000D;

    @Autowired
    private Cache cache;

    @Autowired
    private EventMessageSender eventMessageSender;

    @Scheduled(fixedDelay = 60000)
    public void checkEventSentHistory() {
        double ms = System.currentTimeMillis() - EVENT_HISTORY_BEFORE;
        for (EventType et : EventType.values()) {
            log.info("开始检查事件: {}", et);
            String idKey = CacheName.EVENT_MESSAGE_ID + et;
            String bodyKey = CacheName.EVENT_MESSAGE_BODY + et;
            Set<String> idSet = cache.zrangeByScore(idKey, 0, ms);
            if (CollectionUtils.isNotEmpty(idSet)) {
                String[] ids = idSet.toArray(new String[0]);
                List<String> bodies = cache.hmget(bodyKey, ids);
                String body;
                for (int i = 0; i < ids.length; i++) {
                    body = bodies.get(i);
                    if (StringUtils.isNotBlank(body)) {
                        try {
                            EventMessageHelper.EventTransferObject<?> transferObject
                                    = EventMessageHelper.toTransferObject(body, et.getEventObjectClass());
                            eventMessageSender.send(et, transferObject.getEventKey(), transferObject.getEventObject());
                        } catch (Exception e) {
                            log.error("事件处理异常:" + et + "-" + ids[i], e);
                        }
                    }
                }
            }
            log.info("结束检查事件: {}", et);
        }
    }
}
