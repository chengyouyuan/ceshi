package com.winhxd.b2c.common.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author lixiaodong
 */
@Component
public class MessageHelper {
    @Autowired
    private MessageSource messageSource;

    public String getMessage(String code, Object... args) {
        return getMessageWithLocale(null, code, args);
    }


    public String getMessageWithLocale(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }
}
