package com.winhxd.b2c.pay;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.amqp.RabbitMetricsAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {RabbitMetricsAutoConfiguration.class})
@MapperScan({"com.winhxd.b2c.pay.dao","com.winhxd.b2c.pay.weixin.dao"})
@ComponentScan(basePackages = "com.winhxd.b2c")
@Import(MicroServiceConfig.class)
public class PayServiceApplication {
    private static final Logger log = LoggerFactory.getLogger(PayServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PayServiceApplication.class, args);
        log.info("支付服务启动完成...");
    }
}
