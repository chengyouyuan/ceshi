package com.winhxd.b2c.gateway.config;

import com.winhxd.b2c.common.context.version.VersionedZoneAvoidanceRule;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;


@Configuration
public class FilterConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public VersionedZoneAvoidanceRule versionedZoneAvoidanceRule() {
        return new VersionedZoneAvoidanceRule();
    }
}
