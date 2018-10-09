package com.winhxd.b2c.common.domain.detection.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "t_quartz_job")
public class QuartzJob {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 数据源ID
     */
    @Column(name = "db_id")
    private Long dbId;

    /**
     * sql语句
     */
    @Column(name = "job_sql")
    private String jobSql;

    /**
     * 比较符号
     */
    private String operate;

    /**
     * 比较值
     */
    @Column(name = "opt_value")
    private Integer optValue;

    /**
     * 警告级别
     */
    @Column(name = "warning_level")
    private Byte warningLevel;

    /**
     * 警告描述
     */
    @Column(name = "warning_msg")
    private String warningMsg;

    /**
     * 警告状态
     */
    @Column(name = "warning_status")
    private Byte warningStatus;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 时间表达式
     */
    @Column(name = "cron_expression")
    private String cronExpression;

    /**
     * 任务状态
     */
    @Column(name = "job_status")
    private String jobStatus;

    /**
     * 启动时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 前一次运行时间
     */
    @Column(name = "previous_time")
    private Date previousTime;

    /**
     * 下次运行时间
     */
    @Column(name = "next_time")
    private Date nextTime;

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
     * 获取用户ID
     *
     * @return user_id - 用户ID
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * 设置用户ID
     *
     * @param userId 用户ID
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * 获取数据源ID
     *
     * @return db_id - 数据源ID
     */
    public Long getDbId() {
        return dbId;
    }

    /**
     * 设置数据源ID
     *
     * @param dbId 数据源ID
     */
    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    /**
     * 获取sql语句
     *
     * @return job_sql - sql语句
     */
    public String getJobSql() {
        return jobSql;
    }

    /**
     * 设置sql语句
     *
     * @param jobSql sql语句
     */
    public void setJobSql(String jobSql) {
        this.jobSql = jobSql == null ? null : jobSql.trim();
    }

    /**
     * 获取比较符号
     *
     * @return operate - 比较符号
     */
    public String getOperate() {
        return operate;
    }

    /**
     * 设置比较符号
     *
     * @param operate 比较符号
     */
    public void setOperate(String operate) {
        this.operate = operate == null ? null : operate.trim();
    }

    /**
     * 获取比较值
     *
     * @return opt_value - 比较值
     */
    public Integer getOptValue() {
        return optValue;
    }

    /**
     * 设置比较值
     *
     * @param optValue 比较值
     */
    public void setOptValue(Integer optValue) {
        this.optValue = optValue;
    }

    /**
     * 获取警告级别
     *
     * @return warning_level - 警告级别
     */
    public Byte getWarningLevel() {
        return warningLevel;
    }

    /**
     * 设置警告级别
     *
     * @param warningLevel 警告级别
     */
    public void setWarningLevel(Byte warningLevel) {
        this.warningLevel = warningLevel;
    }

    /**
     * 获取警告描述
     *
     * @return warning_msg - 警告描述
     */
    public String getWarningMsg() {
        return warningMsg;
    }

    /**
     * 设置警告描述
     *
     * @param warningMsg 警告描述
     */
    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg == null ? null : warningMsg.trim();
    }

    /**
     * 获取警告状态
     *
     * @return warning_status - 警告状态
     */
    public Byte getWarningStatus() {
        return warningStatus;
    }

    /**
     * 设置警告状态
     *
     * @param warningStatus 警告状态
     */
    public void setWarningStatus(Byte warningStatus) {
        this.warningStatus = warningStatus;
    }

    /**
     * 获取任务名称
     *
     * @return job_name - 任务名称
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * 设置任务名称
     *
     * @param jobName 任务名称
     */
    public void setJobName(String jobName) {
        this.jobName = jobName == null ? null : jobName.trim();
    }

    /**
     * 获取任务描述
     *
     * @return description - 任务描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置任务描述
     *
     * @param description 任务描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取时间表达式
     *
     * @return cron_expression - 时间表达式
     */
    public String getCronExpression() {
        return cronExpression;
    }

    /**
     * 设置时间表达式
     *
     * @param cronExpression 时间表达式
     */
    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression == null ? null : cronExpression.trim();
    }

    /**
     * 获取任务状态
     *
     * @return job_status - 任务状态
     */
    public String getJobStatus() {
        return jobStatus;
    }

    /**
     * 设置任务状态
     *
     * @param jobStatus 任务状态
     */
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus == null ? null : jobStatus.trim();
    }

    /**
     * 获取启动时间
     *
     * @return start_time - 启动时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置启动时间
     *
     * @param startTime 启动时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取前一次运行时间
     *
     * @return previous_time - 前一次运行时间
     */
    public Date getPreviousTime() {
        return previousTime;
    }

    /**
     * 设置前一次运行时间
     *
     * @param previousTime 前一次运行时间
     */
    public void setPreviousTime(Date previousTime) {
        this.previousTime = previousTime;
    }

    /**
     * 获取下次运行时间
     *
     * @return next_time - 下次运行时间
     */
    public Date getNextTime() {
        return nextTime;
    }

    /**
     * 设置下次运行时间
     *
     * @param nextTime 下次运行时间
     */
    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
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
}