package com.winhxd.b2c.detection.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.common.domain.detection.model.QuartzJobResult;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import com.winhxd.b2c.detection.service.base.IBaseService;

/**
 * @Auther: Louis
 * @Date: 2018/8/28 18:55
 * @Description:
 */
public interface IQuartzJobService extends IBaseService<QuartzJob> {

    void saveQuartzJobResult(QuartzJobResult result);

    DbSource selectDbSource(Long dbId);

    PagedList<QuartzJobVo> findQuartzJobPageList(QuartzJobCondition quartzJobCondition);

    QuartzJobVo findQuartzJobVoById(Long quartzJobId);

    PagedList<QuartzJobResult> findQuartzJobResultPageList(QuartzJobCondition quartzJobCondition);

    PagedList<DetectionUser> findUserPageList();

    PagedList<com.winhxd.b2c.common.domain.detection.model.DbSource> findDbSourcePageList();

    void insertUser(DetectionUser dUser);

    void insertDbSource(DbSource ds);

    DetectionUser findUserById(Long id);

}
