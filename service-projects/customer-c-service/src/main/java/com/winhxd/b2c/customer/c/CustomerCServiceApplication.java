package com.winhxd.b2c.customer.c;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan("com.winhxd.b2c.message.dao")
@ComponentScan(basePackages = "com.winhxd.b2c")
public class CustomerCServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerCServiceApplication.class, args);
    }
}
