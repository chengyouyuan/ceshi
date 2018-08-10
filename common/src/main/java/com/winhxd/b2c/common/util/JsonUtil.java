package com.winhxd.b2c.common.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class JsonUtil {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        TimeZone china = TimeZone.getTimeZone("GMT+08:00");
        mapper.setTimeZone(china);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * @param value
     * @return
     * @author 魏冰
     * @date 2017年4月15日 下午8:12:42
     * @Description 任意Java对象转换成json字符串
     * @version 1.0
     */
    public static String toJSONString(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content
     * @param clazz
     * @return
     * @author 魏冰
     * @date 2017年4月15日 下午8:13:06
     * @Description 解析json对象字符串
     * @version 1.0
     */
    public static <T> T parseJSONObject(String content, Class<T> clazz) {
        try {
            return mapper.readValue(content, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content
     * @param typeReference 自定义map泛型
     * @return
     * @author 魏冰
     * @date 2017年4月15日 下午8:34:07
     * @Description 解析json对象字符串
     * @version 1.0
     */
    public static <T> T parseJSONObject(String content, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(content, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content
     * @return
     * @author 魏冰
     * @date 2017年4月15日 下午8:34:07
     * @Description 解析json对象字符串，返回固定类型Map
     * @version 1.0
     */
    public static Map<String, Object> parseJSONObject(String content) {
        try {
            return mapper.readValue(content, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param content
     * @param entityClass
     * @return
     * @author 魏冰
     * @date 2017年4月15日 下午8:13:29
     * @Description 解析json数组字符串
     * @version 1.0
     */
    public static <T> List<T> parseJSONArray(String content, Class<T> entityClass) {
        JavaType javaType = getCollectionType(List.class, entityClass);
        try {
            return mapper.readValue(content, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static JavaType getCollectionType(Class<?> collectionClass, Class<?> entityClass) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, new Class[]{entityClass});
    }
}
