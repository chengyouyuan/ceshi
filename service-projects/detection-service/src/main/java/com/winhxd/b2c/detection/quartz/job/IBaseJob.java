package com.winhxd.b2c.detection.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @Auther: Louis
 * @Date: 2018/8/29 13:57
 * @Description:
 */
public interface IBaseJob extends Job {

    @Override
    void execute(JobExecutionContext context) throws JobExecutionException;
}
