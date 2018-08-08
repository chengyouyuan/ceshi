package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.promotion.dao.CouponGradeMapper;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wl
 * @Date 2018/8/8 18:30
 * @Description
 **/
@Service
public class CouponGradeServiceImpl implements CouponGradeService {
     @Autowired
     private CouponGradeMapper couponGradeMapper;

    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(long id) {
        ResponseResult responseResult = new ResponseResult();
        CouponGradeVO vo = couponGradeMapper.viewCouponGradeDetail(id);
        responseResult.setData(vo);
        return responseResult;
    }

    @Override
    public int updateCouponGradeValid(long id,long userId,String userName) {
        int count = couponGradeMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }
}
