package com.winhxd.b2c.store.dao;

import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StoreUserInfoMapper {
    int deleteByPrimaryKey(Long businessId);

    int insert(StoreUserInfo record);

    int insertSelective(StoreUserInfo record);

    StoreUserInfo selectByPrimaryKey(Long businessId);

    int updateByPrimaryKeySelective(StoreUserInfo record);

    int updateByPrimaryKey(StoreUserInfo record);

    StoreUserInfo selectByStoreUserInfo(StoreUserInfo storeMobile);

    List<StoreUserInfo> findStoreUserInfo(StoreUserInfo storeUserInfo);

    /**
     * @param customerUserId 用户id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 14:08
     * @Description 根据用户id查询绑定门店信息
     */
    StoreUserInfo selectStoreUserInfoByCustomerId(@Param("customerUserId") Long customerUserId);

    /**
     * 根据门店编码查询门店信息
     *
     * @param storeId 门店编码
     * @return 门店信息
     */
    StoreUserInfo selectByStoreId(@Param("storeId")Long storeId);

    /**
     * @param ids 门店id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/8 10:19
     * @Description 根据ids进行批量查询门店信息
     */
    List<StoreUserInfoVO> selectStoreUserByIds(@Param("dis") Set<Long> ids);
}