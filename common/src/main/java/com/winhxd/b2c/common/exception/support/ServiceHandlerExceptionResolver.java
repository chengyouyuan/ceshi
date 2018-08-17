package com.winhxd.b2c.common.exception.support;

import brave.Span;
import brave.Tracer;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.context.support.ContextHelper;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.i18n.MessageHelper;
import feign.codec.DecodeException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lixiaodong
 */
public class ServiceHandlerExceptionResolver implements HandlerExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(ServiceHandlerExceptionResolver.class);
    @Autowired
    private Tracer tracer;

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        int code;
        String message;
        BusinessException businessException = findBusinessException(ex);
        if (businessException != null) {
            code = businessException.getErrorCode();
            if (StringUtils.isNotBlank(businessException.getMessage())) {
                message = businessException.getMessage();
            } else {
                message = MessageHelper.getInstance().getMessage(String.valueOf(code));
            }
            log.warn("Controller业务异常:{},{}", code, businessException.getMessage());
        } else {
            String stackTrace = ExceptionUtils.getStackTrace(ex);
            Span currentSpan = tracer.currentSpan();
            currentSpan.error(ex);
            currentSpan.tag(ContextHelper.TRACER_API_ERROR, stackTrace);
            log.error("Controller未知异常,traceId=" + currentSpan.context().traceIdString() + ",message=" + ex.getMessage(), ex);
            code = BusinessCode.CODE_1001;
            message = MessageHelper.getInstance().getMessage(String.valueOf(BusinessCode.CODE_1001));
        }
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.addStaticAttribute("code", code);
        view.addStaticAttribute("message", message);
        return new ModelAndView(view);
    }

    private BusinessException findBusinessException(Throwable ex) {
        if (ex == null) {
            return null;
        }
        if (ex instanceof BusinessException) {
            return (BusinessException) ex;
        }
        return findBusinessException(ex.getCause());
    }
}

//@ControllerAdvice
//class GlobalExceptionHandler {
//    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
//
//    /**
//     * 系统异常处理
//     *
//     * @param e
//     * @return
//     * @throws Exception
//     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public Object defaultErrorHandler(Exception e) {
//        if (e instanceof BusinessException) {
//            return new ResponseResult<>(((BusinessException) e).getErrorCode());
//        } else {
//            logger.error("未知异常", e);
//            return new ResponseResult<>(BusinessCode.CODE_1001);
//        }
//    }
//
//}