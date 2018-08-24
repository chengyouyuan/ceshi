package com.winhxd.b2c.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class RegistryServerApplication {
    private static final Logger log = LoggerFactory.getLogger(RegistryServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(RegistryServerApplication.class, args);
        log.info("注册中心启动完成...");
    }
}
