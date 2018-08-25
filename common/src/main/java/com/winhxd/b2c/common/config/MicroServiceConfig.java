package com.winhxd.b2c.common.config;

import com.winhxd.b2c.common.config.support.ControllerChecker;
import com.winhxd.b2c.common.context.support.ContextInitFilter;
import com.winhxd.b2c.common.exception.support.BusinessDecoder;
import com.winhxd.b2c.common.exception.support.ServiceHandlerExceptionResolver;
import com.winhxd.b2c.common.mq.support.MessageQueueConfig;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 微服务通用配置类
 *
 * @author lixiaodong
 */
@Import({CommonConfig.class, MessageQueueConfig.class})
public class MicroServiceConfig {
    /**
     * 统一异常处理
     */
    @Bean
    public ServiceHandlerExceptionResolver serviceHandlerExceptionResolver() {
        return new ServiceHandlerExceptionResolver();
    }

    @Bean
    public ContextInitFilter contextInitFilter() {
        return new ContextInitFilter();
    }

    @Bean
    public ControllerChecker controllerChecker() {
        return new ControllerChecker();
    }

    @Bean
    public BusinessDecoder businessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessDecoder(messageConverters);
    }
}
