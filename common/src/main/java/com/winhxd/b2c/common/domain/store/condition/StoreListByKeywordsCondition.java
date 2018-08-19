package com.winhxd.b2c.common.domain.store.condition;

import java.util.List;

import com.winhxd.b2c.common.domain.common.ApiCondition;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @author liuhanning
 * @date  2018年8月19日 下午2:45:26
 * @Description 根据添加获取门店列表
 * @version 
 */
@ApiModel("请求参数")
@Data
public class StoreListByKeywordsCondition extends ApiCondition{
	@ApiModelProperty(value = "门店id集合")
	private List<Long> storeIds;
	@ApiModelProperty(value = "用户账号集合")
    private List<String> storeMobiles;
}