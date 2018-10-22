package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.MiniFormIdCondition;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.message.service.MiniProgramService;
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
 * @className ApiMiniProgramController
 * @description 消息服务-小程序接口
 */
@Api(value = "消息服务-小程序接口", tags = "消息服务-小程序")
@RestController
@RequestMapping(value = "api-message/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiMiniProgramController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApiMiniProgramController.class);

	@Autowired
	MiniProgramService miniProgramService;

	@ApiOperation(value = "保存用户formid接口", notes = "保存用户formid接口")
	@ApiResponses({
			@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1002, message = "登录凭证无效"),
			@ApiResponse(code = BusinessCode.CODE_702401, message = "参数错误，formids为空")
	})
	@RequestMapping(value = "mini/7024/v1/saveFormIds", method = RequestMethod.POST)
	public ResponseResult<Void> saveFormIds(@RequestBody MiniFormIdCondition condition) {
		LOGGER.info("api-message/mini/7024/v1/saveFormIds,保存用户formid,condition={}",JsonUtil.toJSONString(condition));
		ResponseResult<Void> result = new ResponseResult<>();
		try {
			CustomerUser user = UserContext.getCurrentCustomerUser();
			miniProgramService.saveFormIds(condition,user);
		}catch (BusinessException be) {
			LOGGER.error("api-message/mini/7024/v1/saveFormIds,保存用户formid出错，异常信息为={}", be);
			result.setCode(be.getErrorCode());
		}catch (Exception e) {
			LOGGER.error("api-message/mini/7024/v1/saveFormIds,保存用户formid出错，异常信息为={}", e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

}
