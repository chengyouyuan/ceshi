package com.winhxd.b2c.gateway.filter;

import brave.Span;
import brave.Tracer;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.AppConstant;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.UserContext;
import com.winhxd.b2c.common.domain.ResponseResult;
import com.winhxd.b2c.common.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.reactivestreams.Publisher;
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
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
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
    @Autowired
    private Tracer tracer;

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

        final Span currentSpan = tracer.currentSpan();
        currentSpan.tag("api.serviceName", serviceName);
        currentSpan.tag("api.apiCode", apiCode);

        ServerHttpRequest.Builder requestBuilder = null;
        //验证token
        if (StringUtils.isBlank(security)) {
            String token = request.getHeaders().getFirst("token");
            String grp = request.getHeaders().getFirst("grp");
            if (StringUtils.isBlank(token) || StringUtils.isBlank(grp)) {
                return error(response, BusinessCode.CODE_1002);
            }
            String key, tokenJson, header;
            switch (grp) {
                case AppConstant.GRP_CUSTOMER:
                    header = UserContext.HEADER_USER_CUSTOMER;
                    key = CacheName.CUSTOMER_USER_INFO_TOKEN + token;
                    tokenJson = cache.get(key);
                    break;
                case AppConstant.GRP_STORE:
                    header = UserContext.HEADER_USER_STORE;
                    key = CacheName.STORE_USER_INFO_TOKEN + token;
                    tokenJson = cache.get(key);
                    break;
                default:
                    return error(response, BusinessCode.CODE_1002);
            }
//            if (StringUtils.isBlank(tokenJson)) {
//                return error(response, BusinessCode.CODE_1002);
//            }
//            requestBuilder = request.mutate().header(header, UserContext.encode(tokenJson));


            // todo 使用临时用户, 等登录接口写完后, 切换为正式使用token
            StoreUser storeUser = new StoreUser();
            storeUser.setBusinessId(12L);
            storeUser.setStoreCustomerId(1000L);
            CustomerUser customerUser = new CustomerUser();
            customerUser.setOpenId("asldjkfalskdjflakjsdf");
            customerUser.setCustomerId(11L);
            requestBuilder = request.mutate()
                    .header(UserContext.HEADER_USER_CUSTOMER, JsonUtil.toJSONString(customerUser))
                    .header(UserContext.HEADER_USER_STORE, JsonUtil.toJSONString(storeUser));
        }


        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Flux<DataBuffer> flux = Flux.from(body).flatMap(
                        dataBuffer -> {
                            byte[] bs = new byte[dataBuffer.readableByteCount()];
                            dataBuffer.read(bs);
                            String repJson = new String(bs, StandardCharsets.UTF_8);
                            currentSpan.tag("api.response", repJson);
                            dataBuffer.readPosition(0);
                            return Flux.just(dataBuffer);
                        }
                );
                getHeaders().set("API-TRACE-ID", currentSpan.context().traceIdString());
                return super.writeWith(flux);
            }
        };

        if (requestBuilder == null) {
            return chain.filter(exchange.mutate().response(responseDecorator).build());
        } else {
            return chain.filter(exchange.mutate().response(responseDecorator).request(requestBuilder.build()).build());
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
