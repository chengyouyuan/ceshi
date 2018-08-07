package com.winhxd.b2c.common.config;

import com.winhxd.b2c.common.i18n.MessageHelper;
import org.springframework.context.annotation.Bean;

/**
 * 基础配置类
 *
 * @author lixiaodong
 */
public class CommonConfig {
    /**
     * i18n帮助类
     */
    @Bean
    public MessageHelper messageHelper() {
        return new MessageHelper();
    }
}
