package com.winhxd.b2c.admin;

import com.winhxd.b2c.common.config.CommonConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@Import(CommonConfig.class)
public class AdminServerApplication {
    private static final Logger log = LoggerFactory.getLogger(AdminServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AdminServerApplication.class, args);
        log.info("管理后台服务启动完成...");
    }

}
