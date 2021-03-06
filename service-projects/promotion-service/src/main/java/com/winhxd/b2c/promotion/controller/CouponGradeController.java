package com.winhxd.b2c.promotion.controller;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponGradeCondition;
import com.winhxd.b2c.common.domain.promotion.condition.CouponSetToValidCondition;
import com.winhxd.b2c.common.domain.promotion.condition.RuleRealationCountCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponGradeVO;
import com.winhxd.b2c.common.domain.promotion.vo.GradeTempleteCountVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.feign.promotion.CouponGradeServiceClient;
import com.winhxd.b2c.promotion.service.CouponGradeService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author wl
 * @Date 2018/8/8 18:24
 * @Description
 **/
@RestController
public class CouponGradeController implements CouponGradeServiceClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouponGradeController.class);

     @Autowired
     private CouponGradeService couponGradeService;

    /**
     *
     *@Deccription
     *@Params  couponGradeCondition
     *@Return  ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:40
     */
    @Override
    public ResponseResult<Integer> addCouponGrade(@RequestBody CouponGradeCondition couponGradeCondition) {
        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        int flag = couponGradeService.addCouponGrade(couponGradeCondition);
        if (flag == 0) {
            responseResult.setCode(BusinessCode.CODE_OK);
            responseResult.setMessage("添加成功");
        }
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则详情查看
     *@Params
     *@Return
     *@User  wl
     *@Date   2018/8/11 14:41
     */
    @Override
    public ResponseResult<CouponGradeVO> viewCouponGradeDetail(@RequestParam("id") String id) {
        LOGGER.info("查看详情参数id={}", id);
        if (StringUtils.isBlank(id)) {
            throw new BusinessException(BusinessCode.CODE_500010, "必填参数错误");
        }

        ResponseResult<CouponGradeVO> responseResult = new ResponseResult<CouponGradeVO>();
        CouponGradeVO vo = couponGradeService.viewCouponGradeDetail(id);
        responseResult.setData(vo);
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则设为无效/逻辑删除
     *@Params  id  userId  userName
     *@Return ResponseResult<Integer> 0 表示成功
     *@User  wl
     *@Date   2018/8/11 14:42
     */
    @Override
    public ResponseResult<Integer> updateCouponGradeValid(@RequestBody CouponSetToValidCondition condition) {
        LOGGER.info("坎级规则设置无效参数 id:={},userId:={},userName:={}", condition.getId(), condition.getUserId(), condition.getUserName());
        if (null == condition.getId() || null == condition.getUserId()
                || StringUtils.isBlank(condition.getUserName())) {
            throw new BusinessException(BusinessCode.CODE_500010, "必传参数错误");
        }

        ResponseResult<Integer> responseResult = new ResponseResult<Integer>();
        int count = couponGradeService.updateCouponGradeValid(condition.getId(), condition.getUserId(), condition.getUserName());
        if (count > 0) {
            responseResult.setCode(BusinessCode.CODE_OK);
            responseResult.setMessage("设置成功");
        }
        return responseResult;
    }

    /**
     *
     *@Deccription 坎级规则分页查询
     *@Params  condition
     *@Return  ResponseResult<PagedList<CouponGradeVO>>
     *@User  wl
     *@Date   2018/8/11 14:44
     */
    @Override
    public ResponseResult<PagedList<CouponGradeVO>> getCouponGradePage(@RequestBody CouponGradeCondition condition) {
        ResponseResult<PagedList<CouponGradeVO>> result = new ResponseResult<PagedList<CouponGradeVO>>();
        PagedList<CouponGradeVO> pagedList = couponGradeService.getCouponGradePage(condition);
        result.setData(pagedList);
        return result;
    }

    /**
     *
     *@Deccription 坎级规则关联模板分页查询
     *@Params  gradeId,pageNo,pageSize
     *@Return  ResponseResult<PagedList<GradeTempleteCountVO>>
     *@User  wl
     *@Date   2018/8/11 14:44
     */
    @Override
    public ResponseResult<PagedList<GradeTempleteCountVO>> findGradeTempleteCountPage(@RequestBody RuleRealationCountCondition condition) {
        ResponseResult<PagedList<GradeTempleteCountVO>> result = new ResponseResult<PagedList<GradeTempleteCountVO>>();
        PagedList<GradeTempleteCountVO> pagedList = couponGradeService.findGradeTempleteCountPage(condition);
        result.setData(pagedList);
        return result;
    }


}
