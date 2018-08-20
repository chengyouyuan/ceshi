package com.winhxd.b2c.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.amqp.RabbitMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import com.winhxd.b2c.common.mq.event.support.EnableEventMessage;

@SpringBootApplication(exclude = {RabbitMetricsAutoConfiguration.class})
@EnableEventMessage
@EnableScheduling        //启用定时任务
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@Import(MicroServiceConfig.class)
public class TaskServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(TaskServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
        log.info("计划任务服务启动完成...");
    }
}
