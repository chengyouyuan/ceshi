package com.winhxd.b2c.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.winhxd.b2c.pay.service.impl.MessageSendMqHandler;

/**
 * @author liuhanning
 * @className MessageHandlerConfig
 * @description
 */
@Configuration
@Profile({"test","prod"})
public class MessageHandlerConfig {
    @Bean
    public MessageSendMqHandler messageSendMqHandler(){
        return new MessageSendMqHandler();
    }
}
