package com.winhxd.b2c.common.context.version;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class VersionedRoundRobinRule extends AbstractLoadBalancerRule {
    private AtomicInteger nextServerCyclicCounter;

    private static Logger log = LoggerFactory.getLogger(RoundRobinRule.class);

    public VersionedRoundRobinRule() {
        nextServerCyclicCounter = new AtomicInteger(0);
    }

    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            log.warn("no load balancer");
            return null;
        }

        Server server = null;
        int count = 0;
        while (server == null && count++ < 10) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();

            if ((upCount == 0) || (serverCount == 0)) {
                log.warn("No up servers available from load balancer: " + lb);
                return null;
            }

            if (allServers.size() > 1) {
                String msVer = VersionContext.getVersion();
                if (StringUtils.isNotBlank(msVer)) {
                    List<Server> matchedServers = new ArrayList<>(allServers.size());
                    List<Server> normalServers = new ArrayList<>(allServers.size());
                    for (Server s : allServers) {
                        if (s instanceof DiscoveryEnabledServer) {
                            String ver = ((DiscoveryEnabledServer) s).getInstanceInfo().getMetadata().get(VersionContext.HEADER_NAME);
                            if (StringUtils.isNotBlank(ver)) {
                                if (msVer.equals(ver)) {
                                    matchedServers.add(s);
                                }
                                continue;
                            }
                        }
                        normalServers.add(s);
                    }
                    if (!CollectionUtils.isEmpty(matchedServers)) {
                        allServers = matchedServers;
                    } else if (!CollectionUtils.isEmpty(normalServers)) {
                        allServers = normalServers;
                    }
                }
            }

            int nextServerIndex = incrementAndGetModulo(allServers.size());
            server = allServers.get(nextServerIndex);

            if (server == null) {
                /* Transient. */
                Thread.yield();
                continue;
            }

            if (server.isAlive() && (server.isReadyToServe())) {
                return (server);
            }

            // Next.
            server = null;
        }

        if (count >= 10) {
            log.warn("No available alive servers after 10 tries from load balancer: "
                    + lb);
        }
        return server;
    }

    /**
     * Inspired by the implementation of {@link AtomicInteger#incrementAndGet()}.
     *
     * @param modulo The modulo to bound the value of the counter.
     * @return The next value.
     */
    private int incrementAndGetModulo(int modulo) {
        for (; ; ) {
            int current = nextServerCyclicCounter.get();
            int next = (current + 1) % modulo;
            if (nextServerCyclicCounter.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
    }
}
