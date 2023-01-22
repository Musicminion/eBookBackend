//package com.zzq.ebook.gateway.filters;
//
//import com.zzq.ebook.gateway.utils.jwt;
//import com.zzq.ebook.gateway.vo.UserInfo;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//@Slf4j
//@Component
//public class JwtCheckFilter implements GatewayFilter, Ordered {
//    @SneakyThrows
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        String jwtToken = exchange.getRequest().getHeaders().getFirst("token");
//        UserInfo userInfo = jwt.parseToken(jwtToken);
//        System.out.println(userInfo);
//        if(userInfo != null && userInfo.getUserRole()!= null && userInfo.getUsername()!=null) {
//            ServerHttpRequest request = exchange.getRequest().mutate()
//                    .header("userName", userInfo.getUsername())
//                    .build();
//            return chain.filter(exchange.mutate().request(request).build());
//        }
//        throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Jwt decode error");
//    }
//    @Override
//    public int getOrder() {
//        return 1;
//    }
//}
