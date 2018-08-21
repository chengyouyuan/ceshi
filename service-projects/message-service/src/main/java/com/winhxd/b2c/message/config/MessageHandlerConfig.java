package com.winhxd.b2c.message.config;

import com.winhxd.b2c.message.service.impl.MessageSendMqHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * @author jujinbiao
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
