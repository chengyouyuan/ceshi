package com.winhxd.b2c.task;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import com.winhxd.b2c.common.mq.event.EnableEventMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableEventMessage
@Import(MicroServiceConfig.class)
public class TaskServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
        log.info("计划任务服务启动完成...");
    }
}
