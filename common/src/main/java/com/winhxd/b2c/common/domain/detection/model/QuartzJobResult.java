package com.winhxd.b2c.common.domain.detection.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "t_quartz_job_result")
public class QuartzJobResult {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 任务ID
     */
    @Column(name = "job_id")
    private Long jobId;

    /**
     * 任务执行结果
     */
    @Column(name = "result_value")
    private String resultValue;

    /**
     * 任务执行时长
     */
    private String duration;

    /**
     * 是否成功
     */
    @Column(name = "is_success")
    private Boolean isSuccess;

    /**
     * 错误消息
     */
    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * 创建时间
     */
    @Column(name = "result_time")
    private Date resultTime;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取任务ID
     *
     * @return job_id - 任务ID
     */
    public Long getJobId() {
        return jobId;
    }

    /**
     * 设置任务ID
     *
     * @param jobId 任务ID
     */
    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    /**
     * 获取任务执行结果
     *
     * @return result_value - 任务执行结果
     */
    public String getResultValue() {
        return resultValue;
    }

    /**
     * 设置任务执行结果
     *
     * @param resultValue 任务执行结果
     */
    public void setResultValue(String resultValue) {
        this.resultValue = resultValue == null ? null : resultValue.trim();
    }

    /**
     * 获取任务执行时长
     *
     * @return duration - 任务执行时长
     */
    public String getDuration() {
        return duration;
    }

    /**
     * 设置任务执行时长
     *
     * @param duration 任务执行时长
     */
    public void setDuration(String duration) {
        this.duration = duration == null ? null : duration.trim();
    }

    /**
     * 获取是否成功
     *
     * @return is_success - 是否成功
     */
    public Boolean getIsSuccess() {
        return isSuccess;
    }

    /**
     * 设置是否成功
     *
     * @param isSuccess 是否成功
     */
    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * 获取错误消息
     *
     * @return error_msg - 错误消息
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 设置错误消息
     *
     * @param errorMsg 错误消息
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg == null ? null : errorMsg.trim();
    }

    /**
     * 获取创建时间
     *
     * @return result_time - 创建时间
     */
    public Date getResultTime() {
        return resultTime;
    }

    /**
     * 设置创建时间
     *
     * @param resultTime 创建时间
     */
    public void setResultTime(Date resultTime) {
        this.resultTime = resultTime;
    }
}