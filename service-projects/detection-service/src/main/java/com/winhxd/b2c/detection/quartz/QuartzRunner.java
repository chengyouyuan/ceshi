package com.winhxd.b2c.detection.quartz;

import com.winhxd.b2c.common.domain.detection.enums.JobStatusEnum;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.detection.service.IQuartzJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class QuartzRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IQuartzJobService quartzJobService;
    @Autowired
    private QuartzHandlerService quartzHandlerService;

    @Override
    public void run(String... args) throws Exception {
        // 可执行的任务列表
        QuartzJob entity = new QuartzJob();
        entity.setJobStatus(JobStatusEnum.STATE_NORMAL.name());
        List<QuartzJob> jobList = quartzJobService.select(entity);
        logger.info("初始化加载定时任务......");
        for (QuartzJob job : jobList) {
            try {
                System.out.println(job.getJobName());
                quartzHandlerService.addJob(job);
            } catch (Exception e) {
                logger.error("add job error: " + job.getJobName() + " " + job.getId(), e);
            }
        }
    }

}