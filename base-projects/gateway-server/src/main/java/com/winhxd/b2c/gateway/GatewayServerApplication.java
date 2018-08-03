package com.winhxd.b2c.gateway;

import com.winhxd.b2c.common.i18n.MessageHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
    }

    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }
}
