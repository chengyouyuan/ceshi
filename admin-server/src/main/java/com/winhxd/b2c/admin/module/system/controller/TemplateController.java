package com.winhxd.b2c.admin.module.system.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.utils.jsonTemplates.JsonTemplatesUtils;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * JSON模板
 *
 * @author songkai
 * @description
 * @date 2018/8/7
 */

@Api(tags = "JSON模板")
@RestController
@RequestMapping("/template")
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateController.class);

    /** 模块名 */
    private static final String MODULE_NAME = "JSON模板";

    /** 菜单配置文件名 */
    private static final String MENU_CONFIG_NAME = "menu";

    @Resource
    private JsonTemplatesUtils jsonTemplatesUtils;

    @ApiOperation("获取菜单")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/menu")
    public ResponseResult<Map> getMenu() {
        ResponseResult<Map> result = new ResponseResult<>();
        UserInfo userInfo = UserManager.getCurrentUser();
        Map<String, Object> templateMap = jsonTemplatesUtils.getTemplatesByName(MENU_CONFIG_NAME);
        if (null == templateMap) {
            LOGGER.error("{} - 后台菜单模板获取失败, 模板有误", MODULE_NAME);
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }

        //TODO 根据权限过滤菜单

        templateMap.put("token", userInfo.getToken());
        templateMap.put("roles", userInfo.getRoleName());
        templateMap.put("name", userInfo.getUsername());
        result.setData(templateMap);
        return result;
    }

    @ApiOperation("获取模块配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "模板配置名称", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
            @ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
            @ApiResponse(code = BusinessCode.CODE_1003, message = "没有权限")
    })
    @GetMapping(value = "/{name}")
    public ResponseResult<Map> getTemplateByPath(@PathVariable("name") String name) {
        ResponseResult<Map> result = new ResponseResult<>();
        Map<String, Object> map = jsonTemplatesUtils.getTemplatesByName(name);
        if (null == map) {
            LOGGER.error("{} - 模板获取失败, 配置名: name = {}", new Object[]{MODULE_NAME, name});
            result.setCode(BusinessCode.CODE_1001);
            return result;
        }
        result.setData(map);
        return result;
    }

}
