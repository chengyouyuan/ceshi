package com.winhxd.b2c.gateway.config;

import brave.http.HttpAdapter;
import brave.http.HttpSampler;
import com.winhxd.b2c.common.context.version.VersionedZoneAvoidanceRule;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.sleuth.instrument.web.ClientSampler;
import org.springframework.cloud.sleuth.instrument.web.ServerSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class GatewayConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VersionedZoneAvoidanceRule versionedZoneAvoidanceRule() {
        return new VersionedZoneAvoidanceRule();
    }

    @Bean(name = ClientSampler.NAME)
    public HttpSampler sleuthClientSampler() {
        HttpSampler httpSampler = new HttpSampler() {
            @Override
            public <Req> Boolean trySample(HttpAdapter<Req, ?> httpAdapter, Req req) {
                return false;
            }
        };
        return httpSampler;
    }

    @Bean(name = ServerSampler.NAME)
    public HttpSampler sleuthServerSampler() {
        HttpSampler httpSampler = new HttpSampler() {
            @Override
            public <Req> Boolean trySample(HttpAdapter<Req, ?> httpAdapter, Req req) {
                String path = httpAdapter.path(req);
                return StringUtils.isNotBlank(path) && path.startsWith("/api-");
            }
        };
        return httpSampler;
    }
}
