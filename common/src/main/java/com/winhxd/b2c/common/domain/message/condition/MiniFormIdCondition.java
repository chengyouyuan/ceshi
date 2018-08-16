package com.winhxd.b2c.common.domain.message.condition;

import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className MiniFormIdCondition 用户提交表单，保存formid
 * @description
 */
@Data
public class MiniFormIdCondition {
    /**
     * formId集合
     */
    List<String> formIds;
}
