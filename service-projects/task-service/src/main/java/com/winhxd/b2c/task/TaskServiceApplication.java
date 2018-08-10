package com.winhxd.b2c.task;

import com.winhxd.b2c.common.config.MicroServiceConfig;
import com.winhxd.b2c.common.mq.StringMessageSender;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(MicroServiceConfig.class)
@EnableRabbit
public class TaskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }

    @Bean
    public StringMessageSender sender() {
        return new StringMessageSender();
    }
}
