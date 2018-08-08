package com.winhxd.b2c.gateway.filter;

import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.AppConstant;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证token及api规则过滤器
 *
 * @author lixiaodong
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    @Autowired
    private Cache cache;

    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);
    private static final Pattern uriPattern = Pattern.compile("^/api-[a-zA-Z]+/([a-zA-Z]+)(/security)?/(\\d+)/.*");


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        Matcher matcher = uriPattern.matcher(path);
        if (!matcher.matches()) {
            return error(response, BusinessCode.CODE_1009);
        }
        String serviceName = matcher.group(1);
        String security = matcher.group(2);
        String apiCode = matcher.group(3);

        ServerHttpRequest.Builder requestBuilder = null;
        //验证token
        if (StringUtils.isBlank(security)) {
            String token = request.getHeaders().getFirst("token");
            String grp = request.getHeaders().getFirst("grp");
            if (StringUtils.isBlank(token) || StringUtils.isBlank(grp)) {
                return error(response, BusinessCode.CODE_1002);
            }
            String key, json, header;
            switch (grp) {
                case AppConstant.GRP_CUSTOMER:
                    header = UserContext.HEADER_USER_CUSTOMER;
                    key = CacheName.CUSTOMER_USER_INFO_TOKEN + token;
                    json = cache.get(key);
                    break;
                case AppConstant.GRP_STORE:
                    header = UserContext.HEADER_USER_STORE;
                    key = CacheName.STORE_USER_INFO_TOKEN + token;
                    json = cache.get(key);
                    break;
                default:
                    return error(response, BusinessCode.CODE_1002);
            }
//            if (StringUtils.isBlank(json)) {
//                return error(response, BusinessCode.CODE_1002);
//            }

//            requestBuilder = request.mutate().header(header, UserContext.encode(json));


            StoreUser storeUser = new StoreUser();
            storeUser.setBusinessId(12L);
            storeUser.setStoreCustomerId(1000L);
            requestBuilder = request.mutate()
                    .header(UserContext.HEADER_USER_STORE, JsonUtil.toJSONString(storeUser));
        }


//        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
//            @Override
//            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
//
////                ResolvableType inElementType = ResolvableType.forClass(config.getInClass());
////                ResolvableType outElementType = ResolvableType.forClass(config.getOutClass());
////                MediaType contentType = exchange.getResponse().getHeaders().getContentType();
////                Optional<HttpMessageReader<?>> reader = getHttpMessageReader(codecConfigurer, inElementType, contentType);
////                Optional<HttpMessageWriter<?>> writer = getHttpMessageWriter(codecConfigurer, outElementType, null);
//
////                if (reader.isPresent() && writer.isPresent()) {
////
////                    ModifyResponseBodyGatewayFilterFactory.ResponseAdapter responseAdapter = new ModifyResponseBodyGatewayFilterFactory.ResponseAdapter(body, getDelegate().getHeaders());
////
////                    Flux<?> modified = reader.get().read(inElementType, responseAdapter, config.getInHints())
////                            .cast(inElementType.resolve())
////                            .flatMap(originalBody -> Flux.just(config.rewriteFunction.apply(exchange, originalBody)))
////                            .cast(outElementType.resolve());
////
////                    return getDelegate().writeWith(
////                            writer.get().write((Publisher) modified, outElementType, null, getDelegate(),
////                                    config.getOutHints())
////                    );
////
////                }
//
//                return getDelegate().writeWith(body);
//            }
//
//            @Override
//            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
//                return writeWith(Flux.from(body).flatMapSequential(p -> p));
//            }
//        };

//        Mono<Void> filter = chain.filter(exchange.mutate().response(responseDecorator).build());

        if (requestBuilder == null) {
            return chain.filter(exchange);
        } else {
            return chain.filter(exchange.mutate().request(request).build());
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }

    private Mono<Void> error(ServerHttpResponse response, int businessCode) {
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        ResponseResult result = new ResponseResult(businessCode);
        byte[] bytes = JsonUtil.toJSONString(result).getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Flux.just(buffer));
    }
}
