//package com.winhxd.b2c.gateway.aop;
//
//import com.winhxd.b2c.common.context.version.VersionContext;
//import org.apache.commons.lang.StringUtils;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//
//@Component
//@Aspect
//public class LoadBalancerFilterAspect {
//    @Before(value = "execution(* org.springframework.cloud.gateway.filter.LoadBalancerClientFilter.filter(..))")
//    public void setDbToReadonly(JoinPoint joinPoint) {
//        ServerWebExchange exchange = (ServerWebExchange) joinPoint.getArgs()[0];
//        String msVer = exchange.getRequest().getHeaders().getFirst(VersionContext.HEADER_NAME);
//        if (StringUtils.isNotBlank(msVer)) {
//            VersionContext.setVersion(msVer);
//        } else {
//            VersionContext.clean();
//        }
//    }
//}
