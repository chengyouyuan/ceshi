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


}
