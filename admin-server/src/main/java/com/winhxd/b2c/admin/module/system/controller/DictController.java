package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.dict.condition.SysDictCondition;
import com.winhxd.b2c.common.domain.system.dict.model.SysDict;
import com.winhxd.b2c.common.domain.system.dict.model.SysDictItem;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.system.DictServiceClient;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description 系统字典管理控制层
 * @author zhangzhengyang
 * @date 2018/9/27
 */
@Api(tags = "系统字典管理")
@RestController
public class DictController {

    private static final Logger logger = LoggerFactory.getLogger(DictController.class);

    private static final String MODULE_NAME = "系统字典管理";

    @Resource
    private DictServiceClient dictServiceClient;

    @ApiOperation("新增字典")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_303001, message = "字典编码已被使用")
    })
    @PostMapping(value = "/dict/add")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT_ADD})
    public ResponseResult add(@RequestBody SysDict sysDict) {
        logger.info("{} - 新增字典, 参数：sysDict={}", MODULE_NAME, sysDict);

        List<SysDictItem> items = dictServiceClient.findByDictCode(sysDict.getCode()).getData();
        if(!CollectionUtils.isEmpty(items)){
            logger.warn("{} - 字典编码已被使用, 参数：sysDict={}", MODULE_NAME, sysDict);
            return new ResponseResult(BusinessCode.CODE_303001);
        }

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        sysDict.setCreated(date);
        sysDict.setCreatedBy(userInfo.getId());
        sysDict.setCreatedByName(userInfo.getUsername());
        sysDict.setUpdated(date);
        sysDict.setUpdatedBy(userInfo.getId());
        sysDict.setUpdatedByName(userInfo.getUsername());

        return dictServiceClient.save(sysDict);
    }

    @ApiOperation("编辑字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典编号", required = true)
    })
    @PostMapping(value = "/dict/edit")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT_EDIT})
    public ResponseResult edit(@RequestBody SysDict sysDict) {
        logger.info("{} - 编辑字典, 参数：sysDict={}", MODULE_NAME, sysDict);

        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();

        sysDict.setUpdated(date);
        sysDict.setUpdatedBy(userInfo.getId());
        sysDict.setUpdatedByName(userInfo.getUsername());

        return dictServiceClient.modify(sysDict);
    }

    @ApiOperation(value = "查询字典列表")
    @PostMapping(value = "/dict/list")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT})
    public ResponseResult<PagedList<SysDict>> list(@RequestBody SysDictCondition condition){
        logger.info("{} - 查询字典列表, 参数：condition={}", MODULE_NAME, condition);
       return dictServiceClient.find(condition);
    }

    @ApiOperation(value = "根据主键获取字典信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典编号", required = true)
    })
    @GetMapping("/dict/get/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT})
    public ResponseResult<SysDict> getById(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取字典信息, 参数：id={}", MODULE_NAME, id);
        return dictServiceClient.get(id);
    }

    @ApiOperation(value = "根据主键删除字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "字典编号", required = true)
    })
    @GetMapping("/dict/remove/{id}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT_DELETE})
    public ResponseResult<Void> remove(@PathVariable("id") Long id){
        logger.info("{} - 根据主键禁用字典, 参数：id={}", MODULE_NAME, id);
        return dictServiceClient.remove(id);
    }

    @ApiOperation(value = "根据字典编码获取字典信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "字典编码", required = true)
    })
    @GetMapping(value = "/dict/findByDictCode/{code}")
    @CheckPermission({PermissionEnum.SYSTEM_MANAGEMENT_DICT})
    public ResponseResult<List<SysDictItem>> findByDictCode(@PathVariable("code") String code){
        logger.info("{} - 根据字典编码获取字典信息, 参数：code={}", MODULE_NAME, code);
        return dictServiceClient.findByDictCode(code);
    }
}
