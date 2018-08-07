package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import feign.hystrix.FallbackFactory;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = UserServiceClientFallback.class)
public interface UserServiceClient {

    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api-system/user/300/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Long> add(@RequestBody SysUser sysUser);

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api-system/user/301/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult update(@RequestBody SysUser sysUser);

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api-system/user/302/v1/updatePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult updatePassword(@RequestBody SysUserPasswordDTO sysUser);

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api-system/user/303/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<SysUser>> list(@RequestBody SysUserCondition condition);

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param account
     * @return
     */
    @RequestMapping(value = "/api-system/user/304/v1/get/{account}", method = RequestMethod.GET)
    ResponseResult<SysUser> getByAccount(@PathVariable("account") String account);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @RequestMapping(value = "/api-system/user/305/v1/get/{id}", method = RequestMethod.GET)
    ResponseResult<SysUser> getById(@PathVariable("id") Long id);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     */
    @RequestMapping(value = "/api-system/user/306/v1/disabled/{id}", method = RequestMethod.PUT)
    ResponseResult disabled(@PathVariable("id") Long id);

}

@Component
class UserServiceClientFallback implements UserServiceClient, FallbackFactory<UserServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceClientFallback.class);
    private Throwable throwable;

    public UserServiceClientFallback() {
    }

    private UserServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<Long> add(SysUser sysUser) {
        logger.error("UserServiceClientFallback -> add", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult update(SysUser sysUser) {
        logger.error("UserServiceClientFallback -> update", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updatePassword(SysUserPasswordDTO sysUser) {
        logger.error("UserServiceClientFallback -> updatePassword", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<SysUser>> list(SysUserCondition condition) {
        logger.error("UserServiceClientFallback -> list", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysUser> getByAccount(String account) {
        logger.error("UserServiceClientFallback -> getByUserCode", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysUser> getById(Long userId) {
        logger.error("UserServiceClientFallback -> getById", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult disabled(Long id) {
        logger.error("UserServiceClientFallback -> disabled", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public UserServiceClient create(Throwable throwable) {
        return new UserServiceClientFallback(throwable);
    }
}