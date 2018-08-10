package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
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

@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = RoleServiceClientFallback.class)
public interface RoleServiceClient {

    /**
     * 新增权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/role/310/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Long> save(@RequestBody SysRole sysRole);

    /**
     * 修改权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return
     */
    @RequestMapping(value = "/role/311/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Integer> modify(@RequestBody SysRole sysRole);

    /**
     * 查询权限组列表
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param condition
     * @return
     */
    @RequestMapping(value = "/role/312/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<SysRole>> find(@RequestBody SysRoleCondition condition);


    /**
     * 根据主键获取权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return
     */
    @RequestMapping(value = "/role/313/v1/get/{id}", method = RequestMethod.GET)
    ResponseResult<SysRole> get(@PathVariable("id") Long id);

    /**
     * 根据主键获取权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return
     */
    @RequestMapping(value = "/role/314/v1/remove/{id}", method = RequestMethod.DELETE)
    ResponseResult<Integer> remove(@PathVariable("id") Long id);

}

@Component
class RoleServiceClientFallback implements RoleServiceClient, FallbackFactory<RoleServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(RoleServiceClientFallback.class);
    private Throwable throwable;

    public RoleServiceClientFallback() {
    }

    private RoleServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<Long> save(SysRole sysRole) {
        logger.error("RoleServiceClientFallback -> save", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> modify(SysRole sysRole) {
        logger.error("RoleServiceClientFallback -> modify", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<SysRole>> find(SysRoleCondition condition) {
        logger.error("RoleServiceClientFallback -> find", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysRole> get(Long roleId) {
        logger.error("RoleServiceClientFallback -> get", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<Integer> remove(Long id) {
        logger.error("RoleServiceClientFallback -> remove", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public RoleServiceClient create(Throwable throwable) {
        return new RoleServiceClientFallback(throwable);
    }
}