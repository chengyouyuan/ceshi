package com.winhxd.b2c.common.exception.support;

import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.exception.BusinessException;
import com.winhxd.b2c.common.i18n.MessageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        int code;
        if (ex instanceof BusinessException) {
            code = ((BusinessException) ex).getErrorCode();
        } else {
            log.error("Controller未知异常", ex);
            code = BusinessCode.CODE_1001;
        }
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.addStaticAttribute("code", code);
        view.addStaticAttribute("message", MessageHelper.getInstance().getMessage(String.valueOf(code)));
        return new ModelAndView(view);
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