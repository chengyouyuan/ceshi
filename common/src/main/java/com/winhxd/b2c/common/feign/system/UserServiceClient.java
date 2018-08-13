package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.user.dto.SysUserPasswordDTO;
import com.winhxd.b2c.common.domain.system.user.model.SysUser;
import feign.hystrix.FallbackFactory;
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
    @RequestMapping(value = "/user/3000/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Long> save(@RequestBody SysUser sysUser);

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/user/3001/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Void> modify(@RequestBody SysUser sysUser);

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/user/3002/v1/updatePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Void> updatePassword(@RequestBody SysUserPasswordDTO sysUser);

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @RequestMapping(value = "/user/3003/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<SysUser>> find(@RequestBody SysUserCondition condition);

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param account
     * @return
     */
    @RequestMapping(value = "/user/3004/v1/get/{account}", method = RequestMethod.GET)
    ResponseResult<SysUser> getByAccount(@PathVariable("account") String account);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @RequestMapping(value = "/user/3005/v1/get/{id}", method = RequestMethod.GET)
    ResponseResult<SysUser> get(@PathVariable("id") Long id);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     */
    @RequestMapping(value = "/user/3006/v1/disabled/{id}", method = RequestMethod.PUT)
    ResponseResult<Void> disabled(@PathVariable("id") Long id);

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
    public ResponseResult<Long> save(SysUser sysUser) {
        logger.error("UserServiceClientFallback -> save", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult modify(SysUser sysUser) {
        logger.error("UserServiceClientFallback -> modify", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult updatePassword(SysUserPasswordDTO sysUser) {
        logger.error("UserServiceClientFallback -> updatePassword", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<SysUser>> find(SysUserCondition condition) {
        logger.error("UserServiceClientFallback -> find", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysUser> getByAccount(String account) {
        logger.error("UserServiceClientFallback -> getByUserCode", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysUser> get(Long userId) {
        logger.error("UserServiceClientFallback -> get", throwable);
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