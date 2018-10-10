package com.winhxd.b2c.detection.mapper;

import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface QuartzJobMapper extends Mapper<QuartzJob> {

    List<QuartzJobVo> findQuartzJobList(QuartzJobCondition quartzJobCondition);

    QuartzJobVo findQuartzJobVoById(Long quartzJobId);
}