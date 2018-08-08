package com.winhxd.b2c.promotion.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponInvestorCondition;
import com.winhxd.b2c.common.domain.promotion.enums.CouponTemplateEnum;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestor;
import com.winhxd.b2c.common.domain.promotion.model.CouponInvestorDetail;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInvestorVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import com.winhxd.b2c.promotion.dao.CouponInvestorDetailMapper;
import com.winhxd.b2c.promotion.dao.CouponInvestorMapper;
import com.winhxd.b2c.promotion.service.CouponInvestorService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public int saveCouponInvestor( CouponInvestorCondition condition) {
        // flag  0 成功  1占比之和不等于100  2 出资方重复  1001失败  3 出资方明细为空
        int flag = BusinessCode.CODE_1001;
        List deatils = condition.getDetails();
        if(deatils == null){
            flag = CouponTemplateEnum.INVERSTOR_DETAIL_ISNULL.getCode();
            return flag;
        }
        if(deatils!=null && deatils.size()>0){
            Float tempPercent = 0.00f;
            for(int i=0;i<deatils.size();i++){
                LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
                tempPercent += Float.parseFloat(map.get("percent").toString());
            }
            //占比之和必须等于100
            if(tempPercent!=100.00f){
                flag = CouponTemplateEnum.PERCENT_NOT_EQ100.getCode();
                return flag;
            }
            //如果不止一个出资方，但是参数中的出资方的类型 或者品牌编码相等 , 返回出资方不能重复
            if(deatils.size()>1){
               Set set = new HashSet();
                for(int i=0;i<deatils.size();i++){
                    LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
                    boolean isContains =  set.add(map.get("investor_type").toString());
                    if(isContains == false){
                        flag = CouponTemplateEnum.INVERSTOR_REPEAT.getCode();
                        return flag;
                    }
                }
            }
        }

        try {
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
            //插入详情
            for(int i=0;i<deatils.size();i++){
                CouponInvestorDetail detail = new CouponInvestorDetail();
                LinkedHashMap<String,Object> map =  (LinkedHashMap)deatils.get(i);
                if(map.get("brandCode")!=null){
                    detail.setIds(map.get("brandCode").toString());
                }
                detail.setInvestorId(couponInvestor.getId());
                detail.setInvestorType(Short.parseShort(map.get("investor_type").toString()));
                detail.setPercent(Float.parseFloat(map.get("percent").toString()));
                detail.setNames(map.get("names").toString());
                couponInvestorDetailMapper.insert(detail);
            }
            flag = BusinessCode.CODE_OK ;
        }catch (Exception e){
            flag = BusinessCode.CODE_1001;
            e.printStackTrace();
        }

        return flag;
    }



    @Override
    public ResponseResult<CouponInvestorVO> getCouponInvestorDetailById(Long id) {
        CouponInvestorVO vo = couponInvestorMapper.selectCouponInvestorDetailById(id);
        ResponseResult<CouponInvestorVO> result = new ResponseResult();
        result.setData(vo);
        return result;
    }

    @Override
    public int updateCouponInvestorToValid(long id) {
        int count = couponInvestorMapper.updateCouponInvestorToValid(id);
        return count;
    }
}
