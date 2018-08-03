package com.winhxd.b2c.common.exception;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.domain.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 系统异常处理
     *
     * @param e
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Object defaultErrorHandler(Exception e) {
        if (e instanceof BusinessException) {
            return new ResponseResult<>(((BusinessException) e).getErrorCode());
        } else {
            logger.error("未知异常", e);
            return new ResponseResult<>(BusinessCode.CODE_1001);
        }
    }

}