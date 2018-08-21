package com.winhxd.b2c.common.domain.message.condition;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author jujinbiao
 * @className MiniMsgCondition 小程序模板消息
 * @description
 */
@Data
public class MiniMsgCondition {
    @ApiModelProperty("消息接受人的openid")
    private String toUser;

    @ApiModelProperty("消息模板类型（参照MiniMsgTypeEnum的msgType）")
    private Short msgType;

    @ApiModelProperty("点击模板卡片后的跳转页面，不填则模板无跳转。")
    private String page;
    
    @ApiModelProperty("模板内容，不填则下发空模板。 数组元素的顺序，要求和小程序的模板库中的对应模板的参数顺序一致。")
    private List<MiniTemplateData> data;

    @ApiModelProperty("模板需要放大的关键词，不填则默认无放大")
    private String emphasisKeyword;
}
/**
 * 代码示例：
 *          MiniMsgCondition condition = new MiniMsgCondition();
 *         //消息接受人的openid
 *         condition.setToUser("FDAFLKDSAFLSDAFJSLKADFJLSADJFSDLA");
 *         //消息模板类型（参照MiniMsgTypeEnum的msgType）
 *         condition.setMsgType((short)1);
 *         //点击模板卡片后的跳转页面，不填则模板无跳转。
 *         condition.setPage("xxxx");
 *         //模板内容，不填则下发空模板。
 *         //数组元素的顺序，要求和小程序的模板库中的对应模板的参数顺序一致。
 *         List<MiniTemplateData> templateData = new ArrayList<>();
 *         MiniTemplateData data1 = new MiniTemplateData();
 *         data1.setKeyName("keyword1");
 *         data1.setValue("111");
 *         templateData.add(data1);
 *         MiniTemplateData data2 = new MiniTemplateData();
 *         data2.setKeyName("keyword2");
 *         data2.setValue("222");
 *         templateData.add(data2);
 *         MiniTemplateData data3 = new MiniTemplateData();
 *         data3.setKeyName("keyword3");
 *         data3.setValue("333");
 *         templateData.add(data3);
 *         condition.setData(templateData);
 *         condition.setEmphasis_keyword("keyword3.DATA");
 *         System.out.println(JsonUtil.toJSONString(condition));
 */
