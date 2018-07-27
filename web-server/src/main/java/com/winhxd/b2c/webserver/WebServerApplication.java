package com.winhxd.b2c.webserver;

import com.winhxd.b2c.common.i18n.MessageHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class WebServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }

    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }
}
