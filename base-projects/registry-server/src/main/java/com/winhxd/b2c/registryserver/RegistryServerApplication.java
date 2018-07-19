package com.winhxd.b2c.registryserver;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaServer
@EnableAdminServer
@SpringBootApplication
public class RegistryServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryServerApplication.class, args);
    }
}
