package com.winhxd.b2c.system;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan({"com.winhxd.b2c.system.*.dao","com.winhxd.b2c.system.*.*.dao","com.winhxd.b2c.system.dao"})
@ComponentScan(basePackages = "com.winhxd.b2c")
@EnableFeignClients(basePackages = "com.winhxd.b2c.common.feign")
@Import(MicroServiceConfig.class)
public class SystemServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(SystemServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(SystemServiceApplication.class, args);
        log.info("基础服务启动完成...");
    }
}
