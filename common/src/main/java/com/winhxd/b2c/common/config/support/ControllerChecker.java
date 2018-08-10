package com.winhxd.b2c.common.config.support;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lixiaodong
 */
public class ControllerChecker implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
       /* Pattern apiPath = Pattern.compile("^/api-[a-zA-Z]+/([a-zA-Z]+)(/security)?/(\\d+)/v\\d+/\\w+.*");
        Pattern apiClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.api\\.Api\\w+Controller$");
        Pattern servicePath = Pattern.compile("^/([a-zA-Z]+)/(\\d+)/v\\d+/\\w+.*");
        Pattern serviceClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.controller\\.\\w+Controller$");

        List<String> errorList = new ArrayList<>();
        Set<String> codes = new HashSet<>();

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        RequestMappingHandlerMapping rmhp = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            Method method = entry.getValue().getMethod();
            String className = method.getDeclaringClass().getCanonicalName();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            Matcher matcher;
            for (String url : patterns) {
                if ((matcher = apiPath.matcher(url)).matches()) {
                    if (!apiClass.matcher(className).matches()) {
                        errorList.add(className + "#" + method.getName());
                    } else {
                        String code = matcher.group(3);
                        if (codes.contains(code)) {
                            errorList.add(className + "#" + method.getName() + " 接口号重复:" + code);
                        } else {
                            codes.add(code);
                        }
                    }
                } else if ((matcher = servicePath.matcher(url)).matches()) {
                    if (!serviceClass.matcher(className).matches()) {
                        errorList.add(className + "#" + method.getName());
                    } else {
                        String code = matcher.group(2);
                        if (codes.contains(code)) {
                            errorList.add(className + "#" + method.getName() + " 接口号重复:" + code);
                        } else {
                            codes.add(code);
                        }
                    }
                } else {
                    if (className.startsWith("com.winhxd.b2c")) {
                        errorList.add(className + "#" + method.getName());
                    }
                }
            }
        }
        if (errorList.size() > 0) {
            throw new RuntimeException(
                    System.lineSeparator()
                            + StringUtils.repeat(System.lineSeparator(), 6)
                            + "================================Controller定义不规范================================"
                            + StringUtils.repeat(System.lineSeparator(), 2)
                            + String.join(System.lineSeparator(), errorList)
                            + StringUtils.repeat(System.lineSeparator(), 2)
                            + "================================Controller定义不规范================================"
                            + StringUtils.repeat(System.lineSeparator(), 6)
            );
        }*/
    }
}
