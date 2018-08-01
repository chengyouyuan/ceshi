package com.winhxd.b2c.common.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.Set;

/**
 * @author lixiaodong
 */
public class RedisClusterCache extends JedisCluster implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisClusterCache.class);

    public RedisClusterCache(Set<HostAndPort> jedisClusterNode, int timeout, int maxAttempts, GenericObjectPoolConfig poolConfig) {
        super(jedisClusterNode, timeout, maxAttempts, poolConfig);
    }
}
