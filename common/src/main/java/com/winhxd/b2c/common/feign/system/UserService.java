package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.order.vo.OrderVO;
import com.winhxd.b2c.common.domain.system.condition.SysUserCondition;
import com.winhxd.b2c.common.domain.system.model.SysUser;
import feign.hystrix.FallbackFactory;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = UserServiceFallback.class)
public interface UserService {

    /**
     * 新增用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api/user/3010/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Long> add(@RequestBody SysUser sysUser);

    /**
     * 修改用户
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api/user/3011/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult update(@RequestBody SysUser sysUser);

    /**
     * 修改密码
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysUser
     * @return
     */
    @RequestMapping(value = "/api/user/3012/v1/updatePassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult updatePassword(@RequestBody SysUser sysUser);

    /**
     * 查询用户列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @RequestMapping(value = "/api/user/3013/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<SysUser>> list(@RequestBody SysUserCondition condition);

    /**
     * 根据登录账号获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param userCode
     * @return
     */
    @ApiOperation(value = "根据登录账号获取用户信息")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_301401, message = "该用户不存在")
    })
    @RequestMapping(value = "/api/user/3014/v1/get/{userCode}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<SysUser> getByUserCode(@PathVariable("userCode") String userCode);

    /**
     * 根据主键获取用户信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param userId
     * @return
     */
    @ApiOperation(value = "根据主键获取用户信息")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_500, message = "服务器内部异常")
    })
    @RequestMapping(value = "/api/user/3015/v1/get/{userId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<SysUser> getById(Long userId);


    @RequestMapping(value = "/order/v1/getOrderVo/", method = RequestMethod.GET)
    ResponseResult<OrderVO> getOrderVo(@RequestParam("orderNo") String orderNo);
}

class UserServiceFallback implements UserService, FallbackFactory<UserService> {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceFallback.class);
    private Throwable throwable;

    public UserServiceFallback() {
    }

    private UserServiceFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<Long> add(SysUser sysUser) {
        return null;
    }

    @Override
    public ResponseResult update(SysUser sysUser) {
        return null;
    }

    @Override
    public ResponseResult updatePassword(SysUser sysUser) {
        return null;
    }

    @Override
    public ResponseResult<Page<SysUser>> list(SysUserCondition condition) {
        return null;
    }

    @Override
    public ResponseResult<SysUser> getByUserCode(String userCode) {
        return null;
    }

    @Override
    public ResponseResult<SysUser> getById(Long userId) {
        return null;
    }

    @Override
    public ResponseResult<OrderVO> getOrderVo(String orderNo) {
        logger.error("OrderServiceFallback -> getOrderVo", throwable);
        return new ResponseResult<>(BusinessCode.CODE_401);
    }

    @Override
    public UserService create(Throwable throwable) {
        return new UserServiceFallback(throwable);
    }
}