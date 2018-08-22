package com.winhxd.b2c.admin.module.message.controller;

import com.winhxd.b2c.admin.common.context.UserManager;
import com.winhxd.b2c.admin.common.security.annotation.CheckPermission;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MessageBatchPushCondition;
import com.winhxd.b2c.common.domain.message.dto.MessageBatchPushDTO;
import com.winhxd.b2c.common.domain.message.model.MessageBatchPush;
import com.winhxd.b2c.common.domain.message.vo.MessageBatchPushVO;
import com.winhxd.b2c.common.domain.system.security.enums.PermissionEnum;
import com.winhxd.b2c.common.domain.system.user.vo.UserInfo;
import com.winhxd.b2c.common.feign.message.MessageServiceClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;

/**
 * @author jujinbiao
 * @Description: 消息管理-手动推送消息控制器
 * @date 2018/8/17 10:42
 */
@Api(value = "后台消息管理-手动推送消息控制器", tags = "后台消息管理-手动推送消息接口")
@RestController
@RequestMapping(value = "message")
public class MessageBatchPushController {

    private Logger logger = LoggerFactory.getLogger(MessageBatchPushController.class);

    private static final String MODULE_NAME = "消息管理";

    @Autowired
    private MessageServiceClient messageServiceClient;

    @ApiOperation(value = "根据条件查询手动推送消息的分页数据信息", notes = "根据条件查询手动推送消息的分页数据信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,查询手动推送消息列表数据失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @PostMapping(value = "/findMessageBatchPushPageInfo")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT_LIST)
    public ResponseResult<PagedList<MessageBatchPushVO>> findMessageBatchPushPageInfo(@RequestBody MessageBatchPushCondition condition) {
        ResponseResult<PagedList<MessageBatchPushVO>> responseResult = messageServiceClient.findMessageBatchPushPageInfo(condition);
        return responseResult;
    }

    @ApiOperation(value = "新增手动推送消息", notes = "新增手动推送消息")
    @ApiResponses({
            @ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,新增手动推送消息失败"),
            @ApiResponse(code = BusinessCode.CODE_703502, message = "后台消息管理 新增手动给门店推送消息失败 云信门店记录不存在"),
            @ApiResponse(code = BusinessCode.CODE_703504, message = "后台消息管理 新增手动给门店推送消息失败 消息内容为空"),
            @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @PostMapping(value = "/addBatchPush")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT)
    public ResponseResult addBatchPush(@RequestBody MessageBatchPushDTO messageBatchPushDTO) {
        logger.info("{} - 新增手动推送消息, 参数：messageBatchPushDTO={}", MODULE_NAME, messageBatchPushDTO);
        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();
        MessageBatchPush messageBatchPush = new MessageBatchPush();
        BeanUtils.copyProperties(messageBatchPushDTO,messageBatchPush);
        messageBatchPush.setCreated(date);
        messageBatchPush.setCreatedBy(userInfo.getId());
        messageBatchPush.setCreatedByName(userInfo.getUsername());
        messageBatchPush.setUpdated(date);
        messageBatchPush.setUpdatedBy(userInfo.getId());
        messageBatchPush.setUpdatedByName(userInfo.getUsername());
        return messageServiceClient.addBatchPush(messageBatchPush);
    }

    @ApiOperation(value = "获取手动推送消息", notes = "获取手动推送消息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,获取手动推送消息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/getBatchPush/{id}")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT)
    public ResponseResult<MessageBatchPush> getBatchPush(@PathVariable("id") Long id) {
        return messageServiceClient.getBatchPush(id);
    }

    @ApiOperation(value = "修改手动推送消息", notes = "修改手动推送消息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,新增手动推送消息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @PostMapping(value = "/modifyBatchPush")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT)
    public ResponseResult modifyBatchPush(@RequestBody MessageBatchPushDTO messageBatchPushDTO) {
        logger.info("{} - 修改手动推送消息, 参数：messageBatchPushDTO={}", MODULE_NAME, messageBatchPushDTO);
        UserInfo userInfo = UserManager.getCurrentUser();
        Date date = Calendar.getInstance().getTime();
        MessageBatchPush messageBatchPush = new MessageBatchPush();
        BeanUtils.copyProperties(messageBatchPushDTO,messageBatchPush);
        messageBatchPush.setUpdated(date);
        messageBatchPush.setUpdatedBy(userInfo.getId());
        messageBatchPush.setUpdatedByName(userInfo.getUsername());
        return messageServiceClient.modifyBatchPush(messageBatchPush);
    }

    @ApiOperation(value = "删除手动推送消息", notes = "删除手动推送消息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,新增手动推送消息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/removeBatchPush/{id}")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT)
    public ResponseResult removeBatchPush(@PathVariable("id") Long id) {
        logger.info("{} - 删除手动推送消息, 参数：id={}", MODULE_NAME, id);
        return messageServiceClient.removeBatchPush(id);
    }

    @ApiOperation(value = "手动推送消息", notes = "手动推送消息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部错误,手动推送消息失败"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @GetMapping(value = "/batchPushMessage/{id}")
    @CheckPermission(PermissionEnum.MESSAGE_MANAGEMENT)
    public ResponseResult batchPushMessage(@PathVariable("id") Long id) {
        logger.info("{} - 手动推送消息, 参数：id={}", MODULE_NAME, id);
        return messageServiceClient.batchPushMessage(id);
    }

}
