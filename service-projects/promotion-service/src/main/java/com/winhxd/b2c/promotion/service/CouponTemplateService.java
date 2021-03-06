package com.winhxd.b2c.promotion.service;

import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;

import java.util.Date;
import java.util.List;

/**
 * @Author wl
 * @Date 2018/8/6 10:51
 * @Description 优惠券模板服务接口
 **/
public interface CouponTemplateService {
    /**
     *
     *@Deccription 添加优惠换模板
     *@Params  [couponTemplateCondition]
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 10:45
     */
    int saveCouponTemplate(CouponTemplateCondition couponTemplateCondition);

    /**
     *
     *@Deccription 模板列表页面跳转到修改页面 根据id 查询出对应的实体类
     *@Params   id  模板id
     *@Return   ResponseResult
     *@User     wl
     *@Date   2018/8/6 14:41
     */
    CouponTemplateVO getCouponTemplateById(String id);

    /**
     *
     *@Deccription 多条件分页查询 优惠券模板列表
     *@Params  CouponTemplateCondition
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 17:53
     */
    PagedList<CouponTemplateVO> findCouponTemplatePageByCondition(CouponTemplateCondition couponTemplateCondition);
    /**
     *
     *@Deccription 查看优惠券模板详情
     *@Params  id
     *@Return  ResponseResult
     *@User  wl
     *@Date   2018/8/6 20:45
     */
    CouponTemplateVO viewCouponTemplateDetailById(String id);

    /**
     *
     *@Deccription  设为无效
     *@Params  ids  多个页面勾选的ID 用逗号","隔开
     *@Return  ResponseResult 删除是否成功
     *@User  wl
     *@Date   2018/8/6 20:39
     */
    int updateCouponTemplateToValid(Long id, Long updatedBy, Date updated, String updatedByName);



}
