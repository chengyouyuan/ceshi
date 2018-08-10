package com.winhxd.b2c.admin.config;

import com.winhxd.b2c.common.exception.support.BusinessDecoder;
import com.winhxd.b2c.common.exception.support.ServiceHandlerExceptionResolver;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lixiaodong
 */
@Configuration
public class BusinessExceptionConfig {
    @Bean
    public BusinessDecoder businessDecoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new BusinessDecoder(messageConverters);
    }

    @Bean
    public ServiceHandlerExceptionResolver serviceHandlerExceptionResolver() {
        return new ServiceHandlerExceptionResolver();
    }
}
