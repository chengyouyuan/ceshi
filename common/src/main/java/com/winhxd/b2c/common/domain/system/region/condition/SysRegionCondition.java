<<<<<<< HEAD
package com.winhxd.b2c.common.domain.system.region.condition;

import com.winhxd.b2c.common.domain.base.condition.BaseCondition;
import com.winhxd.b2c.common.feign.system.enums.RegionLevelEnum;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * @description: ${description}
 * @author: zhanglingke
 * @create: 2018-08-06 11:39
 **/
public class SysRegionCondition {

    @ApiModelProperty(value = "区域行政级别")
    private Integer level;

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getRegionCode() {
        return regionCode;
    }
}
=======
package com.winhxd.b2c.common.domain.system.region.condition;

import com.winhxd.b2c.common.domain.common.ApiCondition;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: ${description}
 * @author: zhanglingke
 * @create: 2018-08-06 11:39
 **/
@Data
public class SysRegionCondition extends ApiCondition {

    @ApiModelProperty(value = "区域编号", required = true)
    private String regionCode;

    @ApiModelProperty(value = "区域行政级别（1=省，2=市，3=区县，4=乡/镇，5=村/居委会）")
    private Integer level;
}
>>>>>>> branch 'master' of git@192.168.1.101:retail2c/retail2c-backend.git
