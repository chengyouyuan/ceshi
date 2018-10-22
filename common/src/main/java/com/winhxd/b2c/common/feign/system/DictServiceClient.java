package com.winhxd.b2c.common.feign.system;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.ServiceName;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
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

import java.util.List;


@FeignClient(value = ServiceName.SYSTEM_SERVICE, fallbackFactory = DictServiceClientFallback.class)
public interface DictServiceClient {

    /**
     * 新增字典
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "/dict/3030/v1/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Long> saveSysDict(@RequestBody SysDict sysDict);

    /**
     * 修改字典
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param sysDict
     * @return
     */
    @RequestMapping(value = "/dict/3031/v1/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<Void> modifySysDict(@RequestBody SysDict sysDict);
    
    /**
     * 查询字典列表
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param condition
     * @return
     */
    @RequestMapping(value = "/dict/3033/v1/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    ResponseResult<PagedList<SysDict>> findSysDictPagedList(@RequestBody SysDictCondition condition);

    /**
     * 根据登录账号获取字典信息
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param dictCode
     * @return
     */
    @RequestMapping(value = "/dict/3034/v1/get/{dictCode}", method = RequestMethod.GET)
    ResponseResult<List<SysDictItem>> findSysDictItemByDictCode(@PathVariable("dictCode") String dictCode);

    /**
     * 根据主键获取字典信息
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param id
     * @return
     */
    @RequestMapping(value = "/dict/3035/v1/get/{id}", method = RequestMethod.GET)
    ResponseResult<SysDict> getSysDictById(@PathVariable("id") Long id);

    /**
     * 根据主键删除字典
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param id
     * @return
     */
    @RequestMapping(value = "/dict/3036/v1/remove/{id}", method = RequestMethod.PUT)
    ResponseResult<Void> removeSysDictById(@PathVariable("id") Long id);

}

@Component
class DictServiceClientFallback implements DictServiceClient, FallbackFactory<DictServiceClient> {
    private static final Logger logger = LoggerFactory.getLogger(DictServiceClientFallback.class);
    private Throwable throwable;

    public DictServiceClientFallback() {
    }

    private DictServiceClientFallback(Throwable throwable) {
        this.throwable = throwable;
    }

    @Override
    public ResponseResult<Long> saveSysDict(SysDict sysDict) {
        logger.error("DictServiceClientFallback -> save", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult modifySysDict(SysDict sysDict) {
        logger.error("DictServiceClientFallback -> modify", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<SysDict>> findSysDictPagedList(SysDictCondition condition) {
        logger.error("DictServiceClientFallback -> find", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<List<SysDictItem>> findSysDictItemByDictCode(@PathVariable("dictCode") String dictCode) {
        logger.error("DictServiceClientFallback -> findByDictCode", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<SysDict> getSysDictById(Long dictId) {
        logger.error("DictServiceClientFallback -> get", throwable);
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult removeSysDictById(Long id) {
        logger.error("DictServiceClientFallback -> remove", throwable);
        return new ResponseResult(BusinessCode.CODE_1001);
    }

    @Override
    public DictServiceClient create(Throwable throwable) {
        return new DictServiceClientFallback(throwable);
    }
}