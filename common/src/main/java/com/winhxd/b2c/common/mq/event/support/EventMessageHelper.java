package com.winhxd.b2c.common.mq.event.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 帮助类
 *
 * @author lixiaodong
 */
public class EventMessageHelper {
    private static final Logger logger = LoggerFactory.getLogger(EventMessageHelper.class);

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    public static <T> String toJson(String eventId, T eventObject) {
        try {
            EventTransferObject<T> eto = new EventTransferObject<>(eventId, eventObject);
            return objectMapper.writeValueAsString(eto);
        } catch (JsonProcessingException e) {
            logger.error("EventMessageHelper->toJson", e);
        }
        return null;
    }

    public static <T> EventTransferObject<T> toTransferObject(String json, Class<T> clazz) throws IOException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(EventTransferObject.class, clazz);
        return objectMapper.readValue(json, javaType);
    }

    public static class EventTransferObject<T> {
        private String eventId;
        private T eventObject;

        public EventTransferObject() {
        }

        public EventTransferObject(String eventId, T eventObject) {
            this.eventId = eventId;
            this.eventObject = eventObject;
        }

        public String getEventId() {
            return eventId;
        }

        public void setEventId(String eventId) {
            this.eventId = eventId;
        }

        public T getEventObject() {
            return eventObject;
        }

        public void setEventObject(T eventObject) {
            this.eventObject = eventObject;
        }
    }
}
