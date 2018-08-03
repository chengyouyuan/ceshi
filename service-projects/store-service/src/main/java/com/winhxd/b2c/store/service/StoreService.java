package com.winhxd.b2c.store.service;
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
}
