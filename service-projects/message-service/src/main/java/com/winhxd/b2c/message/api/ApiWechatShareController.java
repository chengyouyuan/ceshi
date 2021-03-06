package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.domain.common.ApiCondition;
import com.winhxd.b2c.common.domain.message.vo.MiniProgramConfigVO;
import com.winhxd.b2c.common.domain.store.vo.QRCodeInfoVO;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.message.service.WechatShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * @author chengyy
 * @Description: 微信分享相关的接口
 * @date 2018/8/4 11:43
 */
@Api(value = "微信小程序分享接口", tags = "微信分享相关接口")
@RestController
public class ApiWechatShareController {

    private Logger logger = LoggerFactory.getLogger(ApiWechatShareController.class);
    @Autowired
    private WechatShareService wechatShareService;

    @ApiOperation(value = "生成分享小程序二维码")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1002, message = "当前用户登录的凭证无效"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @PostMapping(value = "/api-message/message/7001/v1/generateQRCodePic")
    public ResponseResult<QRCodeInfoVO> generateQRCodePic(ApiCondition codition, HttpServletResponse response) {
        ResponseResult<QRCodeInfoVO> responseResult = new ResponseResult<>();
        StoreUser storeUser = UserContext.getCurrentStoreUser();

        QRCodeInfoVO qrCodeInfoVO = wechatShareService.generateQRCodePic(storeUser.getBusinessId());
        if (qrCodeInfoVO == null || StringUtils.isEmpty(qrCodeInfoVO.getMiniProgramCodeUrl())) {
            throw new BusinessException(BusinessCode.CODE_200018);
        }
        responseResult.setData(qrCodeInfoVO);
        return responseResult;
    }

    /**
     * @return 小程序配置信息
     * @author chengyy
     * @date 2018/8/10 16:45
     * @Description 返回小程序相关配置信息
     */
    @ApiOperation(value = "返回小程序码的配置信息")
    @ApiResponses({@ApiResponse(code = BusinessCode.CODE_1002, message = "当前用户登录的凭证无效"), @ApiResponse(code = BusinessCode.CODE_OK, message = "操作成功")})
    @PostMapping(value = "/api-message/message/7002/v1/fetchMiniProgramConfig")
    public ResponseResult<MiniProgramConfigVO> fetchMiniProgramConfig(ApiCondition condition) {
        ResponseResult<MiniProgramConfigVO> responseResult = new ResponseResult<>();
        StoreUser storeUser = UserContext.getCurrentStoreUser();
        MiniProgramConfigVO configVO = wechatShareService.getMiniProgramConfigVO(storeUser.getBusinessId());
        responseResult.setData(configVO);
        return responseResult;

    }
}
