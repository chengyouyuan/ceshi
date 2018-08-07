package com.winhxd.b2c.admin.common.context;

import com.winhxd.b2c.admin.common.security.support.MenuNode;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * 获取菜单集合
 * @author zhangzhengyang
 * @date 2018/8/7
 */
public class MenuManager {

    private static final Logger logger = LoggerFactory.getLogger(MenuManager.class);

    /**
     * 菜单集合
     * 此处用String是为了clone菜单进行计算
     **/
    private static String menuNodeListString = null;

    public MenuManager(List<MenuNode> menuNodeList){
        menuNodeListString = JsonUtil.toJSONString(menuNodeList);
    }

    public static List<MenuNode> getMenuNodeList(){
        return JsonUtil.parseJSONArray(menuNodeListString,MenuNode.class);
    }
}
