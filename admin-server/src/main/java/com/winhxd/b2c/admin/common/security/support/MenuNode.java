package com.winhxd.b2c.admin.common.security.support;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;

import java.util.List;

/**
 * @author lixiaodong
 */
public class MenuNode {

    private String id;
    private String menuName;
    private String path;
    private String configKey;
    private List<MenuNode> children;
    private PermissionEnum[] permissions;

    private String method;
    @JsonIgnore
    private MenuNode parent;
    @JsonIgnore
    private boolean hasAuthenticated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
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

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
