package com.winhxd.b2c.common.config;

import brave.http.HttpAdapter;
import brave.http.HttpSampler;
import com.winhxd.b2c.common.cache.redis.RedisClusterCacheAutoConfiguration;
import com.winhxd.b2c.common.i18n.MessageHelper;
import org.springframework.cloud.sleuth.instrument.web.ClientSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

/**
 * 基础配置类
 *
 * @author lixiaodong
 */
@Import({RedisClusterCacheAutoConfiguration.class})
public class CommonConfig {
    /**
     * i18n帮助类
     */
    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }


    @Bean(name = ClientSampler.NAME)
    public HttpSampler skipHttpSampler() {
        HttpSampler httpSampler = new HttpSampler() {
            @Override
            public <Req> Boolean trySample(HttpAdapter<Req, ?> httpAdapter, Req req) {
                return false;
            }
        };
        return httpSampler;
    }
}
