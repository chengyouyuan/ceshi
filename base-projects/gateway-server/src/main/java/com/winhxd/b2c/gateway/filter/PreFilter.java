//package com.winhxd.b2c.gateway.filter;
//
//import brave.Span;
//import brave.Tracer;
//import com.netflix.zuul.context.RequestContext;
//import com.winhxd.b2c.common.cache.Cache;
//import com.winhxd.b2c.common.constant.AppConstant;
//import com.winhxd.b2c.common.constant.BusinessCode;
//import com.winhxd.b2c.common.constant.CacheName;
//import com.winhxd.b2c.common.context.CustomerUser;
//import com.winhxd.b2c.common.context.StoreUser;
//import com.winhxd.b2c.common.context.support.ContextHelper;
//import com.winhxd.b2c.common.util.JsonUtil;
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.util.CollectionUtils;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.regex.Matcher;
//
///***
// *
// * @author likai
// * @date 2017年5月4日 下午4:52:58
// * @Description
// * @version
// */
//@Component
//public class PreFilter extends AbstractFilter {
//    private static final Logger logger = LoggerFactory.getLogger(PreFilter.class);
//    private static final String METHOD_GET = "GET";
//
//    @Autowired
//    private Cache cache;
//
//    @Autowired
//    private Tracer tracer;
//
//    @Override
//    public Object run() {
////        throw new RuntimeException("aaa");
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpServletRequest request = context.getRequest();
//        String uri = request.getRequestURI();
//        Matcher matcher = ContextHelper.PATTERN_API_PATH.matcher(uri);
//        if (!matcher.matches()) {
//            return error(context, BusinessCode.CODE_1002);
//        }
//        String security = matcher.group(2);
//        String apiCode = matcher.group(3);
//
//        final Span currentSpan = tracer.currentSpan();
//        currentSpan.tag(ContextHelper.TRACER_API_CODE, apiCode);
//
//        //验证token
//        if (StringUtils.isBlank(security)) {
//            String token = request.getHeader("token");
//            String grp = request.getHeader("grp");
//            if (StringUtils.isBlank(token) || StringUtils.isBlank(grp)) {
//                return error(context, BusinessCode.CODE_1002);
//            }
//            String key, tokenJson, header;
//            switch (grp) {
//                case AppConstant.GRP_CUSTOMER:
//                    header = ContextHelper.HEADER_USER_CUSTOMER;
//                    key = CacheName.CUSTOMER_USER_INFO_TOKEN + token;
//                    tokenJson = cache.get(key);
//                    break;
//                case AppConstant.GRP_STORE:
//                    header = ContextHelper.HEADER_USER_STORE;
//                    key = CacheName.STORE_USER_INFO_TOKEN + token;
//                    tokenJson = cache.get(key);
//                    break;
//                default:
//                    return error(context, BusinessCode.CODE_1002);
//            }
//            if (StringUtils.isBlank(tokenJson)) {
//                return error(context, BusinessCode.CODE_1002);
//            }
//            if (grp.equals(AppConstant.GRP_CUSTOMER)) {
//                CustomerUser customerUser = ContextHelper.getHeaderObject(tokenJson, CustomerUser.class);
//                currentSpan.tag(ContextHelper.TRACER_API_CUSTOMER, customerUser.getCustomerId().toString());
//            } else {
//                StoreUser storeUser = ContextHelper.getHeaderObject(tokenJson, StoreUser.class);
//                currentSpan.tag(ContextHelper.TRACER_API_STORE, storeUser.getStoreCustomerId().toString());
//            }
//            context.getZuulRequestHeaders().put(header, ContextHelper.encode(tokenJson));
//        }
//
//        if (METHOD_GET.equalsIgnoreCase(request.getMethod())) {
//            return doGet(context);
//        } else {
//            return doPost(context);
//        }
//    }
//
//    private Object doGet(RequestContext context) {
//        String json = context.getRequest().getQueryString();
//        tracer.currentSpan().tag(ContextHelper.TRACER_API_REQUEST, json);
//        return null;
//    }
//
//    private Object doPost(RequestContext context) {
//        try {
//            HttpServletRequest request = context.getRequest();
//            MediaType mediaType = MediaType.valueOf(request.getContentType());
//            if (MediaType.APPLICATION_JSON.includes(mediaType)) {
//                ServletInputStream inputStream = request.getInputStream();
//                if (request.getContentLength() > 0) {
//                    String json = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//                    tracer.currentSpan().tag(ContextHelper.TRACER_API_REQUEST, json);
//                }
//            } else if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
//                if (!CollectionUtils.isEmpty(request.getParameterMap())) {
//                    String json = JsonUtil.toJSONString(request.getParameterMap());
//                    tracer.currentSpan().tag(ContextHelper.TRACER_API_REQUEST, json);
//                }
//            }
//        } catch (IOException e) {
//            logger.error("记录请求数据错误", e);
//        }
//        return null;
//    }
//
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//
//}
