package com.winhxd.b2c.store;

import com.winhxd.b2c.common.config.ServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.store.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(ServiceConfig.class)
public class StoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
    }
}
