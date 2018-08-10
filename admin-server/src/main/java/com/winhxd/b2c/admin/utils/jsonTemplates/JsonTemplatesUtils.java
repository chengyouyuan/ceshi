package com.winhxd.b2c.admin.utils.jsonTemplates;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.CacheName;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JSON模板工具类
 *
 * @author songkai
 * @date 2018/8/7 16:06
 * @description
 */
public class JsonTemplatesUtils {

    @Resource
    private Cache cache;

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonTemplatesUtils.class);

    /** 模板配置文件MAP */
    private static ConcurrentHashMap<String, String> templateMap = new ConcurrentHashMap<>(0);

    /** 模板根路径 */
    private String rootTemplatesPath;

    public JsonTemplatesUtils(String rootTemplatesPath) {
        this.rootTemplatesPath = rootTemplatesPath;
    }

    public void addTemplate(String key, String value) {
        templateMap.put(key, value);
    }


    /**
     * 获取JSON模板文件
     * <p>模板列表在 /resources/jsonTemplates/templates.properties 统一配置</p>
     * @param jsonTemplatesName
     * @return
     */
    public Map<String, Object> getTemplatesByName(String jsonTemplatesName) {
        if (templateMap.size() <= 0) {
            // 加载配置文件
            Properties properties = new Properties();
            try {
                properties.load(this.getClass().getResourceAsStream(rootTemplatesPath + "/templates.properties"));
            } catch (IOException e) {
                LOGGER.error("加载JSON模板配置出错");
                e.printStackTrace();
                return null;
            }

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                LOGGER.info("加载JSON模板配置文件 - name: {} path: {}", new Object[] {entry.getKey().toString(), entry.getValue().toString()});
                cache.del(CacheName.JSON_TEMPLATES + entry.getKey().toString());
                addTemplate(entry.getKey().toString(), entry.getValue().toString());
            }
        }

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> dataNode = null;

        String jsonTemplatesPath = templateMap.get(jsonTemplatesName);

        if (StringUtils.isNotBlank(jsonTemplatesPath)) {

            // 先取缓存, 没有再去读文件
            String json = cache.get(CacheName.JSON_TEMPLATES + jsonTemplatesName);
            if (StringUtils.isNotBlank(json)) {
                try {
                    dataNode = mapper.readValue(json, Map.class);
                } catch (IOException e) {
                    LOGGER.error("解析缓存JSON模板文件错误: {}", jsonTemplatesPath);
                    e.printStackTrace();
                }
            } else {
                URL uri = this.getClass().getResource(rootTemplatesPath + jsonTemplatesPath);
                File file = new File(uri.getPath());

                try {
                    dataNode = mapper.readValue(file, Map.class);
                    cache.set(CacheName.JSON_TEMPLATES + jsonTemplatesName, mapper.writeValueAsString(dataNode));
                } catch (IOException e) {
                    LOGGER.error("JSON模板文件读取错误: {}", jsonTemplatesPath);
                    e.printStackTrace();
                }

            }
        }

        return  dataNode;
    }
}
