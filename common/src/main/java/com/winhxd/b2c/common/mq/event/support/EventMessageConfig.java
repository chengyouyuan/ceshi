package com.winhxd.b2c.common.mq.event.support;

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

/**
 * @author lixiaodong
 */
@Import(MessageQueueConfig.class)
public class EventMessageConfig implements BeanPostProcessor, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueueConfig.class);

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    @Qualifier("normalCachingConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Autowired
    private EventMessageSenderServiceFactory eventMessageSenderServiceFactory;

    @Bean
    public RabbitTemplate eventRabbitTemplate(CachingConnectionFactory normalCachingConnectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(normalCachingConnectionFactory);
        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            EventCorrelationData data = (EventCorrelationData) correlationData;
            if (ack) {
                EventTypeService<Object> service = eventMessageSenderServiceFactory.getService(data.getEventType());
                service.onEventSentSuccess(data.getId());
            } else {
                logger.warn("事件消息发送失败:{}-{}, {}", data.getEventType(), data.getId(), cause);
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

    @Bean
    public EventMessageSenderServiceFactory eventMessageSenderServiceFactory() {
        return new EventMessageSenderServiceFactory();
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
                Class<?> eventObjectClass = annotation.value().getEventType().getEventObjectClass();
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
                    try {
                        String body = new String(message.getBody(), StandardCharsets.UTF_8);
                        EventMessageHelper.EventTransferObject<?> transferObject
                                = EventMessageHelper.toTransferObject(body, eventObjectClass);
                        method.invoke(bean, transferObject.getEventId(), transferObject.getEventObject());
                    } catch (IllegalAccessException e) {
                        logger.error("MQ消费异常", e);
                    } catch (InvocationTargetException e) {
                        logger.error("MQ消费异常", e.getCause());
                        throw new RuntimeException("MQ消费异常", e);
                    } catch (IOException e) {
                        logger.error("MQ消费异常:" + e.toString(), e);
                        throw new RuntimeException("MQ消费异常:" + e.toString(), e);
                    }
                });
                beanFactory.registerSingleton(beanName + "#" + method.getName(), listenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }
}