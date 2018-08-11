package com.winhxd.b2c.promotion;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.promotion.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(MicroServiceConfig.class)
public class PromotionServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(PromotionServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PromotionServiceApplication.class, args);
        log.info("促销服务启动完成...");
    }
}
