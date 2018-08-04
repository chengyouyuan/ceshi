package com.winhxd.b2c.message.api;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.message.service.WechatShareService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: 微信分享相关的接口
 * @author chengyy
 * @date 2018/8/4 11:43
 */
@Api(value = "微信小程序分享接口",tags = "微信分享相关接口")
@RestController
public class ApiWechatShareController {
    private Logger logger = LoggerFactory.getLogger(ApiWechatShareController.class);
    @Autowired
    private WechatShareService wechatShareService;
    @ApiOperation(value = "生成分享小程序二维码")
    @ApiResponses({@ApiResponse(code = 200002,message = "参数错误,门店id为空")})
    @GetMapping(value="/b2c-message-service/wechat/share/7001/v1/generateQRCodePic/{storeUserId}")
    public void generateQRCodePic(@PathVariable("storeUserId")Long storeUserId, HttpServletResponse response){
        if(storeUserId == null){
            logger.error("ApiWechatShareController -> generateQRCodePic方法缺少参数storeUserId");
            throw new BusinessException(BusinessCode.CODE_200002);
        }
        byte[] data = wechatShareService.generateQRCodePic(storeUserId);
        if(data != null && data.length > 0){
            response.setContentType("image/png");
            try {
                OutputStream  out = response.getOutputStream();
                out.write(data);
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
