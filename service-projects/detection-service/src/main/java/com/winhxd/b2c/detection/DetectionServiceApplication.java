package com.winhxd.b2c.detection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign.detection")
public class DetectionServiceApplication {

    private static final Logger log = LoggerFactory.getLogger(DetectionServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DetectionServiceApplication.class, args);
        log.info("监测服务启动完成...");
    }
}
