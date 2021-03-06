package com.winhxd.b2c.promotion;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import com.winhxd.b2c.common.mq.event.support.EnableEventMessage;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.amqp.RabbitMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {RabbitMetricsAutoConfiguration.class})
@MapperScan("com.winhxd.b2c.promotion.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(MicroServiceConfig.class)
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@EnableEventMessage
public class PromotionServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(PromotionServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PromotionServiceApplication.class, args);
        log.info("促销服务启动完成...");
    }
}
