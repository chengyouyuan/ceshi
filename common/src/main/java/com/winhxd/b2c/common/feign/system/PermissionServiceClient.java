package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.model.SysPermission;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author songkai
 * @date 2018/8/13 14:32
 * @description
 */
@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = PermissionServiceClientFallback.class)
public interface PermissionServiceClient {
    /**
     * 查询权限列表
     * @author songkai
     * @date 2018/8/13 14:32
     * @return
     */
    @RequestMapping(value = "/permission/3300/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<List<SysPermission>> list();
}


@Component
class PermissionServiceClientFallback implements PermissionServiceClient, FallbackFactory<PermissionServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceClientFallback.class);
    private Throwable throwable;

    public PermissionServiceClientFallback() {
    }

    private PermissionServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public PermissionServiceClient create(Throwable throwable) {
        return new PermissionServiceClientFallback(throwable);
    }

    @Override
    public ResponseResult<List<SysPermission>> list() {
        logger.error("PermissionServiceClientFallback -> list，错误信息为{}",throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
