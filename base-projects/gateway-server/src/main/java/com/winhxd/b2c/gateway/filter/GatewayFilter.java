package com.winhxd.b2c.gateway.filter;

import brave.Span;
import brave.Tracer;
import com.winhxd.b2c.common.cache.Cache;
import com.winhxd.b2c.common.constant.AppConstant;
import com.winhxd.b2c.common.constant.BusinessCode;
import com.winhxd.b2c.common.constant.CacheName;
import com.winhxd.b2c.common.context.CustomerUser;
import com.winhxd.b2c.common.context.StoreUser;
import com.winhxd.b2c.common.context.support.ContextHelper;
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
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;

/**
 * 验证token及api规则过滤器
 *
 * @author lixiaodong
 */
@Component
public class GatewayFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(GatewayFilter.class);

    @Autowired
    private Cache cache;
    @Autowired
    private Tracer tracer;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path = request.getURI().getPath();
        Matcher matcher = ContextHelper.PATTERN_API_PATH.matcher(path);
        if (!matcher.matches()) {
            return error(response, BusinessCode.CODE_1009);
        }
        MediaType contentType = request.getHeaders().getContentType();
        String pathTag = matcher.group(2);
        String apiCode = matcher.group(3);

        final Span currentSpan = tracer.currentSpan();
        currentSpan.tag(ContextHelper.TRACER_API_CODE, apiCode);

        ServerHttpRequest.Builder requestBuilder = null;
        //验证token
        if (StringUtils.isBlank(pathTag)) {
            String token = request.getHeaders().getFirst("token");
            String grp = request.getHeaders().getFirst("grp");
            if (StringUtils.isBlank(token) || StringUtils.isBlank(grp)) {
                return error(response, BusinessCode.CODE_1002);
            }
            currentSpan.tag(ContextHelper.TRACER_API_TOKEN, token);
            currentSpan.tag(ContextHelper.TRACER_API_GRP, grp);
            String key, tokenJson, header;
            switch (grp) {
                case AppConstant.GRP_CUSTOMER:
                    header = ContextHelper.HEADER_USER_CUSTOMER;
                    key = CacheName.CUSTOMER_USER_INFO_TOKEN + token;
                    tokenJson = cache.get(key);
                    break;
                case AppConstant.GRP_STORE:
                    header = ContextHelper.HEADER_USER_STORE;
                    key = CacheName.STORE_USER_INFO_TOKEN + token;
                    tokenJson = cache.get(key);
                    break;
                default:
                    return error(response, BusinessCode.CODE_1002);
            }
            if (StringUtils.isBlank(tokenJson)) {
                return error(response, BusinessCode.CODE_1002);
            }
            if (grp.equals(AppConstant.GRP_CUSTOMER)) {
                CustomerUser customerUser = ContextHelper.getHeaderObject(tokenJson, CustomerUser.class);
                currentSpan.tag(ContextHelper.TRACER_API_CUSTOMER, customerUser.getCustomerId().toString());
            } else {
                StoreUser storeUser = ContextHelper.getHeaderObject(tokenJson, StoreUser.class);
                currentSpan.tag(ContextHelper.TRACER_API_STORE, storeUser.getStoreCustomerId().toString());
            }
            requestBuilder = request.mutate().header(header, ContextHelper.encode(tokenJson));
        }

        ServerHttpRequestDecorator requestDecorator = new ServerHttpRequestDecorator(requestBuilder == null ? request : requestBuilder.build()) {
            @Override
            public Flux<DataBuffer> getBody() {
                return super.getBody().flatMap(dataBuffer -> {
                    if (MediaType.APPLICATION_JSON.includes(contentType)
                            || MediaType.APPLICATION_FORM_URLENCODED.includes(contentType)) {
                        byte[] bs = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bs);
                        String req = new String(bs, StandardCharsets.UTF_8);
                        currentSpan.tag(ContextHelper.TRACER_API_REQUEST, req);
                        logger.debug("Gateway请求数据: {}", req);
                        dataBuffer.readPosition(0);
                    }
                    return Flux.just(dataBuffer);
                });
            }
        };

        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                Flux<DataBuffer> flux = Flux.from(body).flatMap(
                        dataBuffer -> {
                            if (MediaType.APPLICATION_JSON.includes(contentType)
                                    || MediaType.APPLICATION_FORM_URLENCODED.includes(contentType)) {
                                byte[] bs = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bs);
                                String repJson = new String(bs, StandardCharsets.UTF_8);
                                currentSpan.tag(ContextHelper.TRACER_API_RESPONSE, repJson);
                                if (!ContextHelper.PATH_TAG_COOPERATION.equals(pathTag)) {
                                    ResponseResult result = JsonUtil.tryParseJSONObject(repJson, ResponseResult.class);
                                    if (result != null) {
                                        currentSpan.tag(ContextHelper.TRACER_API_RESULT, String.valueOf(result.getCode()));
                                    }
                                }
                                logger.debug("Gateway响应数据: {}", repJson);
                                dataBuffer.readPosition(0);
                            }
                            return Flux.just(dataBuffer);
                        }
                );
                getHeaders().set(ContextHelper.TRACER_API_TRACE_ID, currentSpan.context().traceIdString());
                return super.writeWith(flux);
            }
        };

        return chain.filter(exchange.mutate().request(requestDecorator).response(responseDecorator).build());
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
