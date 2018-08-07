package com.winhxd.b2c.common.context.support;

import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class ContextHelper {
    private static final String UTF_8 = "UTF-8";

    public static <T> T getHeaderObject(HttpServletRequest request, String headerName, Class<T> clazz) {
        String header = request.getHeader(headerName);
        if (StringUtils.isBlank(header)) {
            return null;
        }
        try {
            String json = URLDecoder.decode(header, UTF_8);
            return JsonUtil.parseJSONObject(json, clazz);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static String getHeaderJsonString(Object obj) {
        String json = JsonUtil.toJSONString(obj);
        return encode(json);
    }

    public static String encode(String json) {
        try {
            return URLEncoder.encode(json, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}
