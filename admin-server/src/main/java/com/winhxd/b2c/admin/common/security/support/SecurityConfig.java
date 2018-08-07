package com.winhxd.b2c.admin.common.security.support;

import com.winhxd.b2c.admin.common.context.MenuManager;
import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @author lixiaodong
 */
@Component
public class SecurityConfig implements ApplicationListener<ContextRefreshedEvent> {
    private Map<Method, List<PermissionEnum>> pathPermissions;
    private List<MenuNode> menuNodeList;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        initMenuNodes();
        pathPermissions = new HashMap<>();

        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        RequestMappingHandlerMapping rmhp = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = rmhp.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            Method method = entry.getValue().getMethod();
            Set<String> patterns = entry.getKey().getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = entry.getKey().getMethodsCondition().getMethods();

            //初始化菜单数据
            if (!CollectionUtils.isEmpty(patterns)) {
                MenuAssign menuAssign = method.getAnnotation(MenuAssign.class);
                if (menuAssign != null) {
                    for (MenuEnum menu : menuAssign.value()) {
                        MenuNode menuNode = findMenuNode(menuNodeList, menu.toString());
                        RequestMethod rm;
                        if (methods.iterator().hasNext()) {
                            rm = methods.iterator().next();
                        } else {
                            rm = RequestMethod.GET;
                        }
                        menuNode.setMethod(rm.toString());
                        menuNode.setPath(patterns.iterator().next());
                    }
                    new MenuManager(menuNodeList);
                }
            }


            //初始化权限数据
            CheckPermission p1 = method.getDeclaredAnnotation(CheckPermission.class);
            CheckPermission p2 = method.getDeclaringClass().getDeclaredAnnotation(CheckPermission.class);
            if (p1 != null || p2 != null) {
                List<PermissionEnum> permissions = new ArrayList<>();
                if (p1 != null) {
                    permissions.addAll(Arrays.asList(p1.value()));
                }
                if (p2 != null) {
                    permissions.addAll(Arrays.asList(p2.value()));
                }
                pathPermissions.put(method, permissions);
            }
        }
    }


    private void initMenuNodes() {
        menuNodeList = new ArrayList<>();
        for (MenuEnum menu : MenuEnum.values()) {
            MenuNode node = buildMenuNode(menu);
            if (menu.getParent() == null) {
                menuNodeList.add(node);
            } else {
                MenuNode parent = findMenuNode(menuNodeList, menu.getParent().toString());
                node.setParent(parent);
                parent.getChildren().add(node);
            }
        }
    }

    private MenuNode buildMenuNode(MenuEnum menu) {
        MenuNode tree = new MenuNode();
        tree.setName(menu.getName());
        tree.setChildren(new ArrayList<>(0));
        tree.setMenu(menu.toString());
        tree.setPermissions(menu.getPermissions());
        return tree;
    }

    private MenuNode findMenuNode(List<MenuNode> list, String menu) {
        for (MenuNode tree : list) {
            if (tree.getMenu().equals(menu)) {
                return tree;
            }
            MenuNode menu1 = findMenuNode(tree.getChildren(), menu);
            if (menu1 != null) {
                return menu1;
            }
        }
        return null;
    }

    /**
     * 检查是否有权限
     *
     * @param method
     * @param permissionSet
     * @return
     */
    public boolean hasPermission(Method method, Set<PermissionEnum> permissionSet) {
        List<PermissionEnum> requiredPermissions = pathPermissions.get(method);
        if (requiredPermissions == null) {
            return true;
        }
        if (CollectionUtils.isEmpty(permissionSet)) {
            return false;
        }
        return permissionSet.containsAll(requiredPermissions);
    }
}
