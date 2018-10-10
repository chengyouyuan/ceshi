package com.winhxd.b2c.admin.module.detection.controller;

import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.admin.common.security.annotation.MenuAssign;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import com.winhxd.b2c.common.domain.system.security.enums.MenuEnum;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.feign.detection.DetectionServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "监控服务管理", tags = "监控服务管理")
@RestController
@RequestMapping("detection/service")
@CheckPermission(PermissionEnum.SYSTEM_MANAGEMENT)
public class DetectionController {

    @Autowired
    private DetectionServiceClient detectionServiceClient;

    @PostMapping(value = "/findQuartzJobPageList")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "根据条件查询定时任务的分页数据信息", notes = "根据条件查询定时任务的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<QuartzJobVo>> findQuartzJobPageList(@RequestBody QuartzJobCondition quartzJobCondition) {
        return detectionServiceClient.findQuartzJobPageList(quartzJobCondition);
    }

    @PostMapping(value = "/addQuartzJob")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "根据条件查询定时任务的分页数据信息", notes = "根据条件查询定时任务的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult addQuartzJob(@RequestBody QuartzJobVo quartzJobVo) {
        return detectionServiceClient.addQuartzJob(quartzJobVo);
    }

    @GetMapping(value = "/deleteQuartzJob")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "根据条件查询定时任务的分页数据信息", notes = "根据条件查询定时任务的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult deleteQuartzJob(@RequestParam Long id) {
        return detectionServiceClient.deleteQuartzJob(id);
    }

    @GetMapping(value = "/findQuartzJobVoById")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "根据ID查询定时任务详细信息", notes = "根据ID查询定时任务详细信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务详情数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult findQuartzJobVoById(@RequestParam Long id) {
        return detectionServiceClient.findQuartzJobVoById(id);
    }

    @PostMapping(value = "/findQuartzJobResultPageList")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "查询定时任务执行结果的分页数据信息", notes = "查询定时任务执行结果的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务执行结果列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult findQuartzJobResultPageList(@RequestBody QuartzJobCondition quartzJobCondition) {
        return detectionServiceClient.findQuartzJobResultPageList(quartzJobCondition);
    }

    @PostMapping(value = "/findUserPageList")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_USER)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_USER)
    @ApiOperation(value = "查询用户列表数据信息", notes = "查询用户列表数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<DetectionUser>> findUserPageList() {
        return detectionServiceClient.findUserPageList();
    }

    @PostMapping(value = "/findDbSourcePageList")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_DBSOURCE)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_DBSOURCE)
    @ApiOperation(value = "查询数据源列表数据信息", notes = "查询数据源列表数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询定时任务列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult<PagedList<DbSource>> findDbSourcePageList() {
        return detectionServiceClient.findDbSourcePageList();
    }

    @PostMapping(value = "/addUser")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_USER)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_USER)
    @ApiOperation(value = "添加用户数据信息", notes = "添加用户数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,添加用户数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult addUser(@RequestBody DetectionUser user) {
        return detectionServiceClient.addUser(user);
    }

    @PostMapping(value = "/addDbSource")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_DBSOURCE)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_DBSOURCE)
    @ApiOperation(value = "添加数据源信息", notes = "添加数据源信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,添加数据源信息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult addUser(@RequestBody DbSource dbSource) {
        return detectionServiceClient.addDbSource(dbSource);
    }

    @GetMapping(value = "/resumeQuartzJob")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "重新启动定时任务", notes = "重新启动定时任务")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,重新启动定时任务失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult resumeQuartzJob(@RequestParam Long id) {
        return detectionServiceClient.resumeQuartzJob(id);
    }

    @GetMapping(value = "/pauseQuartzJob")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "暂停定时任务", notes = "暂停定时任务")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,暂停定时任务失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult pauseQuartzJob(@RequestParam Long id) {
        return detectionServiceClient.pauseQuartzJob(id);
    }

    @GetMapping(value = "/findErrorApiPageList")
    @CheckPermission(PermissionEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @MenuAssign(MenuEnum.DETECTION_MANAGEMENT_QUARTZ_JOB)
    @ApiOperation(value = "API接口监控查询出错的API", notes = "API接口监控查询出错的API")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,API接口监控查询出错的API失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    public ResponseResult findErrorApiPageList() {
        return detectionServiceClient.findErrorApiPageList();
    }

}