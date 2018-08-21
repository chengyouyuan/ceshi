package com.winhxd.b2c.admin.config;

import com.winhxd.b2c.common.context.support.ContextHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * Web请求跨域支持
 *
 * @author lixiaodong
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.addExposedHeader(ContextHelper.TRACER_API_TRACE_ID);
        config.setMaxAge(864000L);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}