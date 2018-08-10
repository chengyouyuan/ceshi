package com.winhxd.b2c.common.config;

import com.winhxd.b2c.common.context.support.ContextInitFilter;
import com.winhxd.b2c.common.context.support.ContextRequestInterceptor;
import com.winhxd.b2c.common.exception.support.BusinessDecoder;
import com.winhxd.b2c.common.exception.support.ServiceHandlerExceptionResolver;
import com.winhxd.b2c.common.mq.support.MessageQueueConfig;
import com.winhxd.b2c.common.config.support.ControllerChecker;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 微服务通用配置类
 *
 * @author lixiaodong
 */
@Import(MessageQueueConfig.class)
public class MicroServiceConfig extends CommonConfig {

    /**
     * 统一异常处理
     */
    @Bean
    public ServiceHandlerExceptionResolver serviceHandlerExceptionResolver() {
        return new ServiceHandlerExceptionResolver();
    }

    /**
     * 初始化当前用户信息
     */
    @Bean
    public ContextInitFilter contextInitFilter() {
        return new ContextInitFilter();
    }

    /**
     * 传递当前用户信息
     */
    @Bean
    public ContextRequestInterceptor contextRequestInterceptor() {
        return new ContextRequestInterceptor();
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
