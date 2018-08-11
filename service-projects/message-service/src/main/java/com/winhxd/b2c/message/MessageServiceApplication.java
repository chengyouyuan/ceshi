package com.winhxd.b2c.message;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.message.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableScheduling
@Import(MicroServiceConfig.class)
public class MessageServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(MessageServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MessageServiceApplication.class, args);
        log.info("消息服务启动完成...");
    }
}
