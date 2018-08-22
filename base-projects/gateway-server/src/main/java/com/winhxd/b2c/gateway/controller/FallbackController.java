package com.winhxd.b2c.gateway.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.i18n.LocaleContextResolver;

import java.util.Locale;

@RestController
public class FallbackController {
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private LocaleContextResolver resolver;

    @RequestMapping("/fallback")
    public ResponseResult fallback(ServerWebExchange exchange) {
        Locale locale = resolver.resolveLocaleContext(exchange).getLocale();
        ResponseResult<Object> result = new ResponseResult<>();
        result.setCode(BusinessCode.CODE_1001);
        result.setMessage(messageSource.getMessage(String.valueOf(BusinessCode.CODE_1001), null, locale));
        return result;
    }
}
