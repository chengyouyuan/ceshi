package com.winhxd.b2c.common.config.support;

import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lixiaodong
 */
public class ControllerChecker implements ApplicationListener<ContextRefreshedEvent> {
    private Pattern apiClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.api\\.Api\\w+Controller$");
    private Pattern serviceClass = Pattern.compile("^com\\.winhxd\\.b2c\\.\\w+(\\.\\w+)?\\.controller\\.\\w+Controller$");

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        List<String> errorList = new ArrayList<>();
        Set<String> codes = new HashSet<>();

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        RequestMappingHandlerMapping rmhp = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            Method method = entry.getValue().getMethod();

            Set<RequestMethod> requestMethods = entry.getKey().getMethodsCondition().getMethods();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            for (String path : patterns) {
                boolean checkMethod = checkPath(errorList, codes, path, method, requestMethods);
                if (checkMethod) {
                    checkMethodOutput(errorList, method);
                }
            }
        }
        if (errorList.size() > 0) {
            System.out.print(
                    System.lineSeparator()
                            + StringUtils.repeat(System.lineSeparator(), 4)
                            + "================================Controller定义不规范================================"
                            + StringUtils.repeat(System.lineSeparator(), 2)
                            + String.join(System.lineSeparator(), errorList)
                            + StringUtils.repeat(System.lineSeparator(), 2)
                            + "================================Controller定义不规范================================"
                            + StringUtils.repeat(System.lineSeparator(), 2)
            );
            System.exit(1);
        }
    }

    private void checkMethodOutput(List<String> errorList, Method method) {
        String methodName = method.getName(), className = method.getDeclaringClass().getCanonicalName();
        if (!className.startsWith("com.winhxd.b2c")) {
            return;
        }
        Type type = method.getGenericReturnType();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = ParameterizedType.class.cast(type);
            if (ResponseResult.class.isAssignableFrom((Class<?>) pType.getRawType())) {
                if (checkObjectType(pType.getActualTypeArguments()[0])) {
                    return;
                }
            }
        }
        errorList.add(className + "#" + methodName + " 返回值不符合要求");
    }

    private boolean checkObjectType(Type type) {
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            if (!checkObjectType(pType.getRawType())) {
                return false;
            }
            for (Type t : pType.getActualTypeArguments()) {
                if (!checkObjectType(t)) {
                    return false;
                }
            }
        } else if (type instanceof Class<?>) {
            Class<?> clazz = (Class<?>) type;
            return !clazz.equals(Object.class)
                    && !Map.class.isAssignableFrom(clazz)
                    && !ResponseResult.class.isAssignableFrom(clazz);
        }
        return true;
    }

    private boolean checkPath(List<String> errorList, Set<String> codes, String path, Method method, Set<RequestMethod> requestMethods) {
        boolean checkMethod = true;
        Matcher matcher;
        String methodName = method.getName(), className = method.getDeclaringClass().getCanonicalName();
        if ((matcher = ContextHelper.PATTERN_API_PATH.matcher(path)).matches()) {
            if (!apiClass.matcher(className).matches()) {
                errorList.add(className + "#" + methodName);
            } else {
                String pathTag = matcher.group(2);
                if (ContextHelper.PATH_TAG_COOPERATION.equals(pathTag)) {
                    checkMethod = false;
                }
                String code = matcher.group(3);
                if (codes.contains(code)) {
                    errorList.add(className + "#" + methodName + " 接口号重复:" + code);
                } else {
                    codes.add(code);
                }
                if (CollectionUtils.isEmpty(requestMethods) || !requestMethods.contains(RequestMethod.POST)) {
                    errorList.add(className + "#" + methodName + " 外部接口必须是POST");
                }
                if (!ContextHelper.PATH_TAG_COOPERATION.equals(pathTag)) {
                    Class<?>[] classes = method.getParameterTypes();
                    if (classes == null || classes.length == 0 || !(
                            ApiCondition.class.isAssignableFrom(classes[0])
                                    || MultipartRequest.class.isAssignableFrom(classes[0]))
                            ) {
                        errorList.add(className + "#" + methodName + " 外部接口入参必须是ApiCondition或其子类");
                    }
                }
            }
        } else if ((matcher = ContextHelper.PATTERN_SERVICE_PATH.matcher(path)).matches()) {
            if (!serviceClass.matcher(className).matches()) {
                errorList.add(className + "#" + methodName);
            } else {
                String code = matcher.group(2);
                if (codes.contains(code)) {
                    errorList.add(className + "#" + methodName + " 接口号重复:" + code);
                } else {
                    codes.add(code);
                }
            }
        } else {
            if (className.startsWith("com.winhxd.b2c")) {
                errorList.add(className + "#" + methodName);
            }
        }
        return checkMethod;
    }
}
