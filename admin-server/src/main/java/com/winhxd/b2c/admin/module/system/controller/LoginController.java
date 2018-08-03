package com.winhxd.b2c.admin.module.system.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import com.winhxd.b2c.common.feign.system.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    private Cache cache;
    @Resource
    private UserService userService;

    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult<Boolean> login(@RequestParam("userCode") String userCode, @RequestParam("password") String password, HttpServletRequest request, HttpServletResponse response) {
        ResponseResult<Boolean> result = new ResponseResult<>(false);

//        SysUser sysUser = userService.getByUserCode(userCode).getData();
//        if(null == sysUser){
//            logger.error("登录账号无效，账号：{}", userCode);
//            result.setCode(BusinessCode.CODE_10002);
//        }
//
//        String encodePassword = MD5Encoder.encode(password.getBytes());
//        if(!sysUser.getPassword().equals(encodePassword)){
//            logger.error("登录密码错误，账号：{}", userCode);
//            result.setCode(BusinessCode.CODE_10003);
//        }
//
//        if(!sysUser.getStatus().equals(UserStatusEnum.DISABLED.getCode())){
//            logger.error("账号未启用，账号：{}", userCode);
//            result.setCode(BusinessCode.CODE_10004);
//        }

        SysUser sysUser = new SysUser();
        sysUser.setUserCode("zhangzhengyang");
        sysUser.setUserName("张争洋");

        String uuid = UUID.randomUUID().toString();
        try {
            cache.setex(Cache.CACHE_KEY_USER_TOKEN+uuid,30 * 60, new ObjectMapper().writeValueAsString(sysUser));
        } catch (JsonProcessingException e) {
            logger.error("账号信息转json异常，账号信息：{}", sysUser);
            result.setCode(BusinessCode.CODE_10000);
        }

        Cookie tokenCookie = null;
        Cookie[] requestCookies = request.getCookies();
        if(null != requestCookies){
            for(Cookie cookie : requestCookies){
                if(cookie.getName().equals("token")){
                    tokenCookie = cookie;
                }
            }
        }
        if(null == tokenCookie){
            tokenCookie = new Cookie("token", uuid);
        } else {
            tokenCookie.setValue(uuid);
        }
        tokenCookie.setMaxAge(30 * 60);// 设置为30min
        tokenCookie.setPath("/");
        response.addCookie(tokenCookie);

        // 登录成功
        result.setData(true);
        return result;
    }

}
