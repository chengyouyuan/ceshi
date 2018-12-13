package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponInvestorDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponInvestorMapper;
import com.winhxd.b2c.promotion.service.CouponInvestorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author wl
 * @Date 2018/8/7 13:09
 * @Description
 **/
@Service
public class CouponInvestorServiceImpl implements CouponInvestorService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponInvestorServiceImpl.class);
    @Autowired
    private CouponInvestorMapper couponInvestorMapper;
    @Autowired
    private CouponInvestorDetailMapper  couponInvestorDetailMapper;


    @Override
    @Transactional
    public int saveCouponInvestor(CouponInvestorCondition condition) {
        // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
        LOGGER.info("添加出资方参数:{}",condition);
        List<LinkedHashMap<String, Object>> deatils = condition.getDetails();
        int flag = 0;
        CouponInvestor couponInvestor = new CouponInvestor();
        couponInvestor.setCode(condition.getCode());
        couponInvestor.setName(condition.getName());
        couponInvestor.setRemarks(condition.getRemarks());
        couponInvestor.setStatus(condition.getStatus());
        couponInvestor.setCreated(new Date());
        couponInvestor.setCreatedBy(Long.parseLong(condition.getUserId()));
        couponInvestor.setCreatedByName(condition.getUserName());
        //插入主表
        Long keyId = couponInvestorMapper.insertCouponInvestor(couponInvestor);
        if (keyId == 0) {
            throw new BusinessException(BusinessCode.CODE_500006, "添加出资方规则失败");
        }
        //插入详情
        for (int i = 0; i < deatils.size(); i++) {
            CouponInvestorDetail detail = new CouponInvestorDetail();
            LinkedHashMap<String, Object> map = (LinkedHashMap) deatils.get(i);
            if (map.get("companyCode") != null) {
                detail.setIds(map.get("companyCode").toString());
            }
            if (couponInvestor.getId() != null) {
                detail.setInvestorId(couponInvestor.getId());
            }
            if (map.get("investor_type") != null) {
                detail.setInvestorType(Short.parseShort(map.get("investor_type").toString()));
            }
            if (map.get("percent") != null) {
                detail.setPercent(Float.parseFloat(map.get("percent").toString()));
            }
            if (map.get("names") != null) {
                detail.setNames(map.get("names").toString());
            }
            int n = couponInvestorDetailMapper.insert(detail);
            if (n == 0) {
                throw new BusinessException(BusinessCode.CODE_500006, "添加出资方规则失败");
            }
        }
        return flag;
    }





    @Override
    public PagedList<CouponInvestorVO> getCouponInvestorPage(CouponInvestorCondition condition) {
        PagedList<CouponInvestorVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<CouponInvestorVO> couponInvestorList = couponInvestorMapper.getCouponInvestorPage(condition);
        //将数据中的出资方详情和占比 拼接为一个字段返回给前端
        List<CouponInvestorVO> tempList = this.buildFinalList(couponInvestorList);

        List<Map<Long, Object>> list = null;
        if (!CollectionUtils.isEmpty(couponInvestorList)) {
            List<Long> ids = couponInvestorList.stream().map(CouponInvestorVO::getId).collect(Collectors.toList());
            list = couponInvestorMapper.getRelationCouponInvCountMap(ids);
        }

        if (!CollectionUtils.isEmpty(list)) {
            Map<Long, Object> resultMap = getCouponInvestorMap(list);
            for (CouponInvestorVO couponInvestorVO : tempList) {
                if (resultMap.containsKey(couponInvestorVO.getId())) {
                    Long relTempleteCount = (Long) resultMap.get(couponInvestorVO.getId());
                    String templeteCount = relTempleteCount == null ? "0" : String.valueOf(relTempleteCount);
                    couponInvestorVO.setRelTempleteCount(templeteCount);
                }
            }
        }

        PageInfo<CouponInvestorVO> pageInfo = new PageInfo<>(tempList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }

    /**
     * 封装返回的数据，组装成所要的数据
     *
     * @param list
     * @return
     */
    public Map<Long, Object> getCouponInvestorMap(List<Map<Long, Object>> list) {

        Map<Long, Object> resultMap = new HashMap<>(list.size());
        for (Map<Long, Object> map : list) {
            Long id = (Long) map.get("id");
            Long relTempleteCount = (Long) map.get("relTempleteCount");
            resultMap.put(id, relTempleteCount);
        }
        return resultMap;
    }

    @Override
    public PagedList<InvertorTempleteCountVO> findInvertorTempleteCountPage(RuleRealationCountCondition condition) {
        PagedList<InvertorTempleteCountVO> pagedList = new PagedList<>();
        PageHelper.startPage(condition.getPageNo(),condition.getPageSize());
        List<InvertorTempleteCountVO> couponInvestorCountPageList = couponInvestorMapper.getInvertorTempleteCountPage(condition.getId());
        PageInfo<InvertorTempleteCountVO> pageInfo = new PageInfo<>(couponInvestorCountPageList);
        pagedList.setData(pageInfo.getList());
        pagedList.setPageNo(pageInfo.getPageNum());
        pagedList.setPageSize(pageInfo.getPageSize());
        pagedList.setTotalRows(pageInfo.getTotal());
        return pagedList;
    }


    @Override
    public CouponInvestorVO getCouponInvestorDetailById(String id) {
        CouponInvestorVO vo = couponInvestorMapper.selectCouponInvestorDetailById(Long.parseLong(id));
        return vo;
    }

    @Override
    public int updateCouponInvestorToValid(Long id,Long userId,String userName) {
        int count = couponInvestorMapper.updateCouponInvestorToValid(id,userId,userName);
        return count;
    }



    public List<CouponInvestorVO>  buildFinalList(List<CouponInvestorVO> list){
        if (!CollectionUtils.isEmpty(list)) {
            StringBuilder investorNames = new StringBuilder();
            StringBuilder investorPercents = new StringBuilder();

            for (CouponInvestorVO couponInvestorVO : list) {
                List<CouponInvestorDetail> detailList = couponInvestorVO.getDetailList();
                if (!CollectionUtils.isEmpty(detailList)) {
                    for (CouponInvestorDetail investorDetail : detailList) {
                        investorNames.append(investorDetail.getNames()).append("/");
                        investorPercents.append(investorDetail.getPercent()).append("/");
                    }
                    couponInvestorVO.setInvestorNames(
                            investorNames.deleteCharAt(investorNames.lastIndexOf("/")).toString());
                    couponInvestorVO.setInvestorPercents(
                            investorPercents.deleteCharAt(investorPercents.lastIndexOf("/")).toString());

                    // 清空之前数据
                    investorNames.delete(0, investorNames.length());
                    investorPercents.delete(0, investorPercents.length());
                }
            }
        }
        return list;
    }

    
}
