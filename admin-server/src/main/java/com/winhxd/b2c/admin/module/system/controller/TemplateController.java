package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.MenuManager;
import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.support.MenuNode;
import com.winhxd.b2c.admin.utils.jsonTemplates.JsonTemplatesUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.HandlerMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JSON模板
 *
 * @author songkai
 * @description
 * @date 2018/8/7
 */

@Api(tags = "JSON模板")
@RestController
@RequestMapping("/template")
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    /** 模块名 */
    private static final String MODULE_NAME = "JSON模板";

    /** 菜单配置文件 */
    private static final String MENU_CONFIG_PATH = "/menu.json";

    @Resource
    private JsonTemplatesUtils jsonTemplatesUtils;


    /**
     * 根据用户权限获取菜单
     * @author zhangzhengyang
     * @date 2018/9/3
     * @param
     * @return com.winhxd.b2c.common.domain.ResponseResult<java.util.Map>
     */
    @ApiOperation(value = "获取菜单(样本功能)")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/menu")
    @CheckPermission(PermissionEnum.AUTHENTICATED)
    public ResponseResult<Map> menu(){
        ResponseResult<Map> result = new ResponseResult<>();

        //获取完整树结构菜单
        List<MenuNode> menuNodeList = MenuManager.getMenuNodeList();

        //获取当前用户
        UserInfo userInfo = UserManager.getCurrentUser();

        //拿到用户的权限字符串列表
        List<String> permissions = userInfo.getPermissions();

        //根据权限字符串列表组装权限枚举列表
        List<PermissionEnum> permissionEnumList = new ArrayList<>(permissions.size());
        for(String permission : permissions){
            permissionEnumList.add(PermissionEnum.valueOf(permission));
        }
        if(CollectionUtils.isEmpty(menuNodeList) || CollectionUtils.isEmpty(permissions)){
            LOGGER.info("{} - 获取菜单失败, 参数：userInfo={}", MODULE_NAME, userInfo);
            return new ResponseResult<>(BusinessCode.CODE_1003);
        }

        //检查有权限的菜单并做标记
        checkPermission(menuNodeList,permissionEnumList);

        //根据标记构建有权限的菜单树结构
        List<MenuNode> newMenuNodeList = new ArrayList<>();
        getNewMenuNode(menuNodeList, newMenuNodeList);

        Map<String, Object> templateMap = new HashMap<>(5);
        templateMap.put("token", userInfo.getToken());
        templateMap.put("userId", userInfo.getId());
        templateMap.put("roles", userInfo.getRoleName() == null ? "" : userInfo.getRoleName());
        templateMap.put("name", userInfo.getUsername());
        templateMap.put("menus", newMenuNodeList);
        result.setData(templateMap);
        return result;
    }

    /**
     * 检查该用户是否有权限显示改菜单，并做标记
     * @author zhangzhengyang
     * @date 2018/9/3
     * @param menuNodeList
     * @param permissionEnumList
     * @return void
     */
    private void checkPermission(List<MenuNode> menuNodeList, List<PermissionEnum> permissionEnumList){
        for(MenuNode menuNode : menuNodeList){
            if(permissionEnumList.containsAll(Arrays.asList(menuNode.getPermissions()))){
                setHasAuthenticated(menuNode);
            }
            if(!CollectionUtils.isEmpty(menuNode.getChildren())){
                checkPermission(menuNode.getChildren(), permissionEnumList);
            }
        }

    }

    /**
     * 标记有权限的菜单树节点
     * @author zhangzhengyang
     * @date 2018/9/3
     * @param menuNode
     * @return void
     */
    private void setHasAuthenticated(MenuNode menuNode){
        menuNode.setHasAuthenticated(true);
        if(null != menuNode.getParent() && !menuNode.getParent().isHasAuthenticated()){
            setHasAuthenticated(menuNode.getParent());
        }
    }

    /**
     * 根据已标记有权限的菜单树结构构建页面菜单数据
     * @author zhangzhengyang
     * @date 2018/9/3
     * @param menuNodeList
     * @param newMenuNodeList
     * @return void
     */
    private void getNewMenuNode(List<MenuNode> menuNodeList, List<MenuNode> newMenuNodeList){
        for(MenuNode menuNode : menuNodeList){
            MenuNode newMenuNode = new MenuNode();
            if(menuNode.isHasAuthenticated()){
                BeanUtils.copyProperties(menuNode,newMenuNode);
                newMenuNodeList.add(newMenuNode);
            }
            if(!CollectionUtils.isEmpty(menuNode.getChildren())){
                newMenuNode.setChildren(new ArrayList<>());
                getNewMenuNode(menuNode.getChildren(),newMenuNode.getChildren());
            }

        }

    }


//    @ApiOperation("获取菜单")
//    @ApiResponses({
//            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
//            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
//            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
//            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
//    })
//    @GetMapping(value = "/menu")
//    @CheckPermission({PermissionEnum.AUTHENTICATED})
//    public ResponseResult<Map> getMenu() {
//        ResponseResult<Map> result = new ResponseResult<>();
//        UserInfo userInfo = UserManager.getCurrentUser();
//        Map<String, Object> templateMap = jsonTemplatesUtils.getTemplatesByPath(MENU_CONFIG_PATH);
//        if (null == templateMap) {
//            LOGGER.error("{} - 后台菜单模板获取失败, 模板有误", MODULE_NAME);
//            result.setCode(BusinessCode.CODE_1001);
//            return result;
//        }
//
//        //TODO 根据权限过滤菜单
//
//        templateMap.put("token", userInfo.getToken());
//        templateMap.put("userId", userInfo.getId());
//        templateMap.put("roles", userInfo.getRoleName() == null ? "" : userInfo.getRoleName());
//        templateMap.put("name", userInfo.getUsername());
//        result.setData(templateMap);
//        return result;
//    }

    @ApiOperation("获取模块配置")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/module/**")
    @CheckPermission({PermissionEnum.AUTHENTICATED})
    public ResponseResult<Map> getTemplateByPath(HttpServletRequest request) {
        ResponseResult<Map> result = new ResponseResult<>();
        String path = extractPathFromPattern(request);
        Map<String, Object> map = jsonTemplatesUtils.getTemplatesByPath(path);
        if (null == map) {
            LOGGER.error("{} - 模板获取失败, 配置文件路径: path = {}", new Object[]{MODULE_NAME, path});
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }
        result.setData(map);
        return result;
    }

    /**
     * 把指定URL后的字符串全部截断当成参数
     * 为了防止URL中包含中文或者特殊字符（/等）时，匹配不了的问题
     *
     * @param request
     * @return
     */
    private static String extractPathFromPattern(final HttpServletRequest request) {
        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        String bestMatchPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        return new AntPathMatcher().extractPathWithinPattern(bestMatchPattern, path);
    }

}
