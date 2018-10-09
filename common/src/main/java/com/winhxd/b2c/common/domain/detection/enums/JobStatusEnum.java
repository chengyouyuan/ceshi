package com.winhxd.b2c.common.domain.detection.enums;

/**
 * @Auther: Louis
 * @Date: 2018/8/29 09:36
 * @Description:
 */
public enum JobStatusEnum {

    STATE_NONE (-1, "不存在"),
    STATE_NORMAL (0, "正常"),
    STATE_PAUSED (1, "暂停"),
    STATE_COMPLETE (2, "完成"),
    STATE_ERROR (3, "错误"),
    STATE_BLOCKED (4, "阻塞"),
    STATE_DELETED (5, "删除");

    private Integer code;
    private String desc;

    JobStatusEnum(Integer code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
