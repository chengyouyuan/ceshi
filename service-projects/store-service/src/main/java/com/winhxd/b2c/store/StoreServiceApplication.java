package com.winhxd.b2c.store;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.store.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(MicroServiceConfig.class)
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
public class StoreServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(StoreServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(StoreServiceApplication.class, args);
        log.info("门店服务启动完成...");
    }
}
