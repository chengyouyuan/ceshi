package com.winhxd.b2c.store.dao;

import com.github.pagehelper.Page;
import com.winhxd.b2c.common.domain.store.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.store.vo.StoreUserInfoVO;
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

    StoreUserInfo selectByStoreUserInfo(StoreUserInfo storeUserInfo);

    Page<StoreUserInfo> selectStoreUserInfo(StoreUserInfo storeUserInfo);

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
     * @param storeCustomerId 门店编码
     * @return 门店信息
     */
    StoreUserInfo selectByStoreCustomerId(@Param("storeCustomerId") Long storeCustomerId);

    /**
     * @param ids 门店id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/8 10:19
     * @Description 根据ids进行批量查询门店信息
     */
    List<StoreUserInfo> selectStoreUserByIds(@Param("ids") Set<Long> ids);

    /**
     * 根据customerid 更改 regioncode
     *
     * @param storeUserInfo
     */
    void updateRegionCodeByCustomerId(StoreUserInfo storeUserInfo);

    /**
     * @param record 查询条件
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/13 19:46
     * @Description 根据条件查询门店信息
     */
    List<StoreUserInfoVO> selectStoreByCondition(StoreUserInfo record);

    /**
     * 根据regionCode集合查询门店
     * @param
     * @return
     */
    List<String> selectByRegionCodes(@Param("regionCodeList") List<String> regionCodeList);
}