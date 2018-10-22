package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgReadStatusCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.message.service.NeteaseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jujinbiao
 * @className ApiNeteaseController
 * @description 网易云信接口
 */
@Api(value = "云信消息列表接口", tags = "消息盒子")
@RestController
@RequestMapping(value = "api-message/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiNeteaseController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiNeteaseController.class);

	@Autowired
	private NeteaseService neteaseService;

	@ApiOperation(value = "获取云信用户消息接口", notes = "获取云信用户消息接口")
	@ApiResponses({
			@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_701101, message = "云信账户异常")
	})
	@RequestMapping(value = "netease/7011/v1/findNeteaseMsgBox", method = RequestMethod.POST)
	public ResponseResult<PagedList<NeteaseMsgVO>> findNeteaseMsgBox(@RequestBody NeteaseMsgBoxCondition condition) {
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (condition == null || condition.getTimeType() == null) {
			LOGGER.error("ApiNeteaseController -> findNeteaseMsgBox请求参数无效 ");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		Long storeId = storeUser.getBusinessId();
		ResponseResult<PagedList<NeteaseMsgVO>> result = new ResponseResult<>();
		PagedList<NeteaseMsgVO> list = neteaseService.findNeteaseMsgBox(condition, storeId);
		result.setData(list);
		return result;
	}

	@ApiOperation(value = "设置云信消息已读状态", notes = "设置云信消息已读状态")
	@ApiResponses({
			@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_701101, message = "云信账户异常")
	})
	@RequestMapping(value = "netease/7010/v1/modifyNeteaseMsgReadStatus", method = RequestMethod.POST)
	public ResponseResult<Boolean> modifyNeteaseMsgReadStatus(@RequestBody NeteaseMsgReadStatusCondition condition) {
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		/**
		 * 全部已读时必须要传 区分是当天还是历史的时间类型 timeType
		 */
		boolean paramsInvalid = condition == null ||
								condition.getAllRead() == null ||
								(condition.getAllRead() == 1 && condition.getTimeType() == null);
		if (paramsInvalid) {
			LOGGER.error("ApiNeteaseController -> modifyNeteaseMsgReadStatus请求参数无效 ");
			throw new BusinessException(BusinessCode.CODE_1007);
		}
		Long storeId = storeUser.getBusinessId();
		ResponseResult<Boolean> result = new ResponseResult<>();
		boolean modifySuccess = neteaseService.modifyNeteaseMsgReadStatus(condition, storeId);
		result.setData(modifySuccess);
		return result;
	}
}
