package com.winhxd.b2c.admin.utils.jsonTemplates;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

/**
 * JSON模板工具类
 *
 * @author songkai
 * @date 2018/8/7 16:06
 * @description
 */
public class JsonTemplatesUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonTemplatesUtils.class);

    /** 模板根路径 */
    private String rootTemplatesPath;

    public JsonTemplatesUtils(String rootTemplatesPath) {
        this.rootTemplatesPath = rootTemplatesPath;
    }


    /**
     * 获取JSON模板文件
     *
     * @param jsonTemplatesPath
     * @return
     */
    public Map<String, Object> getTemplatesByPath(String jsonTemplatesPath) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> dataNode = null;

        if (StringUtils.isNotBlank(jsonTemplatesPath)) {
            if (jsonTemplatesPath.indexOf("/") != 0) {
                jsonTemplatesPath = "/" + jsonTemplatesPath;
            }

            URL uri = this.getClass().getResource(rootTemplatesPath + jsonTemplatesPath);
            File file = new File(uri.getPath());

            try {
                dataNode = mapper.readValue(file, Map.class);
            } catch (IOException e) {
                LOGGER.error("{} - JSON模板文件读取错误: {}", jsonTemplatesPath, e);
            }
        }
        return  dataNode;
    }
}
