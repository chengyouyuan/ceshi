package com.winhxd.b2c.gateway.filter;

import com.winhxd.b2c.common.context.version.VersionContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 可版本控制负载均衡过滤器
 *
 * @author lixiaodong
 */
public class VersionedLoadBalancerClientFilter extends LoadBalancerClientFilter {
    public VersionedLoadBalancerClientFilter(LoadBalancerClient loadBalancer) {
        super(loadBalancer);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String msVer = exchange.getRequest().getHeaders().getFirst(VersionContext.HEADER_NAME);
        if (StringUtils.isNotBlank(msVer)) {
            VersionContext.setVersion(msVer);
        } else {
            VersionContext.clean();
        }
        return super.filter(exchange, chain);
    }
}
