package com.winhxd.b2c.admin;

import com.winhxd.b2c.common.i18n.MessageHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.winhxd.b2c")
public class AdminServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
    }

    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }
}
