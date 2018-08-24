package com.winhxd.b2c.monitor;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.turbine.EnableTurbine;

@EnableAdminServer
@SpringBootApplication
@EnableHystrixDashboard
@EnableTurbine
public class MonitorServerApplication {
    private static final Logger log = LoggerFactory.getLogger(MonitorServerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(MonitorServerApplication.class, args);
        log.info("监控中心启动完成...");
    }
}
