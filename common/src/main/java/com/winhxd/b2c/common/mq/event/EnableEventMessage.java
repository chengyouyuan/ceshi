package com.winhxd.b2c.common.mq.event;

import com.winhxd.b2c.common.mq.event.support.EventMessageConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用事件消息
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EventMessageConfig.class)
public @interface EnableEventMessage {
}
