package com.winhxd.b2c.customer;

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
@MapperScan("com.winhxd.b2c.customer.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@Import(MicroServiceConfig.class)
public class CustomerServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(CustomerServiceApplication.class, args);
        log.info("客户服务启动完成...");
    }
}
