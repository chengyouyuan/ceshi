package com.winhxd.b2c.system.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@EnableCaching
public class RedisConfiguration {

    /**
     * Redis 操作对象（支持事务）
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param connectionFactory
     * @return org.springframework.data.redis.core.RedisTemplate
     */
    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

    /**
     * StringRedis 操作对象（支持事务）
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param connectionFactory
     * @return org.springframework.data.redis.core.StringRedisTemplate
     */
    @Bean
    public StringRedisTemplate springRedisTemplate(RedisConnectionFactory connectionFactory){
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate(connectionFactory);
        stringRedisTemplate.setEnableTransactionSupport(true);
        return stringRedisTemplate;
    }

    /**
     * Redis 缓存管理器（支持事务）
     * @author zhangzhengyang
     * @date 2018/8/1
     * @param connectionFactory
     * @return org.springframework.data.redis.cache.RedisCacheManager
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return RedisCacheManager.builder(connectionFactory).transactionAware().build();
    }


}
