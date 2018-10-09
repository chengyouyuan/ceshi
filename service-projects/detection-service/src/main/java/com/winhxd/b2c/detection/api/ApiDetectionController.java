package com.winhxd.b2c.detection.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import com.winhxd.b2c.common.util.JsonUtil;
import com.winhxd.b2c.detection.service.IQuartzJobService;
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
 * @author Louis
 * @date 2018年8月31日 上午9:44:11
 * @Description 监控服务Controller
 * @version
 */
@Api(value = "detectionService Controller", tags = "S-Service")
@RestController
@RequestMapping(value = "/api-detection-service/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class ApiDetectionController {
	private static final Logger logger = LoggerFactory.getLogger(ApiDetectionController.class);

	@Autowired
	private IQuartzJobService quartzJobService;
	/**
	 * @author Louis
	 * @date 2018年8月31日 下午1:31:45
	 * @Description 通过账号验证码登录
	 * @param quartzJobCondition
	 * @return
	 */
	@ApiOperation(value = "监控服务查询定时任务列表接口")
	@ApiResponses({ @ApiResponse(code = BusinessCode.CODE_OK, message = "成功"),
			@ApiResponse(code = BusinessCode.CODE_1001, message = "服务器内部异常"),
			@ApiResponse(code = BusinessCode.CODE_1007, message = "参数无效"),
			@ApiResponse(code = BusinessCode.CODE_202115, message = "网络请求超时"),
			@ApiResponse(code = BusinessCode.CODE_202109, message = "您的账号存在异常行为，已被锁定，如有疑问请联系客服4006870066。") })
	@RequestMapping(value = "findQuartzJobPageList", method = RequestMethod.POST)
	public ResponseResult<PagedList<QuartzJobVo>> findQuartzJobPageList(
			@RequestBody QuartzJobCondition quartzJobCondition) {
		logger.info("{} -定时任务查询, 参数：quartzJobExample={}", "", JsonUtil.toJSONString(quartzJobCondition));
		ResponseResult<PagedList<QuartzJobVo>> result = new ResponseResult<>();
		if (null == quartzJobCondition) {
			quartzJobCondition = new QuartzJobCondition();
		}
		PagedList<QuartzJobVo> page = quartzJobService.findQuartzJobPageList(quartzJobCondition);
        result.setData(page);
		return result;
	}

}
