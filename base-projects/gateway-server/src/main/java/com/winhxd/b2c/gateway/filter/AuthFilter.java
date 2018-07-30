package com.winhxd.b2c.gateway.filter;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * 验证token及api规则过滤器
 *
 * @author lixiaodong
 */
@Component
public class AuthFilter implements GlobalFilter, Ordered {
    private static final Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        logger.info(request.getURI().toString());


        ServerHttpResponseDecorator responseDecorator = new ServerHttpResponseDecorator(exchange.getResponse()) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

//                ResolvableType inElementType = ResolvableType.forClass(config.getInClass());
//                ResolvableType outElementType = ResolvableType.forClass(config.getOutClass());
//                MediaType contentType = exchange.getResponse().getHeaders().getContentType();
//                Optional<HttpMessageReader<?>> reader = getHttpMessageReader(codecConfigurer, inElementType, contentType);
//                Optional<HttpMessageWriter<?>> writer = getHttpMessageWriter(codecConfigurer, outElementType, null);

//                if (reader.isPresent() && writer.isPresent()) {
//
//                    ModifyResponseBodyGatewayFilterFactory.ResponseAdapter responseAdapter = new ModifyResponseBodyGatewayFilterFactory.ResponseAdapter(body, getDelegate().getHeaders());
//
//                    Flux<?> modified = reader.get().read(inElementType, responseAdapter, config.getInHints())
//                            .cast(inElementType.resolve())
//                            .flatMap(originalBody -> Flux.just(config.rewriteFunction.apply(exchange, originalBody)))
//                            .cast(outElementType.resolve());
//
//                    return getDelegate().writeWith(
//                            writer.get().write((Publisher) modified, outElementType, null, getDelegate(),
//                                    config.getOutHints())
//                    );
//
//                }

                return getDelegate().writeWith(body);
            }

            @Override
            public Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body) {
                return writeWith(Flux.from(body).flatMapSequential(p -> p));
            }
        };

        Mono<Void> filter = chain.filter(exchange.mutate().response(responseDecorator).build());


        return filter;
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
