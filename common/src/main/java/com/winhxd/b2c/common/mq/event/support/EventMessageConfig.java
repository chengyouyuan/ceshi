package com.winhxd.b2c.common.mq.event.support;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.cache.RedisLock;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.mq.event.*;
import com.winhxd.b2c.common.mq.support.MessageQueueConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author lixiaodong
 */
@Import(MessageQueueConfig.class)
public class EventMessageConfig implements BeanPostProcessor, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueueConfig.class);
    /**
     * 消费分布式锁超时时间
     */
    private static final int LOCK_EXPIRES = 10000;

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    @Qualifier("normalCachingConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Autowired
    private Cache cache;

    @Bean
    public RabbitTemplate eventRabbitTemplate(CachingConnectionFactory normalCachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(normalCachingConnectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            EventCorrelationData data = (EventCorrelationData) correlationData;
            if (ack) {
                String idKey = CacheName.EVENT_MESSAGE_ID + data.getEventType().toString();
                String bodyKey = CacheName.EVENT_MESSAGE_BODY + data.getEventType().toString();
                cache.zrem(idKey, data.getId());
                cache.hdel(bodyKey, data.getId());
                logger.info("事件消息发送成功: {} - {}", data.getEventType(), data.getId());
            } else {
                logger.warn("事件消息发送失败: {} - {}, {}", data.getEventType(), data.getId(), cause);
            }
        });
        return rabbitTemplate;
    }

    @Bean
    public EventMessageSender eventMessageSender() {
        return new EventMessageSender();
    }

    @Bean
    public List<Declarable> declarableEventList() {
        List<Declarable> list = new ArrayList<>();
        for (EventTypeHandler handler : EventTypeHandler.values()) {
            EventType dest = handler.getEventType();
            FanoutExchange exchange = new FanoutExchange(dest.toString(), true, false);
            list.add(exchange);
            Queue queue = new Queue(handler.toString(), true, false, false);
            Binding binding = BindingBuilder.bind(queue).to(exchange);
            list.add(queue);
            list.add(binding);
        }
        return list;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        ReflectionUtils.doWithMethods(targetClass, method -> {
            EventMessageListener annotation = AnnotationUtils.getAnnotation(method, EventMessageListener.class);
            if (annotation != null) {
                EventType eventType = annotation.value().getEventType();
                Class<?> eventObjectClass = eventType.getEventObjectClass();
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes == null || parameterTypes.length != 2
                        || !String.class.isAssignableFrom(parameterTypes[0])
                        || !eventObjectClass.isAssignableFrom(parameterTypes[1])) {
                    throw new IllegalArgumentException("事件消息监听方法参数错误: " + targetClass.getCanonicalName() + "#" + method.getName());
                }
                SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
                listenerContainer.setQueueNames(annotation.value().toString());
                listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
                if (StringUtils.isNotBlank(annotation.concurrency())) {
                    listenerContainer.setConcurrency(annotation.concurrency());
                }
                listenerContainer.setMessageListener(message -> {
                    String body = new String(message.getBody(), StandardCharsets.UTF_8);
                    EventMessageHelper.EventTransferObject<?> transferObject = null;
                    try {
                        transferObject = EventMessageHelper.toTransferObject(body, eventObjectClass);
                    } catch (IOException e) {
                        logger.error("MQ消费异常:" + e.toString(), e);
                        throw new RuntimeException("MQ消费异常:" + e.toString(), e);
                    }
                    String key = CacheName.EVENT_MESSAGE_HANDLER + eventType.toString() + ":" + transferObject.getEventId();
                    RedisLock redisLock = new RedisLock(cache, key, LOCK_EXPIRES);
                    redisLock.tryLock(LOCK_EXPIRES, TimeUnit.MILLISECONDS);
                    try {
                        method.invoke(bean, transferObject.getEventId(), transferObject.getEventObject());
                    } catch (IllegalAccessException e) {
                        logger.error("MQ消费异常", e);
                    } catch (InvocationTargetException e) {
                        logger.error("MQ消费异常", e.getCause());
                        throw new RuntimeException("MQ消费异常", e);
                    } finally {
                        redisLock.unlock();
                    }
                });
                beanFactory.registerSingleton(beanName + "#" + method.getName(), listenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }
}