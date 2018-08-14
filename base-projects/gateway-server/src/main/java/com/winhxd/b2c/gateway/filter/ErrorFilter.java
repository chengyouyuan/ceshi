package com.winhxd.b2c.gateway.filter;

import com.netflix.zuul.context.RequestContext;
import com.winhxd.b2c.common.constant.BusinessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ErrorFilter extends AbstractFilter {

    private static final Logger logger = LoggerFactory.getLogger(ErrorFilter.class);

    @Override
    public String filterType() {
        return "error";
    }

    @Override
    public int filterOrder() {
        return 10;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Throwable throwable = ctx.getThrowable();
        logger.error("网关未知异常:" + throwable.getMessage(), throwable);
        ctx.setThrowable(null);
        ctx.setResponseBody("aaaaaa");;
        return error(ctx, BusinessCode.CODE_1001);
    }

}
