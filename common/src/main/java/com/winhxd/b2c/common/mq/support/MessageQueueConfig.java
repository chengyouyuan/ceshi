package com.winhxd.b2c.common.mq.support;

import com.winhxd.b2c.common.mq.StringMessageListener;
import com.winhxd.b2c.common.mq.MessageQueueDestination;
import com.winhxd.b2c.common.mq.MessageQueueHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lixiaodong
 */
public class MessageQueueConfig implements BeanPostProcessor, BeanFactoryAware {
    private static final Logger logger = LoggerFactory.getLogger(MessageQueueConfig.class);

    private DefaultListableBeanFactory beanFactory;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public List<Declarable> declarableList() {
        List<Declarable> list = new ArrayList<>();
        for (MessageQueueHandler listener : MessageQueueHandler.values()) {
            MessageQueueDestination dest = listener.getDestination();
            FanoutExchange exchange = new FanoutExchange(dest.toString(), true, false);
            exchange.setDelayed(dest.isDelayed());
            list.add(exchange);
            Queue queue = new Queue(listener.toString(), true, false, false);
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
            StringMessageListener annotation = AnnotationUtils.getAnnotation(method, StringMessageListener.class);
            if (annotation != null) {
                SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
                listenerContainer.setQueueNames(annotation.value().toString());
                listenerContainer.setAcknowledgeMode(AcknowledgeMode.AUTO);
                if (StringUtils.isNotBlank(annotation.concurrency())) {
                    listenerContainer.setConcurrency(annotation.concurrency());
                }
                listenerContainer.setMessageListener(message -> {
                    String body = new String(message.getBody(), StandardCharsets.UTF_8);
                    try {
                        method.invoke(bean, body);
                    } catch (IllegalAccessException e) {
                        logger.error("MQ消费异常", e);
                    } catch (InvocationTargetException e) {
                        logger.error("MQ消费异常", e.getCause());
                        throw new RuntimeException("MQ消费异常", e);
                    }
                });
                beanFactory.registerSingleton(bean.getClass().getName() + "#" + method.getName(), listenerContainer);
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
        return bean;
    }
}
