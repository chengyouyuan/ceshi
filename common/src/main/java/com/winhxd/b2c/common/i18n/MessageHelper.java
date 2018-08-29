package com.winhxd.b2c.common.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

/**
 * @author lixiaodong
 */
public class MessageHelper {
    private static MessageHelper instance;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    public static MessageHelper getInstance() {
        return instance;
    }

    public MessageHelper() {
        instance = this;
    }

    private Locale getLocale() {
        Locale locale = null;
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null && requestAttributes instanceof ServletRequestAttributes) {
            locale = localeResolver.resolveLocale(((ServletRequestAttributes) requestAttributes).getRequest());
        }
        if (locale == null) {
            locale = Locale.CHINESE;
        }
        return locale;
    }

    public String getMessage(String code, Object... args) {
        return getMessage(getLocale(), code, args);
    }

    public String getMessage(Locale locale, String code, Object... args) {
        return messageSource.getMessage(code, args, locale);
    }

    public String getMessage(String code, String defaultMessage, Object... args) {
        return getMessage(getLocale(), code, defaultMessage, args);
    }

    public String getMessage(Locale locale, String code, String defaultMessage, Object... args) {
        return messageSource.getMessage(code, args, defaultMessage, locale);
    }
}
