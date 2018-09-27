package com.winhxd.b2c.system.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.common.feign.system.DictServiceClient;
import com.winhxd.b2c.system.service.SysDictService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author zhangzhengyang
 * @description 系统字典管理控制
 * @date 2018/9/27
 */
@Api(tags = "系统字典管理")
@RestController
@RequestMapping("/")
public class SysDictController implements DictServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysDictController.class);

    private static final String MODULE_NAME = "系统字典管理";

    @Autowired
    private SysDictService sysDictService;


    /**
     * 新增字典
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysDict
     * @return
     */
    @Override
    @ApiOperation(value = "新增字典")
    public ResponseResult<Long> save(@RequestBody SysDict sysDict){
        logger.info("{} - 新增字典, 参数：sysDict={}", MODULE_NAME, sysDict);
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysDictService.save(sysDict);
        result.setData(sysDict.getId());
        return result;
    }

    /**
     * 修改字典
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param sysDict
     * @return
     */
    @Override
    @ApiOperation(value = "修改字典")
    public ResponseResult<Void> modify(@RequestBody SysDict sysDict){
        logger.info("{} - 修改字典, 参数：sysDict={}", MODULE_NAME, sysDict);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        sysDictService.modify(sysDict);
        return result;
    }

    /**
     * 查询字典列表
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param condition
     * @return
     */
    @Override
    @ApiOperation(value = "查询字典列表")
    public ResponseResult<PagedList<SysDict>> find(@RequestBody SysDictCondition condition){
        logger.info("{} - 查询字典列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<PagedList<SysDict>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        PagedList<SysDict> page = sysDictService.find(condition);
        result.setData(page);
        return result;
    }

    /**
     * 根据登录账号获取字典信息
     * @author zhangzhengyang
     * @date 2018/9/27
     * @param dictCode
     * @return
     */
    @Override
    @ApiOperation(value = "根据登录账号获取字典信息")
    public ResponseResult<List<SysDictItem>> findByDictCode(@PathVariable("dictCode") String dictCode){
        logger.info("{} - 根据登录账号获取字典信息, 参数：dictCode={}", MODULE_NAME, dictCode);
        ResponseResult<List<SysDictItem>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        List<SysDictItem> sysDictItems = sysDictService.findByDictCode(dictCode);
        result.setData(sysDictItems);
        return result;
    }

    /**
     * 根据主键获取字典信息
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键获取字典信息")
    public ResponseResult<SysDict> get(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取字典信息, 参数：id={}", MODULE_NAME, id);
        ResponseResult<SysDict> result = new ResponseResult<>(BusinessCode.CODE_OK);
        SysDict sysDict = sysDictService.get(id);
        result.setData(sysDict);
        return result;
    }

    /**
     * 根据主键删除字典
     * @author zhangzhengyang
     * @date 2018/8/2
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键删除字典")
    public ResponseResult<Void> remove(@PathVariable("id") Long id){
        logger.info("{} - 根据主键删除字典, 参数：id={}", MODULE_NAME, id);
        ResponseResult result = new ResponseResult<>(BusinessCode.CODE_OK);
        result.setData(sysDictService.remove(id));
        return result;
    }
}
