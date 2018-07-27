package com.winhxd.b2c.webserver.common.security.support;

import com.winhxd.b2c.webserver.common.security.Permission;

import java.util.List;

/**
 * @author lixiaodong
 */
public class MenuNode {
    private String path;
    private String method;
    private String name;
    private List<MenuNode> children;
    private Permission[] permissions;
    private String menu;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MenuNode> getChildren() {
        return children;
    }

    public void setChildren(List<MenuNode> children) {
        this.children = children;
    }

    public Permission[] getPermissions() {
        return permissions;
    }

    public void setPermissions(Permission[] permissions) {
        this.permissions = permissions;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }
}
