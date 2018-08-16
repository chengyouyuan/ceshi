package com.winhxd.b2c.message.service;

import com.winhxd.b2c.common.domain.store.vo.QRCodeInfoVO;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @Description: 微信分享业务接口类
 * @author chengyy
 * @date 2018/8/4 13:33
 */
public interface WechatShareService {
    /**
     * @author chengyy
     * @date 2018/8/4 13:35
     * @Description 生成微信小程序二维码
     * @param  storeUserId 当前的门店id
     * @return 图片二进制数据
     */
    QRCodeInfoVO generateQRCodePic(Long storeUserId);
}
