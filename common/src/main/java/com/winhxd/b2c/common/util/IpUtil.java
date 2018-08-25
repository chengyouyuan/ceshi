package com.winhxd.b2c.common.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取客户端Ip地址
 */
public class IpUtil {
    private static final String[] IP_HEADER_CANDIDATES = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR"};
    public static final String IP_UNKNOWN = "unknown";

    public static String getIpAddr() {
        String ipAddress = StringUtils.EMPTY;
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (ServletRequestAttributes.class.isInstance(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            for (String header : IP_HEADER_CANDIDATES) {
                String ip = request.getHeader(header);
                if (StringUtils.isNotBlank(ip) && !IP_UNKNOWN.equalsIgnoreCase(ip)) {
                    ipAddress = ip;
                    break;
                }
            }
            if (StringUtils.isBlank(ipAddress) || IP_UNKNOWN.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
            if (ipAddress != null && ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }
}
