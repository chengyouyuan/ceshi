package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.common.domain.promotion.vo.InvertorTempleteCountVO;
import com.winhxd.b2c.common.domain.promotion.vo.TempleteRelationCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.promotion.dao.CouponInvestorDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponInvestorMapper;
import com.winhxd.b2c.promotion.service.CouponInvestorService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @Author wl
 * @Date 2018/8/7 13:09
 * @Description
 **/
@Service
public class CouponInvestorServiceImpl implements CouponInvestorService {
    @Autowired
    private CouponInvestorMapper couponInvestorMapper;
    @Autowired
    private CouponInvestorDetailMapper  couponInvestorDetailMapper;


    @Override
    @Transactional
    public int saveCouponInvestor( CouponInvestorCondition condition) {
        // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
        List deatils = condition.getDetails();
        int flag = 0 ;
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
                if(keyId==0){
                   throw new BusinessException(BusinessCode.CODE_500006,"添加出资方规则失败");
                }
                //插入详情
                for(int i=0;i<deatils.size();i++){
                    CouponInvestorDetail detail = new CouponInvestorDetail();
                    LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
                    if(map.get("companyCode")!=null){
                        detail.setIds(map.get("companyCode").toString());
                    }
                    detail.setInvestorId(couponInvestor.getId());
                    detail.setInvestorType(Short.parseShort(map.get("investor_type").toString()));
                    detail.setPercent(Float.parseFloat(map.get("percent").toString()));
                    detail.setNames(map.get("names").toString());
                    int n = couponInvestorDetailMapper.insert(detail);
                    if(n==0){
                        throw new BusinessException(BusinessCode.CODE_500006,"添加出资方规则失败");
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

        if(tempList!=null && tempList.size()>0){
            for(int i=0;i<tempList.size();i++){
                CouponInvestorVO vo = tempList.get(i);
                TempleteRelationCountVO templeteRelationCountVO = couponInvestorMapper.getRelationCouponInvCount(vo.getId());
                if(templeteRelationCountVO!=null){
                    vo.setRelTempleteCount(String.valueOf(templeteRelationCountVO.getRelTempleteCount()));
                }else{
                    vo.setRelTempleteCount(String.valueOf(0));
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
    public CouponInvestorVO getCouponInvestorDetailById(Long id) {
        CouponInvestorVO vo = couponInvestorMapper.selectCouponInvestorDetailById(id);
        return vo;
    }

    @Override
    @Transactional
    public int updateCouponInvestorToValid(long id,long userId,String userName) {
        int count = couponInvestorMapper.updateCouponInvestorToValid(id,userId,userName);
        return count;
    }



    public List<CouponInvestorVO>  buildFinalList(List<CouponInvestorVO> list){
     if(list!=null && list.size()>0){
         for(int i=0;i<list.size();i++){
             CouponInvestorVO vo = list.get(i);
             List<CouponInvestorDetail> detailList  = vo.getDetailList();
             if(detailList!=null && detailList.size()>0){
               String investorNames="";
               String investorPercents="";
                 for(int j=0;j<detailList.size();j++){
                     investorNames += detailList.get(j).getNames()+"/";
                     investorPercents += detailList.get(j).getPercent()+"/";
                 }
                 vo.setInvestorNames(investorNames.substring(0,investorNames.length()-1));
                 vo.setInvestorPercents(investorPercents.substring(0,investorPercents.length()-1));
             }
         }
     }
     return list;
    }

}
