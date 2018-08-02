package com.winhxd.b2c.common.cache.redis;

import com.winhxd.b2c.common.cache.Cache;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * @author lixiaodong
 */
public class RedisClusterCache extends JedisCluster implements Cache {
    public RedisClusterCache(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
    }
}
