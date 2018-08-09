package com.winhxd.b2c.promotion.service.impl;

import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.vo.CouponApplyVO;
import com.winhxd.b2c.promotion.dao.CouponApplyMapper;
import com.winhxd.b2c.promotion.service.CouponApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author wl
 * @Date 2018/8/9 12:14
 * @Description
 **/
@Service
public class CouponApplyServiceImpl implements CouponApplyService {
     @Autowired
     private CouponApplyMapper couponApplyMapper;

    @Override
    public ResponseResult<CouponApplyVO> viewCouponApplyDetail(long id) {
        ResponseResult responseResult = new ResponseResult();
        CouponApplyVO vo = couponApplyMapper.viewCouponApplyDetail(id);
        responseResult.setData(vo);
        return responseResult;
    }

    @Override
    public int updateCouponApplyToValid(long id, long userId, String userName) {
        int count = couponApplyMapper.updateCouponGradeValid(id,userId,userName);
        return count;
    }


}
