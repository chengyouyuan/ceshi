package com.winhxd.b2c.pay;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.pay.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(MicroServiceConfig.class)
public class PayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
    }
}
