package com.winhxd.b2c.detection.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.detection.condition.QuartzJobCondition;
import com.winhxd.b2c.common.domain.detection.model.DbSource;
import com.winhxd.b2c.common.domain.detection.model.DetectionUser;
import com.winhxd.b2c.common.domain.detection.model.QuartzJob;
import com.winhxd.b2c.common.domain.detection.model.QuartzJobResult;
import com.winhxd.b2c.common.domain.detection.vo.QuartzJobVo;
import com.winhxd.b2c.detection.mapper.DbSourceMapper;
import com.winhxd.b2c.detection.mapper.DetectionUserMapper;
import com.winhxd.b2c.detection.mapper.QuartzJobMapper;
import com.winhxd.b2c.detection.mapper.QuartzJobResultMapper;
import com.winhxd.b2c.detection.service.IQuartzJobService;
import com.winhxd.b2c.detection.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: Louis
 * @Date: 2018/8/28 18:56
 * @Description:
 */
@Service
public class QuartzJobServiceImpl extends BaseServiceImpl<QuartzJob> implements IQuartzJobService {

    @Autowired
    private QuartzJobMapper quartzJobMapper;
    @Autowired
    private QuartzJobResultMapper quartzJobResultMapper;
    @Autowired
    private DetectionUserMapper detectionUserMapper;
    @Autowired
    private DbSourceMapper dbSourceMapper;


    @Override
    public PagedList<QuartzJobVo> findQuartzJobPageList(QuartzJobCondition quartzJobCondition) {
        PageHelper.startPage(quartzJobCondition.getPageNo(), quartzJobCondition.getPageSize());
        List<QuartzJobVo> jobList = quartzJobMapper.findQuartzJobList(quartzJobCondition);
        PageInfo<QuartzJobVo> pageInfo = new PageInfo<>(jobList);
        PagedList<QuartzJobVo> pagedList = new PagedList<>();
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    @Override
    public QuartzJobVo findQuartzJobVoById(Long quartzJobId) {
        return quartzJobMapper.findQuartzJobVoById(quartzJobId);
    }

    @Override
    public void saveQuartzJobResult(QuartzJobResult result) {
        quartzJobResultMapper.insert(result);
    }

    @Override
    public PagedList<QuartzJobResult> findQuartzJobResultPageList(QuartzJobCondition quartzJobCondition) {
        PageHelper.startPage(quartzJobCondition.getPageNo(), quartzJobCondition.getPageSize());
        QuartzJobResult entity = new QuartzJobResult();
        entity.setJobId(quartzJobCondition.getQuartzJobId());
        List<QuartzJobResult> list = quartzJobResultMapper.select(entity);
        PageInfo<QuartzJobResult> pageInfo = new PageInfo<QuartzJobResult>(list);
        PagedList<QuartzJobResult> pagedList = new PagedList<>();
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    @Override
    public PagedList<DetectionUser> findUserPageList() {
        List<DetectionUser> list = detectionUserMapper.selectAll();
        PagedList<DetectionUser> pagedList = new PagedList<DetectionUser>();
        pagedList.setData(list);
        pagedList.setPageNo(1);
        pagedList.setPageSize(list.size());
        pagedList.setTotalRows(list.size());
        return pagedList;
    }

    @Override
    public PagedList<DbSource> findDbSourcePageList() {
        List<DbSource> list = dbSourceMapper.selectAll();
        PagedList<DbSource> pagedList = new PagedList<com.winhxd.b2c.common.domain.detection.model.DbSource>();
        pagedList.setData(list);
        pagedList.setPageNo(1);
        pagedList.setPageSize(list.size());
        pagedList.setTotalRows(list.size());
        return pagedList;
    }

    @Override
    public DbSource selectDbSource(Long dbId) {
        return dbSourceMapper.selectByPrimaryKey(dbId);
    }

    @Override
    public void insertUser(DetectionUser dUser) {
        detectionUserMapper.insert(dUser);
    }

    @Override
    public void insertDbSource(DbSource ds) {
        dbSourceMapper.insert(ds);
    }

    @Override
    public DetectionUser findUserById(Long id) {
        return detectionUserMapper.selectByPrimaryKey(id);
    }
}
