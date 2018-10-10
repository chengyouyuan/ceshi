package com.winhxd.b2c.common.feign.detection;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Auther: Louis
 * @Date: 2018/8/30 13:58
 * @Description:
 */
@FeignClient(value = "detection-service", fallbackFactory = detectionServiceClientFallBack.class)
public interface DetectionServiceClient {
    /**
     * @param quartzJobCondition 查询条件对象
     * @return 分页数据
     * @author Louis
     * @date 2018/8/30 14:59
     * @Description 根据条件查询定时任务列表页数据，分页查询
     */
    @RequestMapping(value = "/detection/service/v1/findQuartzJobPageList", method = RequestMethod.POST)
    ResponseResult<PagedList<QuartzJobVo>> findQuartzJobPageList(@RequestBody QuartzJobCondition quartzJobCondition);

    @RequestMapping(value = "/detection/service/v1/findQuartzJobVoById", method = RequestMethod.POST)
    ResponseResult<QuartzJobVo> findQuartzJobVoById(@RequestBody Long quartzJobId);

    @RequestMapping(value = "/detection/service/v1/addQuartzJob", method = RequestMethod.POST)
    ResponseResult addQuartzJob(@RequestBody QuartzJobVo quartzJobVo);

    @RequestMapping(value = "/detection/service/v1/deleteQuartzJob", method = RequestMethod.POST)
    ResponseResult deleteQuartzJob(@RequestBody Long quartzJobId);

    @RequestMapping(value = "/detection/service/v1/findQuartzJobResultPageList", method = RequestMethod.POST)
    ResponseResult findQuartzJobResultPageList(@RequestBody QuartzJobCondition quartzJobCondition);

    @RequestMapping(value = "/detection/service/v1/findUserPageList", method = RequestMethod.POST)
    ResponseResult<PagedList<DetectionUser>> findUserPageList();

    @RequestMapping(value = "/detection/service/v1/findDbSourcePageList", method = RequestMethod.POST)
    ResponseResult<PagedList<DbSource>> findDbSourcePageList();

    @RequestMapping(value = "/detection/service/v1/addUser", method = RequestMethod.POST)
    ResponseResult addUser(@RequestBody DetectionUser user);

    @RequestMapping(value = "/detection/service/v1/addDbSource", method = RequestMethod.POST)
    ResponseResult addDbSource(@RequestBody DbSource dbSource);

    @RequestMapping(value = "/detection/service/v1/resumeQuartzJob", method = RequestMethod.POST)
    ResponseResult resumeQuartzJob(@RequestBody Long quartzJobId);

    @RequestMapping(value = "/detection/service/v1/pauseQuartzJob", method = RequestMethod.POST)
    ResponseResult pauseQuartzJob(@RequestBody Long quartzJobId);

    @RequestMapping(value = "/detection/service/v1/findErrorApiPageList", method = RequestMethod.POST)
    ResponseResult findErrorApiPageList();
}

@Component
class detectionServiceClientFallBack implements DetectionServiceClient, FallbackFactory<DetectionServiceClient> {

    Logger logger = LoggerFactory.getLogger(detectionServiceClientFallBack.class);
    Throwable throwable;

    @Override
    public ResponseResult findQuartzJobResultPageList(QuartzJobCondition quartzJobCondition) {
        logger.error("detectionServiceClientFallBack -> findQuartzJobResultPageList错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<DbSource>> findDbSourcePageList() {
        logger.error("detectionServiceClientFallBack -> findDbSourcePageList错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult deleteQuartzJob(Long quartzJobId) {
        logger.error("detectionServiceClientFallBack -> deleteQuartzJob错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult addQuartzJob(QuartzJobVo quartzJobVo) {
        logger.error("detectionServiceClientFallBack -> addQuartzJob错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public DetectionServiceClient create(Throwable throwable) {
        this.throwable = throwable;
        return new detectionServiceClientFallBack();
    }

    @Override
    public ResponseResult<PagedList<DetectionUser>> findUserPageList() {
        logger.error("detectionServiceClientFallBack -> findUserPageList错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<PagedList<QuartzJobVo>> findQuartzJobPageList(QuartzJobCondition quartzJobCondition) {
        logger.error("detectionServiceClientFallBack -> findQuartzJobList错误信息{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult<QuartzJobVo> findQuartzJobVoById(Long quartzJobId) {
        logger.error("detectionServiceClientFallBack -> findQuartzJobVoById{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult addUser(DetectionUser user) {
        logger.error("detectionServiceClientFallBack -> addUser{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult addDbSource(DbSource dbSource) {
        logger.error("detectionServiceClientFallBack -> addDbSource{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult resumeQuartzJob(Long quartzJobId) {
        logger.error("detectionServiceClientFallBack -> resumeQuartzJob{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult pauseQuartzJob(Long quartzJobId) {
        logger.error("detectionServiceClientFallBack -> pauseQuartzJob{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }

    @Override
    public ResponseResult findErrorApiPageList() {
        logger.error("detectionServiceClientFallBack -> findErrorApiPageList{}", throwable.getMessage());
        return new ResponseResult<>(BusinessCode.CODE_1001);
    }
}
