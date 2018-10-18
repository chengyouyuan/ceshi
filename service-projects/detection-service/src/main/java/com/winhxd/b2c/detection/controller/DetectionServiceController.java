package com.winhxd.b2c.detection.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.enums.JobStatusEnum;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import com.winhxd.b2c.common.feign.detection.DetectionServiceClient;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.common.domain.detection.model.QuartzJobResult;
import com.winhxd.b2c.detection.quartz.QuartzHandlerService;
import com.winhxd.b2c.detection.service.IQuartzJobService;
import com.winhxd.b2c.detection.util.Sequence;
import com.winhxd.b2c.detection.zipkin.ZipkinApi;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Auther: Louis
 * @Date: 2018/8/30 17:01
 * @Description:
 */
@RestController
public class DetectionServiceController implements DetectionServiceClient {

    private Logger logger = LoggerFactory.getLogger(DetectionServiceController.class);

    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private QuartzHandlerService quartzHandlerService;

    @Override
    public ResponseResult<PagedList<QuartzJobVo>> findQuartzJobPageList(@RequestBody QuartzJobCondition quartzJobCondition) {
        ResponseResult<PagedList<QuartzJobVo>> responseResult = new ResponseResult<PagedList<QuartzJobVo>>();
        try {
            if(StringUtils.isNotBlank(quartzJobCondition.getJobName())){
                quartzJobCondition.setJobName("%"+quartzJobCondition.getJobName()+"%");
            }else{
                quartzJobCondition.setJobName(null);
            }
            PagedList<QuartzJobVo> page = quartzJobService.findQuartzJobPageList(quartzJobCondition);
            responseResult.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<QuartzJobVo> findQuartzJobVoById(@RequestBody Long quartzJobId) {
        ResponseResult<QuartzJobVo> responseResult = new ResponseResult<QuartzJobVo>();
        try {
            QuartzJobVo quartzJob = quartzJobService.findQuartzJobVoById(quartzJobId);
            responseResult.setData(quartzJob);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<DetectionUser>> findUserPageList() {
        ResponseResult<PagedList<DetectionUser>> responseResult = new ResponseResult<PagedList<DetectionUser>>();
        try {
            PagedList<DetectionUser> page = quartzJobService.findUserPageList();
            responseResult.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<DbSource>> findDbSourcePageList() {
        ResponseResult<PagedList<DbSource>> responseResult = new ResponseResult<PagedList<DbSource>>();
        try {
            PagedList<DbSource> page = quartzJobService.findDbSourcePageList();
            if(page.getData()!=null && page.getData().size()>0){
                for (DbSource dbSource : page.getData()) {
                    dbSource.setPassword("******");
                    dbSource.setUri("******");
                }
            }
            responseResult.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Integer> addQuartzJob(@RequestBody QuartzJobVo quartzJobVo) {
        ResponseResult responseResult = new ResponseResult();
        try {
            Long id = Sequence.uniqId();
            QuartzJob job = new QuartzJob();
            job.setId(id);
            job.setUserId(quartzJobVo.getUserId());
            job.setDbId(quartzJobVo.getDbId());
            job.setCronExpression(quartzJobVo.getCronExpression());
            job.setDescription(quartzJobVo.getDescription());
            job.setJobName(quartzJobVo.getJobName());
            job.setJobSql(quartzJobVo.getJobSql());
            job.setWarningLevel(quartzJobVo.getWarningLevel());
            job.setWarningMsg(quartzJobVo.getWarningMsg());
            job.setWarningStatus((byte)0);
            job.setOperate(quartzJobVo.getOperate());
            job.setOptValue(quartzJobVo.getOptValue());
            job.setJobStatus(JobStatusEnum.STATE_NORMAL.name());
            quartzJobService.insert(job);
            job.setId(id);
            quartzHandlerService.addJob(job);
            responseResult.setData(id);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Void> deleteQuartzJob(@RequestBody Long quartzJobId) {
        ResponseResult responseResult = new ResponseResult<>();
        try {
            QuartzJob job = new QuartzJob();
            job.setId(quartzJobId);
            job.setJobStatus(JobStatusEnum.STATE_DELETED.name());
            quartzJobService.updateByPrimaryKeySelective(job);
            job = quartzJobService.selectByPrimaryKey(quartzJobId);
            quartzHandlerService.deleteJob(job);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<PagedList<QuartzJobResult>> findQuartzJobResultPageList(@RequestBody QuartzJobCondition quartzJobCondition) {
        ResponseResult<PagedList<QuartzJobResult>> responseResult = new ResponseResult<PagedList<QuartzJobResult>>();
        try {
            PagedList<QuartzJobResult> page = quartzJobService.findQuartzJobResultPageList(quartzJobCondition);
            responseResult.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Integer> addUser(@RequestBody DetectionUser user) {
        ResponseResult responseResult = new ResponseResult();
        try {
            Long id = Sequence.uniqId();
            DetectionUser dUser = new DetectionUser();
            dUser.setId(id);
            dUser.setName(user.getName());
            dUser.setPassword(user.getPassword());
            dUser.setPhone(user.getPhone());
            dUser.setEmail(user.getEmail());
            dUser.setCreateTime(new Date());
            quartzJobService.insertUser(dUser);
            responseResult.setData(id);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Integer> addDbSource(@RequestBody DbSource dbSource) {
        ResponseResult responseResult = new ResponseResult();
        try {
            Long id = Sequence.uniqId();
            DbSource ds = new DbSource();
            ds.setId(id);
            ds.setAlias(dbSource.getAlias());
            ds.setUserName(dbSource.getUserName());
            ds.setPassword(dbSource.getPassword());
            ds.setUri(dbSource.getUri());
            ds.setCreateTime(new Date());
            quartzJobService.insertDbSource(ds);
            responseResult.setData(id);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Void> resumeQuartzJob(@RequestBody Long quartzJobId) {
        ResponseResult responseResult = new ResponseResult<>();
        try {
            QuartzJob job = new QuartzJob();
            job.setId(quartzJobId);
            job.setJobStatus(JobStatusEnum.STATE_NORMAL.name());
            quartzJobService.updateByPrimaryKeySelective(job);
            job = quartzJobService.selectByPrimaryKey(quartzJobId);
            quartzHandlerService.resumeJob(job);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Void> pauseQuartzJob(@RequestBody Long quartzJobId) {
        ResponseResult responseResult = new ResponseResult<>();
        try {
            QuartzJob job = new QuartzJob();
            job.setId(quartzJobId);
            job.setJobStatus(JobStatusEnum.STATE_PAUSED.name());
            quartzJobService.updateByPrimaryKeySelective(job);
            job = quartzJobService.selectByPrimaryKey(quartzJobId);
            quartzHandlerService.pauseJob(job);
        } catch (Exception e) {
            e.printStackTrace();
            responseResult.setCode(BusinessCode.CODE_1001);
        }
        return responseResult;
    }

    @Override
    public ResponseResult<Void> findErrorApiPageList() {
        return ZipkinApi.findErrorApiPageList();
    }
}
