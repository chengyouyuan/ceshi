package com.winhxd.b2c.admin.common.security.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;

import java.util.List;

/**
 * @author lixiaodong
 */
public class MenuNode {
    private String path;
    private String method;
    private String name;
    private List<MenuNode> children;
    @JsonIgnore
    private MenuNode parent;
    private PermissionEnum[] permissions;
    private String menu;

    @JsonIgnore
    private boolean hasAuthenticated;

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

    public PermissionEnum[] getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionEnum[] permissions) {
        this.permissions = permissions;
    }

    public String getMenu() {
        return menu;
    }

    public void setMenu(String menu) {
        this.menu = menu;
    }

    public MenuNode getParent() {
        return parent;
    }

    public void setParent(MenuNode parent) {
        this.parent = parent;
    }

    public boolean isHasAuthenticated() {
        return hasAuthenticated;
    }

    public void setHasAuthenticated(boolean hasAuthenticated) {
        this.hasAuthenticated = hasAuthenticated;
    }

}
