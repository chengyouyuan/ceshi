package com.winhxd.b2c.gateway.filter;

import brave.Tracer;
import com.netflix.zuul.context.RequestContext;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * @author mahongliang
 * @date 2018年1月25日 下午6:37:24
 * @Description
 */
@Component
public class PostFilter extends AbstractFilter {
    public static final String CODE = "\"code\"";
    private static Logger logger = LoggerFactory.getLogger(PostFilter.class);

    @Autowired
    private Tracer tracer;

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        InputStream zin = context.getResponseDataStream();
        try {
            byte[] bs = IOUtils.toByteArray(zin);
            String json = new String(bs, StandardCharsets.UTF_8);
            tracer.currentSpan().tag(ContextHelper.TRACER_API_RESPONSE, json);
            if (json.indexOf(CODE) > 0) {
                ResponseResult result = JsonUtil.parseJSONObject(json, ResponseResult.class);
                tracer.currentSpan().tag(ContextHelper.TRACER_API_RESULT, String.valueOf(result.getCode()));
            }
            context.getResponse().getOutputStream().write(bs);
        } catch (IOException e) {
            logger.error("返回请求数据错误", e);
        }
        return null;
    }
}
