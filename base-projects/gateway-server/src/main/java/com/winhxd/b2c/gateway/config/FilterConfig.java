//package com.winhxd.b2c.gateway.config;
//
//import com.winhxd.b2c.common.context.version.VersionedZoneAvoidanceRule;
//import com.winhxd.b2c.gateway.filter.VersionedLoadBalancerClientFilter;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
//import org.springframework.cloud.gateway.filter.LoadBalancerClientFilter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
////@ConditionalOnClass({LoadBalancerClient.class, RibbonAutoConfiguration.class, DispatcherHandler.class})
////@AutoConfigureAfter(RibbonAutoConfiguration.class)
//public class FilterConfig {
//    @Bean
////    @ConditionalOnBean(LoadBalancerClient.class)
//    public LoadBalancerClientFilter loadBalancerClientFilter(LoadBalancerClient client) {
//        return new VersionedLoadBalancerClientFilter(client);
//    }
//
//    @Bean
//    public VersionedZoneAvoidanceRule versionedZoneAvoidanceRule(){
//        return new VersionedZoneAvoidanceRule();
//    }
//}
