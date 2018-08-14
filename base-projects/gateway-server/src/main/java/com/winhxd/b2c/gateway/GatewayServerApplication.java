package com.winhxd.b2c.gateway;

import com.winhxd.b2c.common.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = {"com.winhxd.b2c.gateway", "com.winhxd.b2c.common.cache"})
@Import(CommonConfig.class)
public class GatewayServerApplication {
    private static final Logger log = LoggerFactory.getLogger(GatewayServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
        log.info("网关启动完成...");
    }
}
