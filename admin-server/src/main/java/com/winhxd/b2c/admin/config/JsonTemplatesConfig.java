package com.winhxd.b2c.admin.config;

import com.winhxd.b2c.admin.utils.jsonTemplates.JsonTemplatesUtils;
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

    @Bean
    public JsonTemplatesUtils jsonTemplatesUtils() {
        JsonTemplatesUtils jsonTemplatesUtils = new JsonTemplatesUtils("/jsonTemplates");
        return jsonTemplatesUtils;
    }
}
