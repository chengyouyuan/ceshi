package com.winhxd.b2c.common.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * @author lixiaodong
 */
public class MessageHelper {
    private static MessageHelper instance;

    @Autowired
    private MessageSource messageSource;

    public static MessageHelper getInstance() {
        return instance;
    }

    public MessageHelper() {
        instance = this;
    }

    public String getMessage(String code, Object... args) {
        return getMessageWithLocale(null, code, args);
    }

    public String getMessageWithLocale(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, String defaultMessage, Object... args) {
        return getMessage(null, code, defaultMessage, args);
    }

    public String getMessage(Locale locale, String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
