package com.example.onlinelecturegateway.config;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class CustomFilter extends AbstractGatewayFilterFactory<CustomFilter.Config> {

    public CustomFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //filter에서 하고 싶은 내용을 재정의
        //pre filter 동작
        return ((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            log.info("Custom PRE filter : request id -> {}", request.getId());

            //post filter 동작
            return chain.filter(exchange).then(Mono.fromRunnable(()->{  //스프링5에서 지원하는 기능으로 비동기로 값을 전달할때 사용되는 객체
                log.info("Custom POST filter : response id -> {}", response.getStatusCode());
            }));
        });
    }

    @Data
    public static class Config{
        // configuration 정보 입력
        private String Message;
        private boolean preLogger;
        private boolean postLogger;
    }
}