package com.winhxd.b2c.order;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import com.winhxd.b2c.common.mq.event.support.EnableEventMessage;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.order.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@Import(MicroServiceConfig.class)
@EnableEventMessage
public class OrderServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(OrderServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        log.info("订单服务启动完成...");
    }
}
