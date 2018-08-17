package com.winhxd.b2c.common.cache.redis;

import com.winhxd.b2c.common.cache.Cache;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Set;

/**
 * @author lixiaodong
 */
@ConditionalOnProperty(prefix = "redis.sentinel", name = {"master", "nodes"})
public class RedisSentinelCacheAutoConfiguration {
    /**
     * Redis集群配置
     */
    @Value("${redis.sentinel.master}")
    private String master;

    @Value("${redis.sentinel.nodes}")
    private String nodes;

    @Value("${redis.sentinel.password}")
    private String password;

    @Value("${redis.sentinel.timeout:5000}")
    private Integer timeout;

    @ConfigurationProperties(prefix = "redis.sentinel")
    @Bean
    public JedisPoolConfig redisSentinelCacheJedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public Cache redisSentinelCache() {
        JedisPoolConfig config = redisSentinelCacheJedisPoolConfig();

        Set<String> sentinels = StringUtils.commaDelimitedListToSet(nodes);

        JedisSentinelPool jedisSentinelPool = new JedisSentinelPool(master, sentinels, config, timeout, password);
        Object cache = Proxy.newProxyInstance(
                RedisSentinelCacheAutoConfiguration.class.getClassLoader(),
                new Class[]{Cache.class},
                new CacheInvocationHandler(jedisSentinelPool)
        );
        return (Cache) cache;
    }

    private static class CacheInvocationHandler implements InvocationHandler {
        private JedisSentinelPool pool;

        public CacheInvocationHandler(JedisSentinelPool pool) {
            this.pool = pool;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            try (Jedis jedis = pool.getResource()) {
                Object invoke = method.invoke(jedis, args);
                return invoke;
            }
        }
    }
}
