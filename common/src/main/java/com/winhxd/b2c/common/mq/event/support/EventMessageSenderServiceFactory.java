package com.winhxd.b2c.common.mq.event.support;

import com.winhxd.b2c.common.mq.event.EventType;
import com.winhxd.b2c.common.mq.event.EventTypeService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lixiaodong
 */
public class EventMessageSenderServiceFactory implements ApplicationListener<ContextRefreshedEvent> {
    private Map<EventType, EventTypeService> eventServiceMap;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Map<String, EventTypeService> beansOfType =
                event.getApplicationContext().getBeansOfType(EventTypeService.class);
        eventServiceMap = new HashMap<>(beansOfType.size());
        for (EventTypeService service : beansOfType.values()) {
            eventServiceMap.put(service.getEventType(), service);
        }
    }

    public <T> EventTypeService<T> getService(EventType eventType) {
        if (CollectionUtils.isEmpty(eventServiceMap)) {
            return null;
        }
        return eventServiceMap.get(eventType);
    }
}
