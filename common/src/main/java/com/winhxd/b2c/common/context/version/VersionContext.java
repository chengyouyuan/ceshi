package com.winhxd.b2c.common.context.version;

/**
 * 微服务版本控制
 *
 * @author lixiaodong
 */
public class VersionContext {
    public static final String HEADER_NAME = "ms-ver";

    private static ThreadLocal<String> version = new ThreadLocal<>();

    public static String getVersion() {
        return version.get();
    }

    public static void setVersion(String ver) {
        version.set(ver);
    }

    public static void clean() {
        version.remove();
    }
}
