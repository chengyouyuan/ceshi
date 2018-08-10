package com.winhxd.b2c.admin.config;

import com.winhxd.b2c.admin.utils.jsonTemplates.JsonTemplatesUtils;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * JSON模板配置
 *
 * @author songkai
 * @date 2018/8/8 14:56
 * @description
 */
@Configuration
public class JsonTemplatesConfig {

    @Resource
    private Cache cache;

    @Bean
    public JsonTemplatesUtils jsonTemplatesUtils() {
        cache.del(CacheName.JSON_TEMPLATES);
        JsonTemplatesUtils jsonTemplatesUtils = new JsonTemplatesUtils("/jsonTemplates");
        return jsonTemplatesUtils;
    }
}
