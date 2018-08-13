package com.winhxd.b2c.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.util.JsonUtil;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author lixiaodong
 */
public abstract class AbstractFilter extends ZuulFilter {
    private static final String ERROR_KEY = "error";

    @Override
    public boolean shouldFilter() {
        return !RequestContext.getCurrentContext().getBoolean(ERROR_KEY);
    }

    public Object error(RequestContext context, int code) {
        ResponseResult<?> responseResult = new ResponseResult<>(code);
        context.set(ERROR_KEY, true);
        context.setSendZuulResponse(false);
        context.setResponseStatusCode(200);
        ServletOutputStream out = null;
        try {
            out = context.getResponse().getOutputStream();
            out.write(JsonUtil.toJSONString(responseResult).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
