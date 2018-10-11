package com.winhxd.b2c.promotion.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInStoreGetedAndUsedVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CouponTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplate record);

    int insertSelective(CouponTemplate record);

    CouponTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplate record);

    int updateByPrimaryKey(CouponTemplate record);

    CouponTemplate selectCouponTemplateById(long id);

    List<CouponTemplateVO> getCouponTemplatePageByCondition(@Param("condition") CouponTemplateCondition condition);

    int updateCouponTemplateToValid(@Param("id") Long id, @Param("updatedBy") Long updateBy, @Param("updated") Date updated, @Param("updatedByName") String updateByName);

    List<CouponInStoreGetedAndUsedVO> selectCouponInStoreGetedAndUsedPage( Long storeId);

    List<CouponInStoreGetedAndUsedVO> selectCouponGetedAndUsedCout(Map<String,Object> paraMap);

}