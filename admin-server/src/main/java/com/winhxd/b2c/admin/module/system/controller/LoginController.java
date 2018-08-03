package com.winhxd.b2c.admin.module.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.module.system.constant.Constant;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.enums.UserStatusEnum;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.UserServiceClient;
import io.swagger.annotations.*;
import org.apache.tomcat.util.security.MD5Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author zhangzhengyang
 * @description 登录管理
 * @date 2018/8/2
 */

@Api(value = "系统用户管理")
@RestController
@RequestMapping("/")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private static final String MODULE_NAME = "登录/注销管理";

    @Autowired
    private Cache cache;
    @Resource
    private UserServiceClient userServiceClient;

    @ApiOperation("登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userCode", value = "账号", required =true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "form")
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1004, message = "账号无效"),
            @ApiResponse(code = BusinessCode.CODE_1005, message = "密码错误"),
            @ApiResponse(code = BusinessCode.CODE_1006, message = "账号未启用")
    })
    @PostMapping(value = "/login")
    public ResponseResult<Boolean> login(String userCode, String password, HttpServletRequest request, HttpServletResponse response) {
        logger.info("{} - 用户登录, 参数：userCode={}", MODULE_NAME, userCode);

        ResponseResult<Boolean> result = new ResponseResult<>(false);

        SysUser sysUser = userServiceClient.getByUserCode(userCode).getData();
        if(null == sysUser){
            logger.error("登录账号无效，账号：{}", userCode);
            result.setCode(BusinessCode.CODE_1004);
        }

        String encodePassword = MD5Encoder.encode(password.getBytes());
        if(!sysUser.getPassword().equals(encodePassword)){
            logger.error("登录密码错误，账号：{}", userCode);
            result.setCode(BusinessCode.CODE_1005);
        }

        if(!sysUser.getStatus().equals(UserStatusEnum.DISABLED.getCode())){
            logger.error("账号未启用，账号：{}", userCode);
            result.setCode(BusinessCode.CODE_1006);
        }

        String token = UUID.randomUUID().toString().replaceAll("-","");
        String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;
        try {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(sysUser,userInfo);
            cache.setex(cacheKey,30 * 60, new ObjectMapper().writeValueAsString(userInfo));
        } catch (JsonProcessingException e) {
            logger.error("账号信息转json异常，账号信息：{}", sysUser);
            result.setCode(BusinessCode.CODE_1001);
        }

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
        logger.info("{} - 用户登录成功, 参数：userCode={}", MODULE_NAME, userCode);
        result.setData(true);
        return result;
    }

    @ApiOperation("注销")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
    })
    @GetMapping(value = "/logout")
    public ResponseResult<Boolean> login(HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = UserManager.getCurrentUser();
        logger.info("{} - 用户注销, 参数：userInfo={}", MODULE_NAME, userInfo);

        ResponseResult<Boolean> result = new ResponseResult<>(false);

        Cookie[] requestCookies = request.getCookies();
        if(null != requestCookies){
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals(Constant.TOKEN_NAME)){
                    String token = cookie.getValue();
                    String cacheKey = CacheName.CACHE_KEY_USER_TOKEN + token;

                    cache.del(cacheKey);
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

}
