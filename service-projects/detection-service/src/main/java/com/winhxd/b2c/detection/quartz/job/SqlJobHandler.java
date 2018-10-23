package com.winhxd.b2c.detection.quartz.job;

import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.common.domain.detection.model.QuartzJobResult;
import com.winhxd.b2c.detection.datasource.ConnectionManager;
import com.winhxd.b2c.detection.quartz.SpringBeanFactory;
import com.winhxd.b2c.detection.quartz.job.email.EmailSendUtil;
import com.winhxd.b2c.detection.service.IQuartzJobService;
import com.winhxd.b2c.detection.util.Sequence;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 * @Auther: Louis
 * @Date: 2018/8/29 13:58
 * @Description:
 */
@Slf4j
@DisallowConcurrentExecution
@Component
public class SqlJobHandler implements IBaseJob {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private EmailSendUtil emailSendUtil;


    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail job = context.getJobDetail();
        JobKey key = job.getKey();
        String jobIdentity = "scheduleJob" + key.getGroup() + "_" + key.getName();
        Trigger trigger = context.getTrigger();
        QuartzJob scheduleJob = (QuartzJob) context.getMergedJobDataMap().get(jobIdentity);
        logger.info(Thread.currentThread().getName()+"运行任务名称 = [" + scheduleJob + "]");
        sqlJob(scheduleJob,trigger);
    }

    public void sqlJob(QuartzJob job,Trigger trigger) {
        long start = System.currentTimeMillis();
        IQuartzJobService quartzJobService = SpringBeanFactory.getBean(IQuartzJobService.class);
        job = quartzJobService.selectByPrimaryKey(job.getId());
        DbSource dbSource = quartzJobService.selectDbSource(job.getDbId());
        Connection conn = ConnectionManager.getConnection(dbSource);
        PreparedStatement pstm = null;
        ResultSet rs = null;
        QuartzJobResult result = new QuartzJobResult();
        result.setId(Sequence.uniqId());
        result.setJobId(job.getId());
        result.setResultTime(new Date());
        try {
            pstm = conn.prepareStatement(job.getJobSql());
            rs = pstm.executeQuery();
            Integer resultValue = null;
            while (rs.next()){
                resultValue = rs.getInt(1);
            }
            result.setResultValue(resultValue+"");
            job.setResultValue(resultValue+"");
            result.setIsSuccess(true);
            job.setIsSuccess(true);
            if(resultValue != null){
                String oprate = job.getOperate();
                Integer optValue = job.getOptValue();
                if(StringUtils.isNotBlank(oprate)){
                    if(StringUtils.isNotBlank(oprate) && oprate.equals(">")){
                        if(resultValue>optValue){
                            job.setWarningStatus((byte)1);
                            sendEmail(job);
                        }
                    } else if (oprate.equals("<")){
                        if(resultValue<optValue){
                            job.setWarningStatus((byte)1);
                            sendEmail(job);
                        }
                    } else if (oprate.equals("=")){
                        if(resultValue.equals(optValue)){
                            job.setWarningStatus((byte)1);
                            sendEmail(job);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result.setIsSuccess(false);
            result.setErrorMsg(e.getMessage());
            job.setIsSuccess(false);
            job.setErrorMsg(e.getMessage());
        } finally {
            try {
                if(rs!=null) {
                    rs.close();
                }
                if(pstm!=null){
                    pstm.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        long end = System.currentTimeMillis();
        job.setDuration((end-start)+"毫秒");
        job.setNextTime(trigger.getNextFireTime());
        job.setPreviousTime(trigger.getPreviousFireTime());
        quartzJobService.updateByPrimaryKeySelective(job);
        result.setDuration((end-start)+"毫秒");
        quartzJobService.saveQuartzJobResult(result);
    }

    /**
     * 组装参数调用发邮件接口
     */
    public void sendEmail(QuartzJob job){
        //根据用户的id获取用户的信息
        quartzJobService = SpringBeanFactory.getBean(IQuartzJobService.class);
        DetectionUser user = quartzJobService.findUserById(job.getUserId());
        //获取收件人的邮箱
        String toEmail = "";
        if (user!=null){
            toEmail = user.getEmail();
            if(StringUtils.isBlank(toEmail)){
                logger.warn("监控服务获取到的收件人的邮箱为空");
            }
        }
        logger.info("监控服务预警发送的收件人的邮箱为:"+toEmail);
        emailSendUtil = SpringBeanFactory.getBean(EmailSendUtil.class);
        emailSendUtil.sendMail(toEmail,null,"报警",job.getWarningMsg());
    }

}
