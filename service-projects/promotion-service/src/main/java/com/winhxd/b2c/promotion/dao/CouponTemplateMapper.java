package com.winhxd.b2c.promotion.dao;

import com.winhxd.b2c.common.domain.promotion.condition.CouponTemplateCondition;
import com.winhxd.b2c.common.domain.promotion.model.CouponTemplate;
import com.winhxd.b2c.common.domain.promotion.vo.CouponInStoreGetedAndUsedVO;
import com.winhxd.b2c.common.domain.promotion.vo.CouponTemplateVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

public interface CouponTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CouponTemplate record);

    int insertSelective(CouponTemplate record);

    CouponTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CouponTemplate record);

    int updateByPrimaryKey(CouponTemplate record);

    CouponTemplate selectCouponTemplateById(long id);

    List<CouponTemplateVO> getCouponTemplatePageByCondition(@Param("condition") CouponTemplateCondition condition);

    void updateCouponTemplateToValid(@Param("idsList") List<String> idsList, @Param("updateBy") Long updateBy, @Param("updated") Date updated, @Param("updateByName") String updateByName);

    int confirmUpdateCouponTemplate(@Param("updateBy")Long updateBy, @Param("updated")Date updated,@Param("updateByName")String updateByName, @Param("condition")CouponTemplateCondition condition);

    List<CouponInStoreGetedAndUsedVO> selectCouponInStoreGetedAndUsedPage(@RequestParam("storeId") Long storeId);
}