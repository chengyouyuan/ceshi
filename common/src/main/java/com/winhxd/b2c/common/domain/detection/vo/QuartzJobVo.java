package com.winhxd.b2c.common.domain.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.winhxd.b2c.common.domain.detection.enums.JobStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

@ApiModel("任务详细信息")
public class QuartzJobVo {
    /**
     * 主键
     */
    @ApiModelProperty("任务主键")
    private Long id;

    /**
     * 用户ID
     */
    @ApiModelProperty("关联用户ID")
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty("关联用户名称")
    private String userName;

    /**
     * 数据源ID
     */
    @ApiModelProperty("关联数据源ID")
    private Long dbId;
    /**
     * 别名
     */
    @ApiModelProperty("关联数据源名称")
    private String dbAlias;

    /**
     * sql语句
     */
    @ApiModelProperty("任务执行SQL语句")
    private String jobSql;

    /**
     * 比较符号
     */
    @ApiModelProperty("比较符号")
    private String operate;

    /**
     * 比较值
     */
    @ApiModelProperty("比较值")
    private Integer optValue;

    /**
     * 警告级别
     */
    @ApiModelProperty("任务警告级别")
    private Byte warningLevel;

    /**
     * 警告描述
     */
    @ApiModelProperty("任务警告描述")
    private String warningMsg;

    /**
     * 警告状态
     */
    @ApiModelProperty("任务警告状态")
    private Byte warningStatus;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String jobName;

    /**
     * 任务描述
     */
    @ApiModelProperty("任务描述")
    private String description;

    /**
     * 时间表达式
     */
    @ApiModelProperty("任务执行时间表达式")
    private String cronExpression;

    /**
     * 任务状态
     */
    @ApiModelProperty("任务状态")
    private String jobStatus;

    /**
     * 启动时间
     */
    @ApiModelProperty("任务开始时间")
    @JsonFormat(locale="zh", timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 前一次运行时间
     */
    @ApiModelProperty("任务上次执行时间")
    @JsonFormat(locale="zh", timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date previousTime;

    /**
     * 下次运行时间
     */
    @ApiModelProperty("任务下次执行时间")
    @JsonFormat(locale="zh", timezone="GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private Date nextTime;

    /**
     * 任务执行结果
     */
    @ApiModelProperty("任务上次执行结果")
    private String resultValue;

    /**
     * 任务执行时长
     */
    @ApiModelProperty("任务上次执行时长")
    private String duration;

    /**
     * 是否成功
     */
    @ApiModelProperty("任务上次执行是否成功")
    private Boolean isSuccess;

    /**
     * 错误消息
     */
    @ApiModelProperty("任务上次执行错误消息")
    private String errorMsg;

    /**
     * 任务状态描述
     */
    @ApiModelProperty("任务状态描述")
    private String jobStatusStr;

    /**
     * 警告状态
     */
    @ApiModelProperty("任务警告状态描述")
    private String warningStatusStr;

    /**
     * 是否成功
     */
    @ApiModelProperty("是否成功描述")
    private String isSuccessStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getDbAlias() {
        return dbAlias;
    }

    public void setDbAlias(String dbAlias) {
        this.dbAlias = dbAlias;
    }

    public String getJobSql() {
        return jobSql;
    }

    public void setJobSql(String jobSql) {
        this.jobSql = jobSql;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public Integer getOptValue() {
        return optValue;
    }

    public void setOptValue(Integer optValue) {
        this.optValue = optValue;
    }

    public Byte getWarningLevel() {
        return warningLevel;
    }

    public void setWarningLevel(Byte warningLevel) {
        this.warningLevel = warningLevel;
    }

    public String getWarningMsg() {
        return warningMsg;
    }

    public void setWarningMsg(String warningMsg) {
        this.warningMsg = warningMsg;
    }

    public Byte getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(Byte warningStatus) {
        this.warningStatus = warningStatus;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getPreviousTime() {
        return previousTime;
    }

    public void setPreviousTime(Date previousTime) {
        this.previousTime = previousTime;
    }

    public Date getNextTime() {
        return nextTime;
    }

    public void setNextTime(Date nextTime) {
        this.nextTime = nextTime;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getJobStatusStr() {
        if(StringUtils.isNotBlank(jobStatus)){
            jobStatusStr = JobStatusEnum.valueOf(jobStatus).getDesc();
        }
        return jobStatusStr;
    }

    public void setJobStatusStr(String jobStatusStr) {
        this.jobStatusStr = jobStatusStr;
    }

    public String getWarningStatusStr() {
        if(null!=warningStatus){
            if(warningStatus==(byte)0){
                warningStatusStr = "正常";
            }else{
                warningStatusStr = "报警";
            }
        }
        return warningStatusStr;
    }

    public void setWarningStatusStr(String warningStatusStr) {
        this.warningStatusStr = warningStatusStr;
    }

    public String getIsSuccessStr() {
        if(isSuccess!=null){
            if(isSuccess){
                isSuccessStr = "成功";
            }else{
                isSuccessStr = "失败";
            }
        }
        return isSuccessStr;
    }

    public void setIsSuccessStr(String isSuccessStr) {
        this.isSuccessStr = isSuccessStr;
    }
}