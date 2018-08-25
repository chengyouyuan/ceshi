package com.winhxd.b2c.common.mq.support.zipkin;

import com.winhxd.b2c.common.mq.support.MessageQueueProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import zipkin2.reporter.Sender;
import zipkin2.reporter.amqp.RabbitMQSender;

/**
 * Zipkin配置类
 *
 * @author lixiaodong
 */
public class ZipkinRabbitConfig {
    @Value("${spring.zipkin.rabbitmq.queue:zipkin}")
    private String queue;

    @Bean(name = "zipkinMessageQueueProperties")
    @ConfigurationProperties(prefix = "mq.zipkin")
    public MessageQueueProperties zipkinMessageQueueProperties() {
        return new MessageQueueProperties();
    }

    @Bean(name = "zipkinCachingConnectionFactory")
    public CachingConnectionFactory zipkinCachingConnectionFactory(MessageQueueProperties zipkinMessageQueueProperties) {
        CachingConnectionFactory factory = new CachingConnectionFactory();
        factory.setAddresses(zipkinMessageQueueProperties.getAddress());
        factory.setUsername(zipkinMessageQueueProperties.getUsername());
        factory.setPassword(zipkinMessageQueueProperties.getPassword());
        if (StringUtils.isNotBlank(zipkinMessageQueueProperties.getVirtualHost())) {
            factory.setVirtualHost(zipkinMessageQueueProperties.getVirtualHost());
        }
        return factory;
    }

    @Bean
    public Sender rabbitSender(
            @Qualifier("zipkinCachingConnectionFactory") CachingConnectionFactory zipkinCachingConnectionFactory,
            @Qualifier("zipkinMessageQueueProperties") MessageQueueProperties zipkinMessageQueueProperties) {
        return RabbitMQSender.newBuilder()
                .connectionFactory(zipkinCachingConnectionFactory.getRabbitConnectionFactory())
                .queue(this.queue)
                .addresses(zipkinMessageQueueProperties.getAddress())
                .build();
    }
}