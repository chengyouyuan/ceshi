package com.winhxd.b2c.common.config.support;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class ControllerChecker implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        Pattern apiPath = Pattern.compile("^/api-[a-zA-Z]+/([a-zA-Z]+)(/security)?/(\\d+)/v\\d+/\\w+.*");
        Pattern apiClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.api\\.Api\\w+Controller$");
        Pattern servicePath = Pattern.compile("^/([a-zA-Z]+)/(\\d+)/v\\d+/\\w+.*");
        Pattern serviceClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.controller\\.\\w+Controller$");

        List<String> errorList = new ArrayList<>();

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        RequestMappingHandlerMapping rmhp = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            Method method = entry.getValue().getMethod();
            String className = method.getDeclaringClass().getCanonicalName();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            for (String url : patterns) {
                if (apiPath.matcher(url).matches()) {
                    if (!apiClass.matcher(className).matches()) {
                        errorList.add(className + "#" + method.getName());
                    }
                } else if (servicePath.matcher(url).matches()) {
                    if (!serviceClass.matcher(className).matches()) {
                        errorList.add(className + "#" + method.getName());
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
                            + "================================Controller定义不规范================================"
                            + System.lineSeparator()
                            + String.join(System.lineSeparator(), errorList)
                            + System.lineSeparator()
                            + "================================Controller定义不规范================================"
            );
        }
    }
}
