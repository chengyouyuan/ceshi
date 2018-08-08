package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.MenuManager;
import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.support.MenuNode;
import com.winhxd.b2c.admin.module.system.constant.Constant;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserLoginDTO;
import com.winhxd.b2c.common.domain.system.user.enums.UserStatusEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import com.winhxd.b2c.common.util.JsonUtil;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author zhangzhengyang
 * @description 登录管理
 * @date 2018/8/2
 */

@Api(tags = "登录管理")
@RestController
@RequestMapping("/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final String MODULE_NAME = "登录管理";

    @Resource
    private Cache cache;
    @Resource
    private UserServiceClient userServiceClient;

    @ApiOperation("登录")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误"),
            @ApiResponse(code = BusinessCode.CODE_1006, message = "账号未启用"),
            @ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效")
    })
    @PostMapping(value = "/login")
    public ResponseResult<Boolean> login(@RequestBody SysUserLoginDTO userLoginDTO, HttpServletRequest request, HttpServletResponse response) {
        logger.info("{} - 用户登录, 参数：userLoginDTO={}", MODULE_NAME, userLoginDTO);

        userLoginDTO.setPassword(DigestUtils.md5DigestAsHex(userLoginDTO.getPassword().getBytes()));

        ResponseResult<Boolean> result = new ResponseResult<>(BusinessCode.CODE_OK);

        if(StringUtils.isBlank(userLoginDTO.getAccount()) || StringUtils.isBlank(userLoginDTO.getPassword())){
            logger.error("{} - 参数无效，账号：{}", MODULE_NAME, userLoginDTO);
            result = new ResponseResult<>(BusinessCode.CODE_1007);
            result.setData(false);
            return result;
        }

        ResponseResult<SysUser> responseResult = userServiceClient.getByAccount(userLoginDTO.getAccount());
        if(responseResult.getCode() != BusinessCode.CODE_OK){
            logger.error("{} - {}，账号：{}", MODULE_NAME, responseResult.getMessage(), userLoginDTO);
            result = new ResponseResult<>(responseResult.getCode());
            result.setData(false);
            return result;
        }

        SysUser sysUser = responseResult.getData();
        if(null == sysUser){
            logger.error("{} - 登录账号无效，账号：{}", MODULE_NAME, userLoginDTO);
            result = new ResponseResult<>(BusinessCode.CODE_1004);
            result.setData(false);
            return result;
        }


        if(!sysUser.getPassword().equals(userLoginDTO.getPassword())){
            logger.error("{} - 登录密码错误，账号：{}", MODULE_NAME, userLoginDTO);
            result = new ResponseResult<>(BusinessCode.CODE_1005);
            result.setData(false);
            return result;
        }

        if(sysUser.getStatus().equals(UserStatusEnum.DISABLED.getCode())){
            logger.error("{} - 账号未启用，账号：{}", MODULE_NAME, userLoginDTO);
            result = new ResponseResult<>(BusinessCode.CODE_1006);
            result.setData(false);
            return result;
        }

        //随机生成token
        String token = UUID.randomUUID().toString().replaceAll("-","");
        String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(sysUser,userInfo);
        userInfo.setToken(token);

        //将token和用户信息放入缓存，并设置token过期时间为30分钟
        cache.setex(cacheKey,30 * 60, JsonUtil.toJSONString(userInfo));

        //将token写到客户端的cookie里面
        Cookie tokenCookie = null;
        Cookie[] requestCookies = request.getCookies();
        if(null != requestCookies){
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals(Constant.TOKEN_NAME)){
                    tokenCookie = cookie;
                }
            }
        }
        if(null == tokenCookie){
            tokenCookie = new Cookie(Constant.TOKEN_NAME, token);
        } else {
            tokenCookie.setValue(token);
        }
        // 设置为30min
        tokenCookie.setMaxAge(30 * 60);
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        // 登录成功
        logger.info("{} - 用户登录成功, 账号={}", MODULE_NAME, userLoginDTO);
        result.setData(true);
        return result;
    }

    @ApiOperation("注销")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @GetMapping(value = "/logout")
    @CheckPermission(PermissionEnum.AUTHENTICATED)
    public ResponseResult<Boolean> login(HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = UserManager.getCurrentUser();
        logger.info("{} - 用户注销, 参数：userInfo={}", MODULE_NAME, userInfo);

        ResponseResult<Boolean> result = new ResponseResult<>(BusinessCode.CODE_OK);

        //获取客户端的cookie数据
        Cookie[] requestCookies = request.getCookies();
        if(null != requestCookies){
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals(Constant.TOKEN_NAME)){
                    String token = cookie.getValue();
                    String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;

                    //删除缓存里面的token和用户信息
                    cache.del(cacheKey);

                    //删除客户端的cookie里面的token
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                }
            }
        }
        // 注销成功
        logger.info("{} - 用户注销成功, 参数：userInfo={}", MODULE_NAME, userInfo);
        result.setData(true);
        return result;
    }

    @ApiOperation(value = "获取菜单")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping("/menu")
    @CheckPermission(PermissionEnum.AUTHENTICATED)
    public ResponseResult<List<MenuNode>> menu(){
        ResponseResult<List<MenuNode>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        List<MenuNode> menuNodeList = MenuManager.getMenuNodeList();
        UserInfo userInfo = UserManager.getCurrentUser();
        List<String> permissions = userInfo.getPermissions();
        List<PermissionEnum> permissionEnumList = new ArrayList<>(permissions.size());
        for(String permission : permissions){
            permissionEnumList.add(PermissionEnum.valueOf(permission));
        }

        if(CollectionUtils.isEmpty(menuNodeList) || CollectionUtils.isEmpty(permissions)){
            logger.info("{} - 获取菜单失败, 参数：userInfo={}", MODULE_NAME, userInfo);
            return new ResponseResult<>(BusinessCode.CODE_1003);
        }

        checkPermission(menuNodeList,permissionEnumList);

        List<MenuNode> newMenuNodeList = new ArrayList<>();
        getNewMenuNode(menuNodeList, newMenuNodeList);

        result.setData(newMenuNodeList);
        return result;
    }

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
    private void setHasAuthenticated(MenuNode menuNode){
        menuNode.setHasAuthenticated(true);
        if(null != menuNode.getParent() && !menuNode.getParent().isHasAuthenticated()){
            setHasAuthenticated(menuNode.getParent());
        }
    }
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
}
