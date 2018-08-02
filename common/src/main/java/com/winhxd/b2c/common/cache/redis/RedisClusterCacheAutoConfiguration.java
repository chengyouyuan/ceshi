package com.winhxd.b2c.common.cache.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lixiaodong
 */
@Configuration
@ConditionalOnProperty(prefix = "redis.cluster", name = {"jedisClusterNode"})
public class RedisClusterCacheAutoConfiguration {
    /**
     * Redis集群配置
     */
    @Value("${redis.cluster.jedisClusterNode}")
    private String jedisClusterNode;

    @Value("${redis.cluster.maxAttempts:5}")
    private Integer maxAttempts;

    @Value("${redis.cluster.timeout:5000}")
    private Integer timeout;

    @ConfigurationProperties(prefix = "redis.cluster")
    @Bean
    public JedisPoolConfig redisClusterCacheJedisPoolConfig() {
        return new JedisPoolConfig();
    }

    @Bean
    public RedisClusterCache redisClusterCache() {
        JedisPoolConfig config = redisClusterCacheJedisPoolConfig();
        return new RedisClusterCache(parseHostAndPort(jedisClusterNode), timeout, maxAttempts, config);
    }

    private static final Set<HostAndPort> parseHostAndPort(String address) {
        try {
            Set<HostAndPort> hostAndPorts = new HashSet<>();
            if (null == address || address.length() <= 0) {
                return null;
            }
            String[] addressList = address.split(",");
            Pattern hostAndPortsPattern = Pattern.compile("^\\s*(\\S+)\\s*:\\s*(\\d{1,5})\\s*$");
            for (String cluster : addressList) {
                Matcher matcher = hostAndPortsPattern.matcher(cluster);
                boolean isIpPort = matcher.matches();
                if (!isIpPort) {
                    throw new IllegalArgumentException("ip 或 port 不合法");
                }
                HostAndPort hap = new HostAndPort(matcher.group(1), Integer.parseInt(matcher.group(2)));
                hostAndPorts.add(hap);
            }
            return hostAndPorts;
        } catch (IllegalArgumentException ex) {
            throw ex;
        }
    }
}
