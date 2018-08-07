package com.winhxd.b2c.store.service;


import com.winhxd.b2c.common.domain.PagedList;
import com.winhxd.b2c.common.domain.backstage.store.condition.BackStageStoreInfoCondition;
import com.winhxd.b2c.common.domain.backstage.store.vo.BackStageStoreVO;
import com.winhxd.b2c.common.domain.system.login.vo.StoreUserInfoVO;

/**
 * @Description: 门店服务接口类
 * @author chengyy
 * @date 2018/8/3 10:50
 */
public interface StoreService {
    /**
     * @author chengyy
     * @date 2018/8/3 13:23
     * @Description  门店用户绑定
     * @param customerId 用户id
     * @param storeUserId 门店id
     * @return 绑定状态码(0绑定失败,1绑定成功,-1用户已经和当前门店存在绑定关系，-2用户已经和其他门店存在绑定关系)
     */
    int bindCustomer(Long customerId, Long storeUserId);
    /**
     * @author chengyy
     * @date 2018/8/3 16:09
     * @Description 根据门店的id查询门店信息
     * @param storeUserId 门店id
     * @return  StoreUserInfoVO 门店VO
     */
    StoreUserInfoVO findStoreUserInfo(Long storeUserId);

    /**
     * 根据参数查询门店列表
     * @param storeCondition
     * @return
     */
    PagedList<BackStageStoreVO> findStoreUserInfo(BackStageStoreInfoCondition storeCondition);
}
