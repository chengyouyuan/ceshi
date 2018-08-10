package com.winhxd.b2c.system.user.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.condition.SysRoleCondition;
import com.winhxd.b2c.common.domain.system.user.model.SysRole;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.system.RoleServiceClient;
import com.winhxd.b2c.system.user.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author zhangzhengyang
 * @description 系统权限组控制
 * @date 2018/8/7
 */
@Api(tags = "系统权限组管理")
@RestController
@RequestMapping("/")
public class SysRoleController implements RoleServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(SysRoleController.class);

    private static final String MODULE_NAME = "系统权限组管理";

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 新增权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return
     */
    @Override
    @ApiOperation(value = "新增权限组", response = Long.class)
    public ResponseResult<Long> save(@RequestBody SysRole sysRole){
        logger.info("{} - 新增权限组, 参数：sysRole={}", MODULE_NAME, sysRole);
        ResponseResult<Long> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            sysRoleService.save(sysRole);
            result.setData(sysRole.getId());
        } catch (BusinessException e){
            logger.error("{} - 新增权限组失败, 参数：sysRole={}", MODULE_NAME, sysRole, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 新增权限组失败, 参数：sysRole={}", MODULE_NAME, sysRole, e);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 修改权限组
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param sysRole
     * @return
     */
    @Override
    @ApiOperation(value = "修改权限组")
    public ResponseResult<Integer> modify(@RequestBody SysRole sysRole){
        logger.info("{} - 修改权限组, 参数：sysRole={}", MODULE_NAME, sysRole);
        ResponseResult<Integer> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            result.setData(sysRoleService.modify(sysRole));
        } catch (BusinessException e){
            logger.error("{} - 修改权限组失败, 参数：sysRole={}", MODULE_NAME, sysRole, e);
            result = new ResponseResult<>(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 修改权限组失败, 参数：sysRole={}", MODULE_NAME, sysRole, e);
            result = new ResponseResult<>(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 查询权限组列表
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param condition
     * @return
     */
    @Override
    @ApiOperation(value = "查询权限组列表")
    public ResponseResult<PagedList<SysRole>> find(@RequestBody SysRoleCondition condition){
        logger.info("{} - 查询权限组列表, 参数：condition={}", MODULE_NAME, condition);
        ResponseResult<PagedList<SysRole>> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            PagedList<SysRole> page = sysRoleService.find(condition);
            result.setData(page);
        } catch (BusinessException e){
            logger.error("{} - 查询权限组列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 查询权限组列表失败, 参数：condition={}", MODULE_NAME, condition, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 根据主键获取权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键获取权限组信息")
    public ResponseResult<SysRole> get(@PathVariable("id") Long id){
        logger.info("{} - 根据主键获取权限组信息, 参数：id={}", MODULE_NAME, id);
        ResponseResult<SysRole> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            SysRole sysRole = sysRoleService.get(id);
            result.setData(sysRole);
            return result;
        } catch (BusinessException e){
            logger.error("{} - 根据主键获取权限组信息失败, 参数：id={}", MODULE_NAME, id, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 根据主键获取权限组信息失败, 参数：id={}", MODULE_NAME, id, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

    /**
     * 根据主键删除权限组信息
     * @author zhangzhengyang
     * @date 2018/8/7
     * @param id
     * @return
     */
    @Override
    @ApiOperation(value = "根据主键删除权限组信息")
    public ResponseResult<Integer> remove(Long id) {
        logger.info("{} - 根据主键删除权限组信息, 参数：id={}", MODULE_NAME, id);
        ResponseResult<Integer> result = new ResponseResult<>(BusinessCode.CODE_OK);
        try {
            result.setData(sysRoleService.remove(id));
            return result;
        } catch (BusinessException e){
            logger.error("{} - 根据主键删除权限组信息失败, 参数：id={}", MODULE_NAME, id, e);
            result = new ResponseResult(e.getErrorCode());
        } catch (Exception e){
            logger.error("{} - 根据主键删除权限组信息失败, 参数：id={}", MODULE_NAME, id, e);
            result = new ResponseResult(BusinessCode.CODE_1001);
        }
        return result;
    }

}
