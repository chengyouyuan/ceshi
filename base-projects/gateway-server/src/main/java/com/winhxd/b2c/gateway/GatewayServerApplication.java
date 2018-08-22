package com.winhxd.b2c.gateway;

import com.winhxd.b2c.common.config.CommonConfig;
import com.winhxd.b2c.common.mq.support.zipkin.ZipkinRabbitConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.config.GatewayLoadBalancerClientAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {GatewayLoadBalancerClientAutoConfiguration.class})
@ComponentScan(basePackages = {"com.winhxd.b2c.gateway", "com.winhxd.b2c.common.cache"})
@Import({CommonConfig.class, ZipkinRabbitConfig.class})
public class GatewayServerApplication {
    private static final Logger log = LoggerFactory.getLogger(GatewayServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayServerApplication.class, args);
        log.info("网关启动完成...");
    }
}
