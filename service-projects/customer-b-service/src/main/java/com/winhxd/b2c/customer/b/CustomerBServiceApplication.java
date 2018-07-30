package com.winhxd.b2c.customer.b;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.message.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
public class CustomerBServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerBServiceApplication.class, args);
    }
}
