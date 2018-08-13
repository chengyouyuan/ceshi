package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.message.condition.NeteaseMsgBoxCondition;
import com.winhxd.b2c.common.domain.message.vo.NeteaseMsgVO;
import com.winhxd.b2c.common.domain.order.vo.OrderInfoDetailVO;
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
import org.springframework.web.bind.annotation.*;

/**
 * @author jujinbiao
 * @className ApiNeteaseController
 * @description 网易云信接口
 */
@Api(value = "消息盒子", tags = "云信消息列表接口")
@RestController
@RequestMapping(value = "api-message/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiNeteaseController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApiNeteaseController.class);

	@Autowired
	private NeteaseService neteaseService;

	@ApiOperation(value = "获取云信用户消息接口", notes = "获取云信用户消息接口")
	@ApiResponses({
			@ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常")
	})
	@RequestMapping(value = "netease/7011/v1/findNeteaseMsgBox", method = RequestMethod.POST)
	public ResponseResult<PagedList<NeteaseMsgVO>> findNeteaseMsgBox(@RequestBody NeteaseMsgBoxCondition neteaseMsgBoxCondition) {
		StoreUser storeUser = UserContext.getCurrentStoreUser();
		if (storeUser == null || storeUser.getStoreCustomerId() == null) {
			LOGGER.error("ApiNeteaseController -> findNeteaseMsgBox当前用户登录的凭证无效 ");
			throw new BusinessException(BusinessCode.CODE_1002);
		}
		Long customerId = storeUser.getStoreCustomerId();
		ResponseResult<PagedList<NeteaseMsgVO>> result = new ResponseResult<>();
		try {
			PagedList<NeteaseMsgVO> list = neteaseService.getNeteaseMsgBox(neteaseMsgBoxCondition, customerId);
			result.setData(list);
		} catch (BusinessException e) {
			LOGGER.error("/api-message/netease/7011/v1/findNeteaseMsgBox,获取云信用户消息接口，异常信息{}" + e.getMessage(), e.getErrorCode());
			result.setCode(e.getErrorCode());
		} catch (Exception e) {
			LOGGER.error("/api-message/netease/7011/v1/findNeteaseMsgBox,获取云信用户消息接口，异常信息{}" + e.getMessage(), e);
			result.setCode(BusinessCode.CODE_1001);
		}
		return result;
	}

}
