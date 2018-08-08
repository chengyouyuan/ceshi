package com.winhxd.b2c.store.service;


import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.system.login.model.StoreUserInfo;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoSimpleVO;

/**
 * @author chengyy
 * @Description: 门店服务接口类
 * @date 2018/8/3 10:50
 */
public interface StoreService {
    /**
     * @param customerId  用户id
     * @param storeUserId 门店id
     * @return 绑定状态码(0绑定失败, 1绑定成功, - 1用户已经和当前门店存在绑定关系 ， - 2用户已经和其他门店存在绑定关系)
     * @author chengyy
     * @date 2018/8/3 13:23
     * @Description 门店用户绑定
     */
    int bindCustomer(Long customerId, Long storeUserId);

    /**
     * @param storeUserId 门店id
     * @return StoreUserInfoVO 门店VO
     * @author chengyy
     * @date 2018/8/3 16:09
     * @Description 根据门店的id查询门店信息
     */
    StoreUserInfoSimpleVO findStoreUserInfo(Long storeUserId);

    /**
     * 根据参数查询门店列表
     *
     * @param storeCondition
     * @return
     */
    PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition);

    /**
     * @param customerUserId 用户id
     * @return 门店信息
     * @author chengyy
     * @date 2018/8/7 14:06
     * @Description 根据用户id查询绑定的门店信息
     */
    StoreUserInfo findStoreUserInfoByCustomerId(Long customerUserId);

    /**
     * 根据门店编码查询门店信息
     *
     * @param storeId 门店编码
     * @return 门店信息
     */
    StoreUserInfo selectByStoreId(Long storeId);

    /**
     * 修改门店信息非空的字段
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(StoreUserInfo record);
}
