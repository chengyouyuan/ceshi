package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.message.vo.MiniProgramConfigVO;
import com.winhxd.b2c.common.domain.store.vo.QRCodeInfoVO;

/**
 * @author chengyy
 * @Description: 微信分享业务接口类
 * @date 2018/8/4 13:33
 */
public interface WechatShareService {
    /**
     * @param storeUserId 当前的门店id
     * @return 图片二进制数据
     * @author chengyy
     * @date 2018/8/4 13:35
     * @Description 生成微信小程序二维码
     */
    QRCodeInfoVO generateQRCodePic(Long storeUserId);

    /**
     * @param storeUserId 门店id
     * @return 配置信息
     * @author chengyy
     * @date 2018/8/16 17:31
     * @Description 生成小程序卡片配置信息
     */
    MiniProgramConfigVO getMiniProgramConfigVO(Long storeUserId);
}
