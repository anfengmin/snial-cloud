package com.snail.gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 跨域配置
 *
 * @author Levi.
 * Created time 2026/3/3
 * @since 1.0
 */
@Component
public class GlobalCorsFilter implements WebFilter, Ordered {

    /**
     * 这里为支持的请求头，如果有自定义的header字段请自己添加
     */
    private static final String ALLOWED_HEADERS = "X-Requested-With, Content-Language, Content-Type, Authorization, credential, X-XSRF-TOKEN, isToken, token, Admin-Token, App-Token, x-request-id, X-Client-Type, X-Device-Id, X-Device-Name";
    private static final String ALLOWED_METHODS = "GET,POST,PUT,DELETE,OPTIONS,HEAD";
    private static final String ALLOWED_EXPOSE = "*";
    private static final String MAX_AGE = "18000";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (CorsUtils.isCorsRequest(request)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = response.getHeaders();
            String origin = request.getHeaders().getOrigin();
            headers.set("Access-Control-Allow-Headers", ALLOWED_HEADERS);
            headers.set("Access-Control-Allow-Methods", ALLOWED_METHODS);
            headers.set("Access-Control-Allow-Origin", origin);
            headers.set("Access-Control-Expose-Headers", ALLOWED_EXPOSE);
            headers.set("Access-Control-Max-Age", MAX_AGE);
            headers.set("Access-Control-Allow-Credentials", "true");
            headers.add(HttpHeaders.VARY, HttpHeaders.ORIGIN);
            if (request.getMethod() == HttpMethod.OPTIONS) {
                response.setStatusCode(HttpStatus.OK);
                return Mono.empty();
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
